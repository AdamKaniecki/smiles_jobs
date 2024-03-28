//package pl.zajavka.util;
//
//import lombok.experimental.UtilityClass;
//import pl.zajavka.controller.dto.NotificationDTO;
//import pl.zajavka.infrastructure.domain.CV;
//import pl.zajavka.infrastructure.domain.JobOffer;
//import pl.zajavka.infrastructure.domain.Notification;
//import pl.zajavka.infrastructure.domain.User;
//import pl.zajavka.infrastructure.database.entity.NotificationEntity;
//import pl.zajavka.infrastructure.database.entity.Status;
//import pl.zajavka.infrastructure.security.RoleEntity;
//
//import java.time.LocalDateTime;
//import java.time.OffsetDateTime;
//import java.util.List;
//import java.util.Set;
//
//@UtilityClass
//public class NotificationFixtures {
//
//public static NotificationEntity sampleNotificationEntity1(){
//    LocalDateTime notificationDateTime = LocalDateTime.now();
//    return NotificationEntity.builder()
//            .companyMessage("randomMessage1")
//            .candidateMessage("randomMessage2")
//            .status(Status.WAITING_FOR_INTERVIEW)
//            .dateTime(notificationDateTime)
//            .build();
//
//}
//
//    public static NotificationEntity sampleNotificationEntity2(){
//        LocalDateTime notificationDateTime = LocalDateTime.now();
//        return NotificationEntity.builder()
//                .companyMessage("randomMessage3")
//                .candidateMessage("randomMessage4")
//                .status(Status.WAITING_FOR_INTERVIEW)
//                .dateTime(notificationDateTime)
//                .build();
//
//    }
//
//    public static Notification sampleNotification1(){
//        LocalDateTime notificationDateTime = LocalDateTime.now();
//        return Notification.builder()
//                .companyMessage("randomMessage1")
//                .candidateMessage("randomMessage2")
//                .status(Status.WAITING_FOR_INTERVIEW)
//                .dateTime(notificationDateTime)
//                .build();
//
//    }
//
//    public static List<NotificationDTO> sampleNotificationDTOList(){
//        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_CANDIDATE").build();
//        User user = User.builder()
//                .id(1)
//                .userName("adam112")
//                .roles(Set.of(candidateRole))
//                .password("adam1112")
//                .email("adam21113@poczta.onet.pl")
//                .active(true)
//                .build();
//
//        RoleEntity candidateRole2 = RoleEntity.builder().id(2).role("ROLE_COMPANY").build();
//        User user2 = User.builder()
//                .id(2)
//                .userName("adam122222")
//                .roles(Set.of(candidateRole2))
//                .password("adam122212")
//                .email("adam2112223@poczta.onet.pl")
//                .active(true)
//                .build();
//
//        LocalDateTime notificationDateTime = LocalDateTime.now();
//        LocalDateTime notification2DateTime = LocalDateTime.now();
//
//        CV cv = CV.builder()
//                .name("John")
//                .surname("Doe")
//                .dateOfBirth("1990-01-01")
//                .sex("Male")
//                .maritalStatus("Single")
//                .phoneNumber("123456789")
//                .contactEmail("john@example.com")
//                .workExperience("5 years")
//                .education("Master's Degree")
//                .skills("Java, Python")
//                .tools("IntelliJ IDEA, Git")
//                .yearsOfExperience(5)
//                .language("English")
//                .languageLevel("Fluent")
//                .hobby("Reading, Travelling")
//                .build();
//
//        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
//        JobOffer jobOffer = JobOffer.builder()
//                .id(1)
//                .companyName("company1")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
//                .build();
//
//
//       NotificationDTO notificationDTO1 = NotificationDTO.builder()
//                .companyMessage("randomMessage1")
//                .candidateMessage("randomMessage2")
//                .status(Status.WAITING_FOR_INTERVIEW)
//                .dateTime(notificationDateTime)
//                .sender(user)
//                .receiver(user2)
//                .cv(cv)
//                .jobOffer(jobOffer)
//                .build();
//
//
//        List<NotificationDTO> notificationDTOs = List.of(notificationDTO1);
//        return notificationDTOs;
//    }
//
//    public static List<Notification> sampleNotificationList(){
//        LocalDateTime notificationDateTime = LocalDateTime.now();
//        LocalDateTime notification2DateTime = LocalDateTime.now();
//        Notification notification1 = Notification.builder()
//                .companyMessage("randomMessage1")
//                .candidateMessage("randomMessage2")
//                .status(Status.WAITING_FOR_INTERVIEW)
//                .dateTime(notificationDateTime)
//                .build();
//
//        Notification notification2 = Notification.builder()
//                .companyMessage("randomMessage1")
//                .candidateMessage("randomMessage2")
//                .status(Status.WAITING_FOR_INTERVIEW)
//                .dateTime(notification2DateTime)
//                .build();
//        List<Notification> notifications = List.of(notification1,notification2);
//        return notifications;
//    }
//}
