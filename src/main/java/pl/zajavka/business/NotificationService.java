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
import pl.zajavka.infrastructure.database.repository.NotificationRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.database.repository.mapper.NotificationMapper;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

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

//            Notification newNotification = Notification.builder()
//                    .message("narazie nic")
//                    .jobOffer(jobOffer)
//                    .cv(cv)
//                    .user(loggedInUser)
//                    .build();

        NotificationEntity notificationEntity= NotificationEntity.builder()
                .candidateMessage("Wysłano CV, oczekuj na propozycję rozmowy")
                .companyMessage("chcę u was pracować")
                .jobOffer(jobOfferMapper.map(jobOffer))
                .cv(cvMapper.map(cv))
                .senderUser(userMapper.map(loggedInUser))
                .receiverUser(userMapper.map(adresat))
                .build();

        // Ustaw null, aby uniknąć rekurencji
//        loggedInUser.setNotifications(null);
        notificationRepository.save(notificationEntity);
//            NotificationEntity savedNotificationEntity = notificationMapper.map(newNotification);

        // Przemapuj NotificationEntity na Notification
//            Notification notification = notificationMapper.map(savedNotificationEntity);

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
    public void updateNotificationAndUsers(Notification notification, User loggedInUser, User adresat, LocalDateTime proposedDateTime) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
//        log.info("Updating Notification: {}", notificationEntity);

        // Ustaw propozycję daty i wiadomość w NotificationEntity
        notificationEntity.setDateTime(proposedDateTime);
        notificationEntity.setCandidateMessage("zaakceptuj termin rozmowy lub poproś o inny");
        notificationEntity.setCompanyMessage("wysłano propozycję terminu rozmowy");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(adresat));

        // Zapisz zmodyfikowaną NotificationEntity
        notificationRepository.save(notificationEntity);

        // Dodaj zapis do encji User w razie potrzeby
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
    }


//    public Optional<Notification> findById(Integer jobOfferId) {
//    }


