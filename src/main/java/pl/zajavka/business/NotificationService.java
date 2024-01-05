package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.Collection;
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


    public List<Notification> findByUser(User user) {
        UserEntity userEntity = userMapper.map(user);
        List<NotificationEntity> notificationEntities = notificationRepository.findByUser(userEntity);
        return notificationMapper.mapToList(notificationEntities);
    }

//    public Notification createNotification(Notification notification) {
//        NotificationEntity notificationEntity = notificationMapper.map(notification);
//        NotificationEntity newNotification = NotificationEntity.builder()
//                .message("narazie nic")
//                .jobOffer(notificationEntity.getJobOffer())
//                .cv(notificationEntity.getCv())
//                .build();
//        notificationRepository.save(newNotification);
//
//        return notificationMapper.map(newNotification);
//    }

    //    public Notification createNotification(JobOffer jobOffer, CV myCV, User loggedInUser) {
//        System.out.println("twórz wariacie");
//        NotificationEntity newNotificationEntity = NotificationEntity.builder()
//                .message("narazie nic")
//                .jobOffer(jobOffer)  // Uzupełnij to pole w zależności od logiki Twojej aplikacji
//                .cv(myCV)        // Uzupełnij to pole w zależności od logiki Twojej aplikacji
//                .user(loggedInUser)
//                .build();
//        notificationRepository.save(newNotificationEntity);
//
//        return notificationMapper.map(newNotificationEntity);
//    }
    public Notification createNotification(JobOffer jobOffer, CV cv, User loggedInUser) {

//            Notification newNotification = Notification.builder()
//                    .message("narazie nic")
//                    .jobOffer(jobOffer)
//                    .cv(cv)
//                    .user(loggedInUser)
//                    .build();

            NotificationEntity notificationEntity= NotificationEntity.builder()
                    .message("narazie nic")
                    .jobOffer(jobOfferMapper.map(jobOffer))
                    .cv(cvMapper.map(cv))
                    .user(userMapper.map(loggedInUser))
                    .build();

        // Ustaw null, aby uniknąć rekurencji
//        loggedInUser.setNotifications(null);
        notificationRepository.save(notificationEntity);
//            NotificationEntity savedNotificationEntity = notificationMapper.map(newNotification);

            // Przemapuj NotificationEntity na Notification
//            Notification notification = notificationMapper.map(savedNotificationEntity);

            return notificationMapper.map(notificationEntity);
        }



    }


