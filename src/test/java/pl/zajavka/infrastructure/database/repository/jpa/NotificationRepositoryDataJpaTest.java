package pl.zajavka.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.integration.AbstractJpaIT;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.NotificationFixtures;
import pl.zajavka.util.UserFixtures;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.zajavka.util.UserFixtures.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NotificationRepositoryDataJpaTest extends AbstractJpaIT {

    private NotificationJpaRepository notificationJpaRepository;
    private UserRepository userRepository;

    private CvJpaRepository cvRepository;
    private JobOfferJpaRepository jobOfferRepository;

@Test
    void thatNotificationSavedCorrectly(){
//   given
    var users = List.of(someUserEntity1(),someUserEntity2());
    userRepository.saveAll(users);
    NotificationEntity notification = NotificationFixtures.sampleNotificationEntity1();
    notification.setReceiverUser(users.get(0));
    notification.setSenderUser(users.get(1));

//    when
    NotificationEntity savedNotification = notificationJpaRepository.save(notification);

//    then
    assertThat(savedNotification).isNotNull();
}


    @Test
    void testFindBySenderUser() {
        //   given
        var users = List.of(someUserEntity1(),someUserEntity2());
        userRepository.saveAll(users);
        NotificationEntity notification = NotificationFixtures.sampleNotificationEntity1();
        notification.setReceiverUser(users.get(0));
        notification.setSenderUser(users.get(1));
        notificationJpaRepository.save(notification);

        // when
        List<NotificationEntity> notifications = notificationJpaRepository.findBySenderUser(notification.getSenderUser());

        // then
        assertThat(notifications).containsExactly(notification);
    }

    @Test
    void testFindByReceiverUser() {
        //   given
        var users = List.of(someUserEntity1(),someUserEntity2());
        userRepository.saveAll(users);
        NotificationEntity notification = NotificationFixtures.sampleNotificationEntity1();
        notification.setReceiverUser(users.get(0));
        notification.setSenderUser(users.get(1));
        notificationJpaRepository.save(notification);

        // when
        List<NotificationEntity> notifications = notificationJpaRepository.findByReceiverUser(notification.getReceiverUser());

        // then
        assertThat(notifications).containsExactly(notification);
    }

    @Test
    void testFindByUser() {
        //   given
        var users = List.of(someUserEntity1(),someUserEntity2());
        userRepository.saveAll(users);
        NotificationEntity notification = NotificationFixtures.sampleNotificationEntity1();
        notification.setReceiverUser(users.get(0));
        notification.setSenderUser(users.get(1));
        notificationJpaRepository.save(notification);

        // when
        List<NotificationEntity> notifications = notificationJpaRepository.findByUser(notification.getSenderUser());

        // then
        assertThat(notifications).containsExactly(notification);
    }




    @Test
    void testFindByCvId() {
        //   given
        var users = List.of(someUserEntity1(),someUserEntity2());
        userRepository.saveAll(users);
        CvEntity cv = CvFixtures.someCvEntity1();
        cv.setUser(users.get(0));
        cvRepository.save(cv);
        NotificationEntity notification = NotificationFixtures.sampleNotificationEntity1();
        notification.setReceiverUser(users.get(0));
        notification.setSenderUser(users.get(1));
        notification.setCv(cv);
        notificationJpaRepository.save(notification);

        // when
        List<NotificationEntity> notifications = notificationJpaRepository.findByCvId(notification.getCv().getId());

        // then
        assertThat(notifications).containsExactly(notification);
    }

    @Test
    void testExistsBySenderUserAndJobOffer() {
        //   given
        var users = List.of(someUserEntity1(),someUserEntity2());
        userRepository.saveAll(users);
        NotificationEntity notification = NotificationFixtures.sampleNotificationEntity1();
        notification.setReceiverUser(users.get(0));
        notification.setSenderUser(users.get(1));
        notificationJpaRepository.save(notification);

        // when
        boolean exists = notificationJpaRepository.existsBySenderUserAndJobOffer(notification.getSenderUser(), notification.getJobOffer());

        // then
        assertThat(exists).isTrue();
    }

    @Test
    void testNotificationEntityBuilder(){
//    given
        LocalDateTime notificationDateTime = LocalDateTime.now();
        CvEntity cvEntity = CvFixtures.someCvEntity1();
        JobOfferEntity jobOfferEntity = JobOfferFixtures.someJobOfferEntity1();

//    when

    NotificationEntity notificationEntity = NotificationEntity.builder()
            .candidateMessage("random Message")
            .companyMessage("random Message2")
            .receiverUser(jobOfferEntity.getUser())
            .senderUser(cvEntity.getUser())
            .status(Status.WAITING_FOR_INTERVIEW)
            .cv(cvEntity)
            .jobOffer(jobOfferEntity)
            .dateTime(notificationDateTime)
            .build();
        notificationJpaRepository.save(notificationEntity);

//    then

//        assertThat(notificationEntity.getId()).isEqualTo(1);
        assertThat(notificationEntity.getCandidateMessage()).isEqualTo("random Message");
        assertThat(notificationEntity.getCompanyMessage()).isEqualTo("random Message2");
        assertThat(notificationEntity.getStatus()).isEqualTo(Status.WAITING_FOR_INTERVIEW);
        assertThat(notificationEntity.getCv()).isEqualTo(cvEntity);
        assertThat(notificationEntity.getJobOffer()).isEqualTo(jobOfferEntity);

    }

    @Test
    void testFindLatestByUser() {
        // given
        UserEntity user = UserFixtures.someUserEntity1();
        userRepository.save(user);

        // Create sample notifications for the user
        NotificationEntity notification1 = NotificationFixtures.sampleNotificationEntity1();
        notification1.setSenderUser(user);
        notificationJpaRepository.save(notification1);

        NotificationEntity notification2 = NotificationFixtures.sampleNotificationEntity2();
        notification2.setSenderUser(user);
        notificationJpaRepository.save(notification2);

        // when
        List<NotificationEntity> notifications = notificationJpaRepository.findLatestByUser(user);

        // then
        assertThat(notifications).isNotNull();
        assertThat(notifications).hasSize(2);
        assertThat(notifications.get(0)).isEqualTo(notification2); // Ensure the latest notification is at the top
        assertThat(notifications.get(1)).isEqualTo(notification1);
    }





}
