package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.infrastructure.database.entity.*;
import pl.zajavka.infrastructure.domain.*;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@UtilityClass
public class NotificationFixtures {

    public static NotificationEntity sampleNotificationEntity1() {

        LocalDateTime notificationDateTime = LocalDateTime.now();


        return NotificationEntity.builder()
                .companyMessage("randomMessage1")
                .candidateMessage("randomMessage2")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationDateTime)
                .build();

    }

    public static NotificationEntity sampleNotificationEntity2() {
        LocalDateTime notificationDateTime = LocalDateTime.now();
        return NotificationEntity.builder()
                .companyMessage("randomMessage3")
                .candidateMessage("randomMessage4")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationDateTime)
                .build();

    }

    public static Notification sampleNotification1() {
        LocalDateTime notificationDateTime = LocalDateTime.now();
        return Notification.builder()
                .companyMessage("randomMessage1")
                .candidateMessage("randomMessage2")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationDateTime)
                .build();

    }

    public static Notification sampleNotification2() {
        LocalDateTime notificationDateTime = LocalDateTime.now();
        return Notification.builder()
                .companyMessage("randomMessage3")
                .candidateMessage("randomMessage4")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationDateTime)
                .build();

    }

    public static List<NotificationDTO> sampleNotificationDTOList() {
        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_CANDIDATE").build();
        User user = User.builder()
                .id(1)
                .userName("adam112")
                .roles(Set.of(candidateRole))
                .password("adam1112")
                .email("adam21113@poczta.onet.pl")
                .active(true)
                .build();

        RoleEntity candidateRole2 = RoleEntity.builder().id(2).role("ROLE_COMPANY").build();
        User user2 = User.builder()
                .id(2)
                .userName("adam122222")
                .roles(Set.of(candidateRole2))
                .password("adam122212")
                .email("adam2112223@poczta.onet.pl")
                .active(true)
                .build();

        LocalDateTime notificationDateTime = LocalDateTime.now();
        LocalDateTime notification2DateTime = LocalDateTime.now();

        CV cv = CV.builder()
                .name("John")
                .surname("Doe")
                .dateOfBirth("1990-01-01")
                .sex("Male")
                .maritalStatus("Single")
                .phoneNumber("123456789")
                .contactEmail("john@example.com")
                .aboutMe("coś tam")
                .followPosition("junior")
                .workExperience("5 years")
                .education("Master's Degree")
                .skillsAndTools("IntelliJ IDEA, Git")
                .programmingLanguage("Java, Python")
                .certificatesOfCourses("zajavka")
                .language("English")
                .languageLevel("A2")
                .visible(true)
                .socialMediaProfil("linked")
                .projects("smiles")
                .hobby("Reading, Travelling")
//                .address(address)
                .build();

        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
        JobOffer jobOffer = JobOffer.builder()
                .id(1)
                .companyName("company1")
                .position("junior java developer")
                .responsibilities("utrzymywanie aplikacji")
                .requiredTechnologies("spring")
                .benefits("owocowe czwartki")
                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
                .build();


        NotificationDTO notificationDTO1 = NotificationDTO.builder()
                .companyMessage("randomMessage1")
                .candidateMessage("randomMessage2")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationDateTime)
                .sender(user)
                .receiver(user2)
                .cv(cv)
                .jobOffer(jobOffer)
                .build();


        List<NotificationDTO> notificationDTOs = List.of(notificationDTO1);
        return notificationDTOs;
    }

    public static List<Notification> sampleNotificationList() {
        LocalDateTime notificationDateTime = LocalDateTime.now();
        LocalDateTime notification2DateTime = LocalDateTime.now();
        Notification notification1 = Notification.builder()
                .companyMessage("randomMessage1")
                .candidateMessage("randomMessage2")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationDateTime)
                .build();

        Notification notification2 = Notification.builder()
                .companyMessage("randomMessage1")
                .candidateMessage("randomMessage2")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notification2DateTime)
                .build();
        List<Notification> notifications = List.of(notification1, notification2);
        return notifications;
    }

    public static List<NotificationEntity> sampleNotificationEntityList() {
        LocalDateTime notificationDateTime = LocalDateTime.now();
        LocalDateTime notification2DateTime = LocalDateTime.now();
        NotificationEntity notification1 = NotificationEntity.builder()
                .companyMessage("randomMessage1")
                .candidateMessage("randomMessage2")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notificationDateTime)
                .build();

        NotificationEntity notification2 = NotificationEntity.builder()
                .companyMessage("randomMessage1")
                .candidateMessage("randomMessage2")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(notification2DateTime)
                .build();

        return List.of(notification1, notification2);
    }


    public static Notification sampleNotification1fully() {

        LocalDateTime notificationDateTime = LocalDateTime.now();
        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_CANDIDATE").build();

        Address address = Address.builder()
                .city("Sample City")
                .country("Sample Country")
                .streetAndNumber("Sample Street 125")
                .postalCode("09-500")
                .build();

        User user = User.builder()
                .userName("sample_user")
                .email("sample@example.com")
                .password("password123")
                .active(true)
                .roles(Set.of(candidateRole))
                .build();

        CV cv = CV.builder()
                .name("John")
                .surname("Doe")
                .dateOfBirth("1990-01-01")
                .sex("Male")
                .maritalStatus("Single")
                .phoneNumber("123456789")
                .contactEmail("john@example.com")
                .workExperience("5 years")
                .education("Master's Degree")
                .socialMediaProfil("linked")
                .projects("smiles")
                .aboutMe("coś tam")
                .certificatesOfCourses("zajavka")
                .programmingLanguage("Java, Python")
                .skillsAndTools("IntelliJ IDEA, Git")
                .language("English")
                .languageLevel("A2")
                .hobby("Reading, Travelling")
                .followPosition("junior")
                .visible(true)
                .user(user)
                .address(address)
                .build();

        RoleEntity companyRole = RoleEntity.builder().id(2).role("ROLE_COMPANY").build();
        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
        User user2 = User.builder()
                .userName("adam112")
                .roles(Set.of(companyRole))
                .password("adam1112")
                .email("adam21113@poczta.onet.pl")
                .active(true)
                .build();

        JobOffer jobOffer = JobOffer.builder()
                .id(1)
                .companyName("januszex")
                .position("junior java developer")
                .responsibilities("support")
                .requiredTechnologies("spring")
                .experience("two years")
                .jobLocation("remote")
                .typeOfContract("b2b")
                .typeOfWork("FULL-TIME JOB")
                .salaryMin(new BigDecimal("4000")) // Przykładowe wynagrodzenie minimalne
                .salaryMax(new BigDecimal("6000")) // Przykładowe wynagrodzenie maksymalne
                .requiredLanguage("English") // Wymagany język
                .requiredLanguageLevel("B2") // Poziom wymaganego języka
                .benefits("fruits") // Korzyści oferowane przez pracodawcę
                .jobDescription("example about us") // Opis stanowiska pracy
                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
                .active(true) // Czy oferta pracy jest aktywna
                .neededStaff(5) // Ilość potrzebnych pracowników
                .hiredCount(0) // Liczba zatrudnionych pracowników
                .user(user2)
                .build();


        return Notification.builder()
                .id(1)
                .companyMessage("I would like to work for you")
                .candidateMessage("CV sent, await interview offer")
                .sender(user)
                .receiver(user2)
                .status(Status.UNDER_REVIEW)
                .cv(cv)
                .jobOffer(jobOffer)
                .dateTime(notificationDateTime)
                .build();

    }

    public static NotificationEntity sampleNotificationEntity1fully() {

        LocalDateTime notificationDateTime = LocalDateTime.now();
        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_CANDIDATE").build();

        AddressEntity address = AddressEntity.builder()
                .city("Sample City")
                .country("Sample Country")
                .streetAndNumber("Sample Street 125")
                .postalCode("09-500")
                .build();

        UserEntity user = UserEntity.builder()
                .userName("sample_user")
                .email("sample@example.com")
                .password("password123")
                .active(true)
                .roles(Set.of(candidateRole))
                .build();

        CvEntity cv = CvEntity.builder()
                .name("John")
                .surname("Doe")
                .dateOfBirth("1990-01-01")
                .sex("Male")
                .maritalStatus("Single")
                .phoneNumber("123456789")
                .contactEmail("john@example.com")
                .workExperience("5 years")
                .education("Master's Degree")
                .socialMediaProfil("linked")
                .projects("smiles")
                .aboutMe("coś tam")
                .certificatesOfCourses("zajavka")
                .programmingLanguage("Java, Python")
                .skillsAndTools("IntelliJ IDEA, Git")
                .language("English")
                .languageLevel("A2")
                .hobby("Reading, Travelling")
                .followPosition("junior")
                .visible(true)
                .user(user)
                .address(address)
                .build();

        RoleEntity companyRole = RoleEntity.builder().id(2).role("ROLE_COMPANY").build();
        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
        UserEntity user2 = UserEntity.builder()
                .userName("adam112")
                .roles(Set.of(companyRole))
                .password("adam1112")
                .email("adam21113@poczta.onet.pl")
                .active(true)
                .build();

        JobOfferEntity jobOffer = JobOfferEntity.builder()
                .id(1)
                .companyName("januszex")
                .position("junior java developer")
                .responsibilities("support")
                .requiredTechnologies("spring")
                .experience("two years")
                .jobLocation("remote")
                .typeOfContract("b2b")
                .typeOfWork("FULL-TIME JOB")
                .salaryMin(new BigDecimal("4000")) // Przykładowe wynagrodzenie minimalne
                .salaryMax(new BigDecimal("6000")) // Przykładowe wynagrodzenie maksymalne
                .requiredLanguage("English") // Wymagany język
                .requiredLanguageLevel("B2") // Poziom wymaganego języka
                .benefits("fruits") // Korzyści oferowane przez pracodawcę
                .jobDescription("example about us") // Opis stanowiska pracy
                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
                .active(true) // Czy oferta pracy jest aktywna
                .neededStaff(5) // Ilość potrzebnych pracowników
                .hiredCount(0) // Liczba zatrudnionych pracowników
                .user(user2)
                .build();



        return NotificationEntity.builder()
                .id(1)
                .companyMessage("I would like to work for you")
                .candidateMessage("CV sent, await interview offer")
                .senderUser(user)
                .receiverUser(user2)
                .status(Status.UNDER_REVIEW)
                .cv(cv)
                .jobOffer(jobOffer)
                .dateTime(notificationDateTime)
                .build();

    }

    public static Notification sampleNotification1forArrange() {
        LocalDateTime notificationDateTime = LocalDateTime.now();
        return Notification.builder()
                .companyMessage("I would like to work for you")
                .candidateMessage("CV sent, await interview offer")
                .status(Status.UNDER_REVIEW)
                .dateTime(notificationDateTime)
                .build();


    }

    public static NotificationEntity sampleNotificationEntity1forArrange() {
        LocalDateTime notificationDateTime = LocalDateTime.now();
        return NotificationEntity.builder()
                .companyMessage("I would like to work for you")
                .candidateMessage("CV sent, await interview offer")
                .status(Status.UNDER_REVIEW)
                .dateTime(notificationDateTime)
                .build();


    }
}
