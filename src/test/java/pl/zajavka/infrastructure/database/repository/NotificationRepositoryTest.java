package pl.zajavka.infrastructure.database.repository;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.zajavka.infrastructure.database.entity.CvEntity;
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
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.NotificationFixtures;
import pl.zajavka.util.UserFixtures;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static pl.zajavka.util.UserFixtures.someUserEntity1;
import static pl.zajavka.util.UserFixtures.someUserEntity2;

public class NotificationRepositoryTest extends AbstractIT {

    @Mock
    private NotificationJpaRepository notificationJpaRepository;
    @Mock
    private NotificationMapper notificationMapper;
    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;
    @Mock
    private  CvRepository cvRepository;
//    @Mock
//    private  JobOfferRepository jobOfferRepository;
    @Mock
    private CvMapper cvMapper;
    @Mock
    private JobOfferMapper jobOfferMapper;


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
        assertNotNull(actualNotification.getStatus());
        assertNotNull(actualNotification.getDateTime());
        assertNotNull(actualNotification.getCompanyMessage());
        assertNotNull(actualNotification.getCandidateMessage());
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
        verify(notificationJpaRepository).deleteById(cvId);
        ; // Sprawdzenie, czy metoda deleteById została wywołana z prawidłowym argumentem.
    }


    @Test
    void existsBySenderUserAndJobOffer_ShouldReturnFalse_WhenNotificationDoesNotExist() {
        // Given
        User user = UserFixtures.someUser2();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer1();
        when(notificationJpaRepository.existsBySenderUserAndJobOffer(userMapper.map(user), jobOfferMapper.map(jobOffer))).thenReturn(false);

        // When
        boolean result = notificationRepository.existsBySenderUserAndJobOffer(user, jobOffer);

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

    @Test
    void createNotification_ShouldSaveNotificationAndReturnMappedEntity() {
        // Given
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        CV cv = CvFixtures.someCv1forNotification();
        User loggedInUser = cv.getUser();
        User recipient = jobOffer.getUser();

        // Tworzymy oczekiwaną encję powiadomienia
        NotificationEntity expectedNotificationEntity = NotificationEntity.builder()
                .status(Status.UNDER_REVIEW)
                .candidateMessage("CV sent, await interview offer")
                .companyMessage("I would like to work for you")
                .jobOffer(jobOfferMapper.map(jobOffer))
                .cv(cvMapper.map(cv))
                .senderUser(userMapper.map(loggedInUser))
                .receiverUser(userMapper.map(recipient))
                .build();

        // Zdefiniuj zachowanie mocka dla mappera encji powiadomienia
        when(notificationMapper.map(any(NotificationEntity.class))).thenReturn(NotificationFixtures.sampleNotification1());

        // Zdefiniuj zachowanie mocka dla zapisu encji powiadomienia
        when(notificationJpaRepository.save(any(NotificationEntity.class))).thenReturn(expectedNotificationEntity);

        // When
        Notification createdNotification = notificationRepository.createNotification(jobOffer, cv, loggedInUser, recipient);

        // Then
        assertEquals(NotificationFixtures.sampleNotification1(), createdNotification);

        // Sprawdź, czy metoda save została wywołana raz z odpowiednią encją powiadomienia
        verify(notificationJpaRepository, times(1)).save(any(NotificationEntity.class));

        // Sprawdź, czy metoda map została wywołana raz z oczekiwaną encją powiadomienia
        verify(notificationMapper, times(1)).map(expectedNotificationEntity);

        assertNotNull(createdNotification);
    }




    @Test
    public void arrangeInterview_ShouldSaveNotificationWithMeetingSchedulingStatus() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1fully();
        User loggedInUser = notification.getSender();
        User recipient = notification.getReceiver();
        LocalDateTime proposedDateTime = notification.getDateTime();

        // Ustawienie odpowiedniego statusu dla mockowanej notyfikacji
        notification.setStatus(Status.UNDER_REVIEW);

        // Mapowanie notyfikacji na encję
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setStatus(Status.MEETING_SCHEDULING);
        notificationEntity.setDateTime(proposedDateTime);
        notificationEntity.setCandidateMessage("Accept the meeting schedule or request another");
        notificationEntity.setCompanyMessage("The meeting schedule proposal has been sent");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(recipient));
        when(notificationMapper.map(notification)).thenReturn(notificationEntity);
        // When
        notificationRepository.arrangeInterview(notification, loggedInUser, recipient, proposedDateTime);

        // Then
        // Sprawdzenie, czy metoda map z NotificationMapper została wywołana z odpowiednim argumentem
        verify(notificationMapper).map(notification);

        // Sprawdzenie, czy save zostało wywołane na notificationJpaRepository z odpowiednią encją
        verify(notificationJpaRepository).save(notificationEntity);

        // Sprawdzenie pól encji notificationEntity
        assertEquals(Status.MEETING_SCHEDULING, notificationEntity.getStatus());
        assertEquals(proposedDateTime, notificationEntity.getDateTime());
        assertEquals("Accept the meeting schedule or request another", notificationEntity.getCandidateMessage());
        assertEquals("The meeting schedule proposal has been sent", notificationEntity.getCompanyMessage());
        assertEquals(userMapper.map(loggedInUser), notificationEntity.getSenderUser());
        assertEquals(userMapper.map(recipient), notificationEntity.getReceiverUser());
    }

    @Test
    public void arrangeInterview_ShouldThrowException_WhenStatusIsNotUnderReviewOrMeetingScheduling() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1fully();
        notification.setStatus(Status.WAITING_FOR_INTERVIEW); // Ustawienie statusu na coś innego niż UNDER_REVIEW lub MEETING_SCHEDULING
        User loggedInUser = notification.getSender();
        User recipient = notification.getReceiver();
        LocalDateTime proposedDateTime = notification.getDateTime();

        // Mockowanie zachowania metody map z NotificationMapper
        when(notificationMapper.map(notification)).thenReturn(NotificationFixtures.sampleNotificationEntity1fully());

        // When
        try {
            notificationRepository.arrangeInterview(notification, loggedInUser, recipient, proposedDateTime);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Cannot arrange interview when the status is not Under Review or Meeting Scheduling.", e.getMessage());

//
        }
    }


    @Test
    public void changeMeetingDate_ShouldSaveNotificationWithDateTimeNull() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1fully();
        User loggedInUser = notification.getSender();
        User recipient = notification.getReceiver();


        // Ustawienie odpowiedniego statusu dla mockowanej notyfikacji
        notification.setStatus(Status.MEETING_SCHEDULING);
