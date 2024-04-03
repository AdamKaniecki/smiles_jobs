package pl.zajavka.infrastructure.business.dao;

import org.springframework.data.domain.Pageable;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.List;

public interface NotificationDAO {

    Notification findById(Integer notificationId);
    List<Notification> findByUser(User user);


}