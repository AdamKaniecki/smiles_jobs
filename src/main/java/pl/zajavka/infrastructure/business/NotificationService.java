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
    private CvService cvService;
    private final NotificationDAO notificationDAO;
    private NotificationMapperDTO notificationMapperDTO;



    @Transactional
    public Notification createNotification(JobOffer jobOffer, CV cv, User loggedInUser, User recipient) {
        return  notificationDAO.createNotification(jobOffer, cv, loggedInUser, recipient);

    }


    public void save(Notification notification) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
        notificationDAO.save(notificationEntity);
    }


    @Transactional
    public void arrangeInterview(Notification notification, User loggedInUser, User recipient, LocalDateTime proposedDateTime) {
        notificationDAO.arrangeInterview(notification, loggedInUser, recipient, proposedDateTime);

    }

    @Transactional
    public void changeMeetingDate(Notification notification, User loggedInUser, User recipient) {
        notificationDAO.changeMeetingDate(notification, loggedInUser, recipient);

    }

    @Transactional
    public void acceptMeetingDateTime(Notification notification, User loggedInUser, User recipient) {
        notificationDAO. acceptMeetingDateTime(notification, loggedInUser, recipient);

    }


    public boolean isNotificationDateTimeAfterNow(Notification notification) {
        LocalDateTime notificationDateTime = notification.getDateTime();
        return notificationDateTime != null && LocalDateTime.now().isAfter(notificationDateTime);
    }

    public void declineCandidate(Notification notification, User loggedInUser, User recipient) {
        notificationDAO.declineCandidate(notification, loggedInUser, recipient);

    }

    @Transactional
    public void hiredCandidate(Notification notification, User loggedInUser, User recipient) {
        notificationDAO.hiredCandidate(notification, loggedInUser, recipient);

    }

    public void deleteNotificationsByCvId(Integer cvId) {
        notificationDAO.deleteByCvId(cvId);
    }

    public boolean hasUserSentCVToJobOffer(User loggedInUser, JobOffer jobOffer) {
        return notificationDAO.existsBySenderUserAndJobOffer(loggedInUser, jobOffer);
    }


    public Page<Notification> findAllNotificationsForPage(Pageable pageable) {
        return notificationDAO.findAll(pageable);
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








