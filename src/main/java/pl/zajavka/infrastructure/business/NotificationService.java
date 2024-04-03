package pl.zajavka.infrastructure.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.controller.dto.mapper.NotificationMapperDTO;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.database.repository.NotificationRepository;
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
import java.util.stream.Collectors;

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
    private JobOfferRepository jobOfferRepository;
    private CvRepository cvRepository;






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


    @Transactional
    public void arrangeInterview(Notification notification, User loggedInUser, User adresat, LocalDateTime proposedDateTime) {
        Status currentStatus = notification.getStatus();
        if (currentStatus == Status.UNDER_REVIEW || currentStatus == Status.MEETING_SCHEDULING) {
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
        } else {
            throw new IllegalStateException("Cannot arrange interview when the status is not Under Review or Meeting Scheduling.");
        }
    }
    @Transactional
    public void changeMeetingDate(Notification notification, User loggedInUser, User adresat) {
        if (notification.getStatus() == Status.MEETING_SCHEDULING) {
            // Możesz dokonać zmiany daty spotkania tylko jeśli status jest "Meeting Scheduling"
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setDateTime(null);
            notificationEntity.setCompanyMessage("proszę o zmianę terminu");
            notificationEntity.setCandidateMessage("wysłano prośbę o zmianę terminu");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(adresat));
            notificationJpaRepository.save(notificationEntity);
            userService.save(loggedInUser);
            userService.save(adresat);
        } else {
            // W przeciwnym razie zablokuj zmianę daty
            throw new IllegalStateException("Cannot change meeting date when the status is not Meeting Scheduling.");
        }
    }

    @Transactional
    public void acceptMeetingDateTime(Notification notification, User loggedInUser, User adresat) {
        Status currentStatus = notification.getStatus();
        if (currentStatus == Status.MEETING_SCHEDULING) {
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setStatus(Status.WAITING_FOR_INTERVIEW);
            notificationEntity.setCompanyMessage("zaakceptowano termin");
            notificationEntity.setCandidateMessage("wysłano akceptację terminu");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(adresat));
            notificationJpaRepository.save(notificationEntity);
            userService.save(loggedInUser);
            userService.save(adresat);
        } else {
            throw new IllegalStateException("Cannot accept meeting date when the status is not Meeting Scheduling.");
        }
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

    @Transactional
    public void hiredCandidate(Notification notification, User loggedInUser, User adresat) {
        Status currentStatus = notification.getStatus();
        if (currentStatus != Status.REJECT) {
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setStatus(HIRED);
            notificationEntity.setCompanyMessage("wysłano odpowiedź pozytywną ");
            notificationEntity.setCandidateMessage("Gratulacje! zostałeś zatrudniony, twoje status zostaje zmieniony na bierny");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(adresat));
//            poprawić to notification Repository
            notificationJpaRepository.save(notificationEntity);

            CV cv = notification.getCv();
            cv.setVisible(false);
            cvRepository.saveCV(cv);
            userService.save(loggedInUser);
            userService.save(adresat);

            JobOffer jobOffer = notification.getJobOffer();

            // Zwiększ liczbę zatrudnionych pracowników
            jobOffer.setHiredCount(jobOffer.getHiredCount() + 1);

            // Sprawdź, czy liczba zatrudnionych pracowników osiągnęła docelową wartość
            if (jobOffer.isFullyStaffed()) {
                // Jeśli tak, ustaw pole active na false
                jobOffer.setActive(false);
            }

            jobOfferRepository.saveJobOffer(jobOffer);
        } else {
            throw new IllegalStateException("Cannot hire candidate when the status is REJECT.");
        }

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

    @Transactional
    public boolean canChangeMeetingDate(Notification notification) {
        return notification.getStatus() != Status.WAITING_FOR_INTERVIEW;
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




