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
                .candidateMessage("narazie nic")
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


//    public Optional<Notification> findById(Integer jobOfferId) {
//    }
}


