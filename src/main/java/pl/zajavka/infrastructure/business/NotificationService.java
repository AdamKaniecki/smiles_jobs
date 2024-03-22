package pl.zajavka.infrastructure.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.database.repository.jpa.NotificationJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.database.repository.mapper.NotificationMapper;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static pl.zajavka.infrastructure.database.entity.Status.HIRED;


@Slf4j
@Service
@AllArgsConstructor
public class NotificationService {
    private NotificationJpaRepository notificationJpaRepository;
    private NotificationMapper notificationMapper;
    private UserMapper userMapper;
    private UserService userService;
    private JobOfferMapper jobOfferMapper;
    private CvMapper cvMapper;





    @Transactional
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

        notificationJpaRepository.save(notificationEntity);

        return notificationMapper.map(notificationEntity);
    }


    public void save(Notification notification) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
        notificationJpaRepository.save(notificationEntity);
    }

//    public Notification findById(Integer notificationId) {
//        NotificationEntity notificationEntity = notificationJpaRepository.findById(notificationId)
//                .orElse(null);
//        return notificationMapper.map(notificationEntity);
//
//    }

    @Transactional
    public void arrangeInterview(Notification notification, User loggedInUser, User adresat, LocalDateTime proposedDateTime) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);

        notificationEntity.setStatus(Status.MEETING_SCHEDULING);
        notificationEntity.setDateTime(proposedDateTime);
        notificationEntity.setCandidateMessage("zaakceptuj termin rozmowy lub poproś o inny");
        notificationEntity.setCompanyMessage("wysłano propozycję terminu rozmowy");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(adresat));

        notificationJpaRepository.save(notificationEntity);

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
        notificationJpaRepository.save(notificationEntity);
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
        notificationJpaRepository.save(notificationEntity);
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
        notificationJpaRepository.save(notificationEntity);
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
        notificationJpaRepository.save(notificationEntity);

        UserEntity userEntity = userMapper.map(adresat);
        userEntity.setActive(false);
        userService.save(loggedInUser);
        userService.save(adresat);


    }

    public void deleteNotificationsByCvId(Integer cvId) {
        notificationJpaRepository.deleteByCvId(cvId);
    }

    public boolean hasUserSentCVToJobOffer(User loggedInUser, JobOffer jobOffer) {
        UserEntity userEntity = userMapper.map(loggedInUser);
        JobOfferEntity jobOfferEntity = jobOfferMapper.map(jobOffer);
        return notificationJpaRepository.existsBySenderUserAndJobOffer(userEntity, jobOfferEntity);
    }


    public Page<Notification> findAllNotificationsForPage(Pageable pageable) {
        Page<NotificationEntity> notificationEntities = notificationJpaRepository.findAll(pageable);
        return notificationEntities.map(notificationMapper::map);
    }



//    public List<Notification> findByUser(User user) {
//        UserEntity userEntity = userMapper.map(user);
//        List<NotificationEntity> notificationEntities = notificationJpaRepository.findByUser(userEntity);
//        return notificationMapper.mapToList(notificationEntities);
//    }


//    public Notification findUserRorPagination(User user){
//        UserEntity userEntity = userMapper.map(user);
//        NotificationEntity notificationEntity = notificationRepository.findUserRorPagination(userEntity);
//        return notificationMapper.map(notificationEntity);
//    }



//    @Transactional
//    public Page<Notification> findByUser(User user, Pageable pageable) {
//        // Pobierz stronę powiadomień z bazy danych
//        UserEntity userEntity = userMapper.map(user);
//        Page<NotificationEntity> notificationEntitiesPage = notificationRepository.findByUser(userEntity, pageable);
//        return notificationMapper.mapToPage(notificationEntitiesPage,pageable);

//        // Mapuj powiadomienia na DTO
//        List<NotificationDTO> notificationDTOs = notificationEntityPage.getContent().stream()
//                .map(notificationMapper::map)
//                .collect(Collectors.toList());

        // Zwróć stronę paginacji z zmapowanymi obiektami DTO

    }





//    public Page<Notification> findByUser(User user, Pageable pageable ) {
//        UserEntity userEntity = userMapper.map(user);
//      List<NotificationEntity> notificationEntities = notificationRepository.findByUser(userEntity);
//        return notificationMapper.mapToList(notificationEntities);
//}





