package pl.zajavka.infrastructure.business;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.controller.dto.mapper.NotificationMapperDTO;
import pl.zajavka.infrastructure.business.dao.NotificationDAO;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.repository.mapper.NotificationMapper;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.NotificationFixtures;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest extends AbstractIT {
    @Mock
    NotificationMapper notificationMapper;
    @Mock
    NotificationDAO notificationDAO;
    @Mock
    NotificationMapperDTO notificationMapperDTO;

    @InjectMocks
    NotificationService notificationService;



    @Test
    void createNotification_Success() {
        // Given
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        CV cv = new CV();
        User loggedInUser = new User();
        User recipient = new User();
        Notification createdNotification = NotificationFixtures.sampleNotification1();

        // Konfiguracja mocka notificationDAO
        when(notificationDAO.createNotification(jobOffer, cv, loggedInUser, recipient)).thenReturn(createdNotification);

        // When
        Notification result = notificationService.createNotification(jobOffer, cv, loggedInUser, recipient);

        // Then
        assertNotNull(result, "Returned notification should not be null");
        assertEquals(createdNotification, result, "Returned notification should match the created one");
        verify(notificationDAO, times(1)).createNotification(jobOffer, cv, loggedInUser, recipient);
    }

    @Test
    void save_Success() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1();
        NotificationEntity notificationEntity = new NotificationEntity();

        // Konfiguracja mocka notificationMapper
        when(notificationMapper.map(notification)).thenReturn(notificationEntity);

        // When
        notificationService.save(notification);

        // Then
        verify(notificationMapper, times(1)).map(notification);
        verify(notificationDAO, times(1)).save(notificationEntity);
    }

    @Test
    void arrangeInterview_Success() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1();
        User loggedInUser = new User();
        User recipient = new User();
        LocalDateTime proposedDateTime = LocalDateTime.now();

        // When
        notificationService.arrangeInterview(notification, loggedInUser, recipient, proposedDateTime);

        // Then
        verify(notificationDAO, times(1)).arrangeInterview(notification, loggedInUser, recipient, proposedDateTime);
    }

    @Test
    void changeMeetingDate_Success() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1();
        User loggedInUser = new User();
        User recipient = new User();

        // When
        notificationService.changeMeetingDate(notification, loggedInUser, recipient);

        // Then
        verify(notificationDAO, times(1)).changeMeetingDate(notification, loggedInUser, recipient);
    }

    @Test
    void acceptMeetingDateTime_Success() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1();
        User loggedInUser = new User();
        User recipient = new User();

        // When
        notificationService.acceptMeetingDateTime(notification, loggedInUser, recipient);

        // Then
        verify(notificationDAO, times(1)).acceptMeetingDateTime(notification, loggedInUser, recipient);
    }

    @Test
    void declineCandidate_Success() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1();
        User loggedInUser = new User();
        User recipient = new User();

        // When
        notificationService.declineCandidate(notification, loggedInUser, recipient);

        // Then
        verify(notificationDAO, times(1)).declineCandidate(notification, loggedInUser, recipient);
    }

    @Test
    void hiredCandidate_Success() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1();
        User loggedInUser = new User();
        User recipient = new User();

        // When
        notificationService.hiredCandidate(notification, loggedInUser, recipient);

        // Then
        verify(notificationDAO, times(1)).hiredCandidate(notification, loggedInUser, recipient);
    }

    @Test
    void hasUserSentCVToJobOffer_True() {
        // Given
        User loggedInUser = new User();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        when(notificationDAO.existsBySenderUserAndJobOffer(loggedInUser, jobOffer)).thenReturn(true);

        // When
        boolean result = notificationService.hasUserSentCVToJobOffer(loggedInUser, jobOffer);

        // Then
        assertTrue(result);
    }

    @Test
    void hasUserSentCVToJobOffer_False() {
        // Given
        User loggedInUser = new User();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        when(notificationDAO.existsBySenderUserAndJobOffer(loggedInUser, jobOffer)).thenReturn(false);

        // When
        boolean result = notificationService.hasUserSentCVToJobOffer(loggedInUser, jobOffer);

        // Then
        assertFalse(result);
    }

    @Test
    void findLatestByUser_ReturnsFiveLatestNotifications() {
        // Given
        User loggedInUser = new User();
        Notification notification1 = NotificationFixtures.sampleNotification1();
        Notification notification2 = NotificationFixtures.sampleNotification2();


        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // Mockowanie zwracanych wartości przez metodę DAO
        when(notificationDAO.findLatestByUser(loggedInUser)).thenReturn(notifications);

        // When
        List<NotificationDTO> result = notificationService.findLatestByUser(loggedInUser);

        // Then
        assertEquals(2, result.size());
        verify(notificationMapperDTO, times(2)).map(any(Notification.class));
    }


    @Test
    void findByUser_ReturnsNotificationsForUser() {
        // Given
        User loggedInUser = new User();
        Notification notification1 = NotificationFixtures.sampleNotification1();
        Notification notification2 = NotificationFixtures.sampleNotification2();
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        // Mockowanie zwracanych wartości przez metodę DAO
        when(notificationDAO.findByUser(loggedInUser)).thenReturn(notifications);

        // When
        List<NotificationDTO> result = notificationService.findByUser(loggedInUser);

        // Then
        assertEquals(2, result.size());
        verify(notificationMapperDTO, times(2)).map(any(Notification.class));
    }

    @Test
    void findByUser_ReturnsEmptyListIfNoNotificationsForUser() {
        // Given
        User loggedInUser = new User();
        List<Notification> notifications = Arrays.asList();

        // Mockowanie zwracanych wartości przez metodę DAO
        when(notificationDAO.findByUser(loggedInUser)).thenReturn(notifications);

        // When
        List<NotificationDTO> result = notificationService.findByUser(loggedInUser);

        // Then
        assertTrue(result.isEmpty());
        verify(notificationMapperDTO, never()).map(any(Notification.class));
    }

    @Test
    void findById_ReturnsNotificationIfExists() {
        // Given
        Integer notificationId = 1;
        Notification notification = NotificationFixtures.sampleNotification1();
        notification.setId(notificationId);

        // Mockowanie zwracanych wartości przez metodę DAO
        when(notificationDAO.findById(notificationId)).thenReturn(notification);

        // When
        Notification result = notificationService.findById(notificationId);

        // Then
        assertEquals(notification, result);
        verify(notificationDAO, times(1)).findById(notificationId);
    }

    @Test
    void findById_ReturnsNullIfNotificationDoesNotExist() {
        // Given
        Integer notificationId = 1;

        // Mockowanie zwracanych wartości przez metodę DAO
        when(notificationDAO.findById(notificationId)).thenReturn(null);

        // When
        Notification result = notificationService.findById(notificationId);

        // Then
        assertNull(result);
        verify(notificationDAO, times(1)).findById(notificationId);
    }
}