//        notification.setDateTime(null);

        // Mapowanie notyfikacji na encję
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setStatus(Status.MEETING_SCHEDULING);
        notificationEntity.setDateTime(null);
        notificationEntity.setCandidateMessage("The request to change the schedule has been sent");
        notificationEntity.setCompanyMessage("Please request a change of schedule");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(recipient));
        when(notificationMapper.map(notification)).thenReturn(notificationEntity);

        // When
        notificationRepository.changeMeetingDate(notification, loggedInUser, recipient);

        // Then
        // Sprawdzenie, czy metoda map z NotificationMapper została wywołana z odpowiednim argumentem
        verify(notificationMapper).map(notification);

        // Sprawdzenie, czy save zostało wywołane na notificationJpaRepository z odpowiednią encją
        verify(notificationJpaRepository).save(notificationEntity);

        // Sprawdzenie pól encji notificationEntity
        assertEquals(Status.MEETING_SCHEDULING, notificationEntity.getStatus());
        assertEquals(notificationEntity.getDateTime(),null);
        assertEquals("The request to change the schedule has been sent", notificationEntity.getCandidateMessage());
        assertEquals("Please request a change of schedule", notificationEntity.getCompanyMessage());
        assertEquals(userMapper.map(loggedInUser), notificationEntity.getSenderUser());
        assertEquals(userMapper.map(recipient), notificationEntity.getReceiverUser());
    }


    @Test
    public void changeMeetingDateTime_ShouldThrowException_WhenStatusIsNotMeetingScheduling() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1fully();
        notification.setStatus(Status.WAITING_FOR_INTERVIEW); // Ustawienie statusu na coś innego niż MEETING_SCHEDULING lub MEETING_SCHEDULING
        User loggedInUser = notification.getSender();
        User recipient = notification.getReceiver();

        // Mockowanie zachowania metody map z NotificationMapper
        when(notificationMapper.map(notification)).thenReturn(NotificationFixtures.sampleNotificationEntity1fully());

        // When
        try {
            notificationRepository.changeMeetingDate(notification, loggedInUser, recipient);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Cannot change meeting date when the status is not Meeting Scheduling.", e.getMessage());

        }
    }


    @Test
    public void acceptMeetingDate_ShouldSaveNotification() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1fully();
        User loggedInUser = notification.getSender();
        User recipient = notification.getReceiver();

        // Ustawienie odpowiedniego statusu dla mockowanej notyfikacji
        notification.setStatus(Status.MEETING_SCHEDULING);

        // Mapowanie notyfikacji na encję
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setStatus(Status.WAITING_FOR_INTERVIEW);
        notificationEntity.setCandidateMessage("The meeting schedule has been accepted");
        notificationEntity.setCompanyMessage("The meeting schedule has been accepted");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(recipient));
        when(notificationMapper.map(notification)).thenReturn(notificationEntity);

        // When
        notificationRepository.acceptMeetingDateTime(notification, loggedInUser, recipient);

        // Then
        // Sprawdzenie, czy metoda map z NotificationMapper została wywołana z odpowiednim argumentem
        verify(notificationMapper).map(notification);

        // Sprawdzenie, czy save zostało wywołane na notificationJpaRepository z odpowiednią encją
        verify(notificationJpaRepository).save(notificationEntity);

        // Sprawdzenie pól encji notificationEntity
        assertEquals(Status.WAITING_FOR_INTERVIEW, notificationEntity.getStatus());;
        assertEquals("The meeting schedule has been accepted", notificationEntity.getCandidateMessage());
        assertEquals("The meeting schedule has been accepted", notificationEntity.getCompanyMessage());
        assertEquals(userMapper.map(loggedInUser), notificationEntity.getSenderUser());
        assertEquals(userMapper.map(recipient), notificationEntity.getReceiverUser());
    }


    @Test
    public void acceptMeetingDateTime_ShouldThrowException_WhenStatusIsNotMeetingScheduling() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1fully();
        notification.setStatus(Status.WAITING_FOR_INTERVIEW); // Ustawienie statusu na coś innego niż MEETING_SCHEDULING lub MEETING_SCHEDULING
        User loggedInUser = notification.getSender();
        User recipient = notification.getReceiver();

        // Mockowanie zachowania metody map z NotificationMapper
        when(notificationMapper.map(notification)).thenReturn(NotificationFixtures.sampleNotificationEntity1fully());

        // When
        try {
            notificationRepository.acceptMeetingDateTime(notification, loggedInUser, recipient);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertEquals("Cannot accept meeting date when the status is not Meeting Scheduling.", e.getMessage());

        }


    }

    @Test
    public void declineCandidate_ShouldSaveNotification() {
        // Given
        Notification notification = NotificationFixtures.sampleNotification1fully();
        User loggedInUser = notification.getSender();
        User recipient = notification.getReceiver();

        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setStatus(Status.REJECT);
        notificationEntity.setCandidateMessage("Unfortunately, you have not been hired");
        notificationEntity.setCompanyMessage("The rejection response has been sent");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(recipient));
        when(notificationMapper.map(notification)).thenReturn(notificationEntity);

        // When
        notificationRepository.declineCandidate(notification, loggedInUser, recipient);

        // Then
        // Sprawdzenie, czy metoda map z NotificationMapper została wywołana z odpowiednim argumentem
        verify(notificationMapper).map(notification);

        // Sprawdzenie, czy save zostało wywołane na notificationJpaRepository z odpowiednią encją
        verify(notificationJpaRepository).save(notificationEntity);

        // Sprawdzenie pól encji notificationEntity
        assertEquals(Status.REJECT, notificationEntity.getStatus());;
        assertEquals("Unfortunately, you have not been hired", notificationEntity.getCandidateMessage());
        assertEquals("The rejection response has been sent", notificationEntity.getCompanyMessage());
        assertEquals(userMapper.map(loggedInUser), notificationEntity.getSenderUser());
        assertEquals(userMapper.map(recipient), notificationEntity.getReceiverUser());
    }



