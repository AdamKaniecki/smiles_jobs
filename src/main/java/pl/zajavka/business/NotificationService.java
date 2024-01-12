package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.Notification;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.database.repository.NotificationRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.database.repository.mapper.NotificationMapper;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static pl.zajavka.infrastructure.database.entity.Status.HIRED;


@Slf4j
@Service
@AllArgsConstructor
public class NotificationService {
    private NotificationRepository notificationRepository;
    private NotificationMapper notificationMapper;
    private UserMapper userMapper;
    private JobOfferService jobOfferService;
    private UserService userService;
    private CvService cvService;
    private JobOfferMapper jobOfferMapper;
    private CvMapper cvMapper;
    private UserRepository userRepository;


    public List<Notification> findByUser(User user) {
        UserEntity userEntity = userMapper.map(user);
        List<NotificationEntity> notificationEntities = notificationRepository.findByUser(userEntity);
        return notificationMapper.mapToList(notificationEntities);
    }

    public Notification createNotification(JobOffer jobOffer, CV cv, User loggedInUser, User adresat) {

        NotificationEntity notificationEntity = NotificationEntity.builder()
                .status(Status.UNDER_REVIEW)
                .candidateMessage("Wysłano CV, oczekuj na propozycję rozmowy")
                .companyMessage("chcę u was pracować")
                .jobOffer(jobOfferMapper.map(jobOffer))
                .cv(cvMapper.map(cv))
                .senderUser(userMapper.map(loggedInUser))
                .receiverUser(userMapper.map(adresat))
                .build();


        notificationRepository.save(notificationEntity);


        return notificationMapper.map(notificationEntity);
    }

    public void save(Notification notification) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
        notificationRepository.save(notificationEntity);
    }

    public Notification findById(Integer notificationId) {
        NotificationEntity notificationEntity = notificationRepository.findById(notificationId)
                .orElse(null);
        return notificationMapper.map(notificationEntity);

    }

    @Transactional
    public void arrangeInterview(Notification notification, User loggedInUser, User adresat, LocalDateTime proposedDateTime) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);

        notificationEntity.setStatus(Status.MEETING_SCHEDULING);
        notificationEntity.setDateTime(proposedDateTime);
        notificationEntity.setCandidateMessage("zaakceptuj termin rozmowy lub poproś o inny");
        notificationEntity.setCompanyMessage("wysłano propozycję terminu rozmowy");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(adresat));

        notificationRepository.save(notificationEntity);

        userService.save(loggedInUser);
        userService.save(adresat);
    }

    @Transactional
    public void changeMeetingDate(Notification notification, User loggedInUser, User adresat) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
        notificationEntity.setDateTime(null);
        notificationEntity.setCompanyMessage("proszę o zmianę terminu");
        notificationEntity.setCandidateMessage("wysłano prośbę o zmianę terminu");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(adresat));
        notificationRepository.save(notificationEntity);
        userService.save(loggedInUser);
        userService.save(adresat);
    }

    public void acceptMeetingDateTime(Notification notification, User loggedInUser, User adresat) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
        notificationEntity.setStatus(Status.WAITING_FOR_INTERVIEW);
        notificationEntity.setCompanyMessage("zaakceptowano termin");
        notificationEntity.setCandidateMessage("wysłano akceptację terminu");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(adresat));
        notificationRepository.save(notificationEntity);
        userService.save(loggedInUser);
        userService.save(adresat);
    }

    public boolean isNotificationDateTimeAfterNow(Notification notification) {
        LocalDateTime notificationDateTime = notification.getDateTime();
        return notificationDateTime != null && LocalDateTime.now().isAfter(notificationDateTime);
    }

    public void declineCandidate(Notification notification, User loggedInUser, User adresat) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
        notificationEntity.setStatus(Status.REJECT);
        notificationEntity.setCompanyMessage("wysłano odpowiedź odmowną ");
        notificationEntity.setCandidateMessage("niestety nie zostałeś zatrudniony");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(adresat));
        notificationRepository.save(notificationEntity);
        userService.save(loggedInUser);
        userService.save(adresat);
    }

    public void hiredCandidate(Notification notification, User loggedInUser, User adresat) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
        notificationEntity.setStatus(HIRED);
        notificationEntity.setCompanyMessage("wysłano odpowiedź pozytywną ");
        notificationEntity.setCandidateMessage("Gratulacje! zostałeś zatrudniony, twoje status zostaje zmieniony na bierny");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(adresat));
        notificationRepository.save(notificationEntity);

        UserEntity userEntity = userMapper.map(adresat);
        userEntity.setActive(false);
        userService.save(loggedInUser);
        userService.save(adresat);


    }

}





