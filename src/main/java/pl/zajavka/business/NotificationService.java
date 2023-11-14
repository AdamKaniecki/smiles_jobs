//package pl.zajavka.business;
//
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.webjars.NotFoundException;
//import pl.zajavka.domain.User;
//import pl.zajavka.infrastructure.database.entity.NotificationEntity;
//import pl.zajavka.infrastructure.database.repository.NotificationRepository;
//import pl.zajavka.infrastructure.database.repository.mapper.NotificationMapper;
//import pl.zajavka.infrastructure.security.UserEntity;
//
//import java.util.List;
//
//@AllArgsConstructor
//@Service
//public class NotificationService {
//
//    private final NotificationRepository notificationRepository;
//    private NotificationMapper notificationMapper;
//    // Metoda do pobierania powiadomienia po identyfikatorze
//
//    public NotificationEntity getNotificationById(Integer notificationId) {
//        return notificationRepository.findById(notificationId)
//                .orElseThrow(() -> new NotFoundException("Notification not found with id: " + notificationId));
//    }
//
//    // Metoda do pobierania wszystkich powiadomień
//    public List<NotificationEntity> getAllNotifications() {
//        return notificationRepository.findAll();
//    }
//
//    public List<NotificationEntity> getNotificationsByUser(UserEntity userEntity) {
//        // Pobierz listę powiadomień dla danego użytkownika
//        return notificationRepository.findByUsers(userEntity);
//    }
//
//}