//    @Test
//    public void hiredCandidate_ShouldSaveNotification() {
//        // Given
//        Notification notification = NotificationFixtures.sampleNotification1fully();
//        User loggedInUser = notification.getSender();
//        User recipient = notification.getReceiver();
//        notification.setStatus(Status.WAITING_FOR_INTERVIEW);
//
//        NotificationEntity notificationEntity = new NotificationEntity();
//        notificationEntity.setStatus(Status.HIRED);
//        notificationEntity.setCandidateMessage("Congratulations! You have been hired; your status is now changed to invisible");
//        notificationEntity.setCompanyMessage("The positive response has been sent");
//        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
//        notificationEntity.setReceiverUser(userMapper.map(recipient));
//        when(notificationMapper.map(notification)).thenReturn(notificationEntity);
//        CV cv = notification.getCv();
//
//        JobOffer jobOffer = mock(JobOffer.class); // Mockowanie JobOffer
//
//        when(notification.getJobOffer()).thenReturn(jobOffer); // Zwrócenie mockowanego obiektu JobOffer z notyfikacji
//
//        // Ustawienie zachowania dla metod wywoływanych na obiekcie JobOffer
//
//
//
//        // When
//        notificationRepository.hiredCandidate(notification, loggedInUser, recipient);
//
//        // Then
//        // Sprawdzenie, czy metoda map z NotificationMapper została wywołana z odpowiednim argumentem
//        verify(notificationMapper).map(notification);
//
//        // Sprawdzenie, czy save zostało wywołane na notificationJpaRepository z odpowiednią encją
//        verify(notificationJpaRepository).save(notificationEntity);
//
//        // Sprawdzenie pól encji notificationEntity
//        assertEquals(Status.REJECT, notificationEntity.getStatus());;
//        assertEquals("Congratulations! You have been hired; your status is now changed to invisible", notificationEntity.getCandidateMessage());
//        assertEquals("The positive response has been sent", notificationEntity.getCompanyMessage());
//        assertEquals(userMapper.map(loggedInUser), notificationEntity.getSenderUser());
//        assertEquals(userMapper.map(recipient), notificationEntity.getReceiverUser());
//        verify(cvRepository).saveCV(cv);
//    }

}

















