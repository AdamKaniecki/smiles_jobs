package pl.zajavka.infrastructure.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.controller.dto.mapper.NotificationMapperDTO;
import pl.zajavka.infrastructure.business.dao.NotificationDAO;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
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

    private NotificationMapper notificationMapper;
    private UserMapper userMapper;
    private UserService userService;
    private JobOfferMapper jobOfferMapper;
    private CvMapper cvMapper;
    private JobOfferRepository jobOfferRepository;
    private CvRepository cvRepository;
    private CvService cvService;
    private final NotificationDAO notificationDAO;
    private NotificationMapperDTO notificationMapperDTO;



    @Transactional
    public Notification createNotification(JobOffer jobOffer, CV cv, User loggedInUser, User adresat) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .status(Status.UNDER_REVIEW)
                .candidateMessage("CV sent, await interview offer")
                .companyMessage("I would like to work for you")
                .jobOffer(jobOfferMapper.map(jobOffer))
                .cv(cvMapper.map(cv))
                .senderUser(userMapper.map(loggedInUser))
                .receiverUser(userMapper.map(adresat))
                .build();

        notificationDAO.save(notificationEntity);

        return notificationMapper.map(notificationEntity);
    }


    public void save(Notification notification) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
        notificationDAO.save(notificationEntity);
    }


    @Transactional
    public void arrangeInterview(Notification notification, User loggedInUser, User recipient, LocalDateTime proposedDateTime) {
        Status currentStatus = notification.getStatus();
        if (currentStatus == Status.UNDER_REVIEW || currentStatus == Status.MEETING_SCHEDULING) {
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setStatus(Status.MEETING_SCHEDULING);
            notificationEntity.setDateTime(proposedDateTime);
            notificationEntity.setCandidateMessage("Accept the meeting schedule or request another");
            notificationEntity.setCompanyMessage("The meeting schedule proposal has been sent");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(recipient));
            notificationDAO.save(notificationEntity);
            userService.save(loggedInUser);
            userService.save(recipient);
        } else {
            throw new IllegalStateException("Cannot arrange interview when the status is not Under Review or Meeting Scheduling.");
        }
    }

    @Transactional
    public void changeMeetingDate(Notification notification, User loggedInUser, User recipient) {
        if (notification.getStatus() == Status.MEETING_SCHEDULING) {
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setDateTime(null);
            notificationEntity.setCompanyMessage("Please request a change of schedule");
            notificationEntity.setCandidateMessage("The request to change the schedule has been sent");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(recipient));
            notificationDAO.save(notificationEntity);
            userService.save(loggedInUser);
            userService.save(recipient);
        } else {
            throw new IllegalStateException("Cannot change meeting date when the status is not Meeting Scheduling.");
        }
    }

    @Transactional
    public void acceptMeetingDateTime(Notification notification, User loggedInUser, User recipient) {
        Status currentStatus = notification.getStatus();
        if (currentStatus == Status.MEETING_SCHEDULING) {
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setStatus(Status.WAITING_FOR_INTERVIEW);
            notificationEntity.setCompanyMessage("The meeting schedule has been accepted");
            notificationEntity.setCandidateMessage("The meeting schedule has been accepted");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(recipient));
            notificationDAO.save(notificationEntity);
            userService.save(loggedInUser);
            userService.save(recipient);
        } else {
            throw new IllegalStateException("Cannot accept meeting date when the status is not Meeting Scheduling.");
        }
    }


    public boolean isNotificationDateTimeAfterNow(Notification notification) {
        LocalDateTime notificationDateTime = notification.getDateTime();
        return notificationDateTime != null && LocalDateTime.now().isAfter(notificationDateTime);
    }

    public void declineCandidate(Notification notification, User loggedInUser, User recipient) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
        notificationEntity.setStatus(Status.REJECT);
        notificationEntity.setCompanyMessage("The rejection response has been sent");
        notificationEntity.setCandidateMessage("Unfortunately, you have not been hired");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(recipient));
        notificationDAO.save(notificationEntity);
        userService.save(loggedInUser);
        userService.save(recipient);
    }

    @Transactional
    public void hiredCandidate(Notification notification, User loggedInUser, User recipient) {
        Status currentStatus = notification.getStatus();
        if (currentStatus != Status.REJECT) {
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setStatus(HIRED);
            notificationEntity.setCompanyMessage("wysłano odpowiedź pozytywną ");
            notificationEntity.setCandidateMessage("Gratulacje! zostałeś zatrudniony, twoje status zostaje zmieniony na bierny");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(recipient));
            notificationDAO.save(notificationEntity);

            CV cv = notification.getCv();
            cv.setVisible(false);
            cvService.saveCV(cv);
            userService.save(loggedInUser);
            userService.save(recipient);

            JobOffer jobOffer = notification.getJobOffer();
            jobOffer.setHiredCount(jobOffer.getHiredCount() + 1);
            if (jobOffer.isFullyStaffed()) {
                jobOffer.setActive(false);
            }

            jobOfferRepository.saveJobOffer(jobOffer);
        } else {
            throw new IllegalStateException("Cannot hire candidate when the status is REJECT.");
        }

    }

    public void deleteNotificationsByCvId(Integer cvId) {
        notificationDAO.deleteByCvId(cvId);
    }

    public boolean hasUserSentCVToJobOffer(User loggedInUser, JobOffer jobOffer) {


        UserEntity userEntity = userMapper.map(loggedInUser);
        JobOfferEntity jobOfferEntity = jobOfferMapper.map(jobOffer);
        return notificationDAO.existsBySenderUserAndJobOffer(userEntity, jobOfferEntity);
    }


    public Page<Notification> findAllNotificationsForPage(Pageable pageable) {
        Page<NotificationEntity> notificationEntities = notificationDAO.findAll(pageable);
        return notificationEntities.map(notificationMapper::map);
    }

    @Transactional
    public boolean canChangeMeetingDate(Notification notification) {
        return notification.getStatus() != Status.WAITING_FOR_INTERVIEW;
    }

    public List<NotificationDTO> findLatestByUser(User loggedInUser) {
        List<Notification> latestNotifications = notificationDAO.findLatestByUser(loggedInUser);
        return latestNotifications.stream()
                .map(notificationMapperDTO::map)
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<NotificationDTO> findByUser(User loggedInUser) {
        List<Notification> notifications = notificationDAO.findByUser(loggedInUser);
        return notifications.stream()
                .map(notificationMapperDTO::map)
                .toList();
    }

    public Notification findById(Integer notificationId) {
        return notificationDAO.findById(notificationId);
    }



}








