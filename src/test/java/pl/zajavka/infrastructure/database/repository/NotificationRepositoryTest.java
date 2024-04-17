package pl.zajavka.infrastructure.database.repository;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.database.repository.jpa.NotificationJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.NotificationMapper;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.NotificationFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class NotificationRepositoryTest extends AbstractIT {

    @Mock
    private NotificationJpaRepository notificationJpaRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @Mock
    private  UserMapper userMapper;

    @InjectMocks
    private NotificationRepository notificationRepository;

    @Test
    void findById_ShouldReturnNotification_WhenNotificationExists() {
        // Given
        NotificationEntity notificationEntity = NotificationFixtures.sampleNotificationEntity1();
        Notification expectedNotification = NotificationFixtures.sampleNotification1();
        when(notificationJpaRepository.findById(anyInt())).thenReturn(Optional.of(notificationEntity));
        when(notificationMapper.map(notificationEntity)).thenReturn(expectedNotification);

        // When
        Notification actualNotification = notificationRepository.findById(1);


        // Then
        assertNotNull(actualNotification);
        assertNotNull( actualNotification.getStatus());
        assertNotNull( actualNotification.getDateTime());
        assertNotNull( actualNotification.getCompanyMessage());
        assertNotNull( actualNotification.getCandidateMessage());
    }

    @Test
    void findById_ShouldReturnNull_WhenNotificationDoesNotExist() {
        // Given
        when(notificationJpaRepository.findById(anyInt())).thenReturn(Optional.empty());

        // When
        Notification actualNotification = notificationRepository.findById(1);

        // Then
        assertEquals(null, actualNotification);
    }

    @Test
    void findByUser_ShouldReturnNotifications_WhenUserExists() {
        // Given
        User user = UserFixtures.someUser1();
        UserEntity userEntity = UserFixtures.someUserEntity1();
        when(userMapper.map(user)).thenReturn(userEntity);
        List<NotificationEntity> notificationEntities = NotificationFixtures.sampleNotificationEntityList();
        when(notificationJpaRepository.findByUser(userEntity)).thenReturn(notificationEntities);
        List<Notification> expectedNotifications = NotificationFixtures.sampleNotificationList();
        when(notificationMapper.mapToList(notificationEntities)).thenReturn(expectedNotifications);

        // When
        List<Notification> actualNotifications = notificationRepository.findByUser(user);

        // Then
        assertEquals(expectedNotifications.size(), actualNotifications.size());
        assertTrue(actualNotifications.containsAll(expectedNotifications));
        assertEquals(expectedNotifications, actualNotifications); // Sprawdzenie, czy listy powiadomień są równe.
        assertEquals(expectedNotifications.get(0), actualNotifications.get(0)); // Sprawdzenie, czy pierwsze powiadomienie jest takie samo.
        assertEquals(expectedNotifications.get(1), actualNotifications.get(1)); // Sprawdzenie, czy drugie powiadomienie jest takie samo.
        assertEquals(expectedNotifications.get(0).getDateTime(), actualNotifications.get(0).getDateTime()); // Sprawdzenie, czy daty i czasy pierwszych powiadomień są takie same.
        assertEquals(expectedNotifications.get(1).getDateTime(), actualNotifications.get(1).getDateTime()); // Sprawdzenie, czy daty i czasy drugich powiadomień są takie same.
        assertEquals(expectedNotifications.get(0).getStatus(), actualNotifications.get(0).getStatus()); // Sprawdzenie, czy statusy pierwszych powiadomień są takie same.
        assertEquals(expectedNotifications.get(1).getStatus(), actualNotifications.get(1).getStatus()); // Sprawdzenie, czy statusy drugich powiadomień są takie same.
        assertEquals(expectedNotifications.get(0).getCompanyMessage(), actualNotifications.get(0).getCompanyMessage()); // Sprawdzenie, czy wiadomości od firmy pierwszych powiadomień są takie same.
        assertEquals(expectedNotifications.get(1).getCompanyMessage(), actualNotifications.get(1).getCompanyMessage()); // Sprawdzenie, czy wiadomości od firmy drugich powiadomień są takie same.
        assertEquals(expectedNotifications.get(0).getCandidateMessage(), actualNotifications.get(0).getCandidateMessage()); // Sprawdzenie, czy wiadomości od kandydatów pierwszych powiadomień są takie same.
        assertEquals(expectedNotifications.get(1).getCandidateMessage(), actualNotifications.get(1).getCandidateMessage()); // Sprawdzenie, czy wiadomości od kandydatów drugich powiadomień są takie same.

    }

    @Test
    void findByUser_ShouldReturnEmptyList_WhenUserDoesNotExist() {
        // Given
        User user = new User();
        UserEntity userEntity = new UserEntity();
        when(userMapper.map(user)).thenReturn(userEntity);
        when(notificationJpaRepository.findByUser(userEntity)).thenReturn(List.of());

        // When
        List<Notification> actualNotifications = notificationRepository.findByUser(user);

        // Then
        assertTrue(actualNotifications.isEmpty()); // Sprawdzenie, czy zwrócona lista jest pusta.
    }

    @Test
    void findLatestByUser_ShouldReturnLatestNotifications_WhenUserExists() {
        // Given
        User user = UserFixtures.someUser1();
        UserEntity userEntity = UserFixtures.someUserEntity1();
        when(userMapper.map(user)).thenReturn(userEntity);
        List<NotificationEntity> notificationEntities = NotificationFixtures.sampleNotificationEntityList();
        when(notificationJpaRepository.findLatestByUser(userEntity)).thenReturn(notificationEntities);
        List<Notification> expectedNotifications = NotificationFixtures.sampleNotificationList();
        when(notificationMapper.mapToList(notificationEntities)).thenReturn(expectedNotifications);

        // When
        List<Notification> actualNotifications = notificationRepository.findLatestByUser(user);

        // Then
        assertEquals(expectedNotifications, actualNotifications); // Sprawdzenie, czy listy powiadomień są równe.
    }

    @Test
    void findLatestByUser_ShouldReturnEmptyList_WhenUserDoesNotExist() {
        // Given
        User user = new User();
        UserEntity userEntity = new UserEntity();
        when(userMapper.map(user)).thenReturn(userEntity);
        when(notificationJpaRepository.findLatestByUser(userEntity)).thenReturn(List.of());

        // When
        List<Notification> actualNotifications = notificationRepository.findLatestByUser(user);

        // Then
        assertEquals(0, actualNotifications.size()); // Sprawdzenie, czy zwrócona lista jest pusta.
    }

    @Test
    void findByCvId_ShouldReturnNotificationEntities_WhenCvIdExists() {
        // Given
        Integer cvId = 1;
        List<NotificationEntity> expectedNotifications = NotificationFixtures.sampleNotificationEntityList();
        List<Notification> notifications = NotificationFixtures.sampleNotificationList();
        when(notificationJpaRepository.findByCvId(cvId)).thenReturn(expectedNotifications);
        when(notificationMapper.mapToList(expectedNotifications)).thenReturn(notifications);
        // When
        List<Notification> actualNotifications = notificationRepository.findByCvId(cvId);

        // Then
        assertEquals(notifications, actualNotifications); // Sprawdzenie, czy zwrócone powiadomienia są równe.
    }

    @Test
    void findByCvId_ShouldReturnEmptyList_WhenCvIdDoesNotExist() {
        // Given
        Integer cvId = null; // Założenie, że nie istnieje żadne powiadomienie o takim ID.
        List<NotificationEntity> expectedNotifications = NotificationFixtures.sampleNotificationEntityList();
        when(notificationJpaRepository.findByCvId(cvId)).thenReturn(expectedNotifications);
        when(notificationMapper.mapToList(expectedNotifications)).thenReturn(List.of());

        // When
        List<Notification> actualNotifications = notificationRepository.findByCvId(cvId);

        // Then
        assertEquals(0, actualNotifications.size()); // Sprawdzenie, czy zwrócona lista jest pusta.
    }

    @Test
    void deleteByCvId_ShouldCallDeleteByIdMethodOfJpaRepository() {
        // Given
        Integer cvId = 1;

        // When
        notificationRepository.deleteByCvId(cvId);

        // Then
        verify(notificationJpaRepository).deleteById(cvId);; // Sprawdzenie, czy metoda deleteById została wywołana z prawidłowym argumentem.
    }

    @Test
    void existsBySenderUserAndJobOffer_ShouldReturnTrue_WhenNotificationExists() {
        // Given
        UserEntity userEntity = UserFixtures.someUserEntity2();
        JobOfferEntity jobOfferEntity = JobOfferFixtures.someJobOfferEntity1();
        when(notificationJpaRepository.existsBySenderUserAndJobOffer(userEntity, jobOfferEntity)).thenReturn(true);

        // When
        boolean result = notificationRepository.existsBySenderUserAndJobOffer(userEntity, jobOfferEntity);

        // Then
        assertEquals(true, result);
    }

    @Test
    void existsBySenderUserAndJobOffer_ShouldReturnFalse_WhenNotificationDoesNotExist() {
        // Given
        UserEntity userEntity = UserFixtures.someUserEntity2();
        JobOfferEntity jobOfferEntity = JobOfferFixtures.someJobOfferEntity1();
        when(notificationJpaRepository.existsBySenderUserAndJobOffer(userEntity, jobOfferEntity)).thenReturn(false);

        // When
        boolean result = notificationRepository.existsBySenderUserAndJobOffer(userEntity, jobOfferEntity);

        // Then
        assertEquals(false, result);
    }

    @Test
    void save_ShouldCallSaveMethodOfJpaRepository() {
        // Given
        NotificationEntity notificationEntity = NotificationFixtures.sampleNotificationEntity1();

        // When
        notificationRepository.save(notificationEntity);

        // Then
        verify(notificationJpaRepository).save(notificationEntity);
    }

    @Test
    void deleteByCvId_ShouldCallDeleteById() {
        // Given
        Integer cvId = 1;

        // When
        notificationRepository.deleteByCvId(cvId);

        // Then
        verify(notificationJpaRepository).deleteById(cvId);
    }

    @Test
    void testFindListByJobOfferIdToDelete() {
        // Given
        Integer jobOfferId = 1;
        List<NotificationEntity> expectedNotifications = NotificationFixtures.sampleNotificationEntityList();
        // Zaktualizuj dane oczekiwanych powiadomień tak, aby zawierały poprawną wiadomość od firmy
        // Zaktualizuj dane oczekiwanych powiadomień tak, aby zawierały poprawną wiadomość od firmy


        List<Notification> notifications = NotificationFixtures.sampleNotificationList();
        for (Notification notification : notifications) {
            notification.setCompanyMessage("Your Job Offer has been deleted");
            notification.setCandidateMessage("The company deleted their Job Offer");
            notification.setStatus(Status.REJECT);
        }

        when(notificationJpaRepository.findByJobOfferId(jobOfferId)).thenReturn(expectedNotifications);
        when(notificationMapper.mapToList(expectedNotifications)).thenReturn(notifications);
        // When
        List<Notification> actualNotifications = notificationRepository.findListByJobOfferIdToDelete(jobOfferId);


        // Then
        assertNotNull(actualNotifications); // Sprawdź, czy lista nie jest nullem
        assertEquals(notifications.size(), actualNotifications.size()); // Sprawdź, czy liczba powiadomień jest taka sama jak oczekiwana

        // Sprawdź, czy każde powiadomienie na liście ma ustawione pola zgodnie z oczekiwaniami
        for (Notification notification : actualNotifications) {
            assertNull(notification.getJobOffer()); // Upewnij się, że pole jobOffer jest nullem
            assertEquals("Your Job Offer has been deleted", notification.getCompanyMessage()); // Sprawdź, czy wiadomość dla firmy jest poprawna
            assertEquals("The company deleted their Job Offer", notification.getCandidateMessage()); // Sprawdź, czy wiadomość dla kandydata jest poprawna
            assertEquals(Status.REJECT, notification.getStatus()); // Sprawdź, czy status jest poprawny
        }

        // Sprawdź, czy metoda findByJobOfferId została wywołana dokładnie raz z odpowiednim argumentem
        verify(notificationJpaRepository, times(1)).findByJobOfferId(jobOfferId);
        // Sprawdź, czy metoda mapToList została wywołana dokładnie raz z oczekiwanymi argumentami
        verify(notificationMapper, times(1)).mapToList(expectedNotifications);
    }
    }





