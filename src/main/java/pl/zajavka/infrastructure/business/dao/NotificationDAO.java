package pl.zajavka.infrastructure.business.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationDAO {

    Notification findById(Integer notificationId);
    List<Notification> findByUser(User user);

    List<Notification> findByCvId(Integer id);


    List<Notification> findLatestByUser(User loggedInUser);

    void save(NotificationEntity notificationEntity);

    void deleteByCvId(Integer cvId);

    boolean existsBySenderUserAndJobOffer(User user, JobOffer jobOffer);

    Page<Notification> findAll(Pageable pageable);

    List<Notification> findListByJobOfferIdToDelete(Integer id);

    Notification createNotification(JobOffer jobOffer, CV cv, User loggedInUser, User adresat);

    void arrangeInterview(Notification notification, User loggedInUser, User recipient, LocalDateTime proposedDateTime);

    void changeMeetingDate(Notification notification, User loggedInUser, User recipient);

    void acceptMeetingDateTime(Notification notification, User loggedInUser, User recipient);

    void declineCandidate(Notification notification, User loggedInUser, User recipient);

    void hiredCandidate(Notification notification, User loggedInUser, User recipient);
}
