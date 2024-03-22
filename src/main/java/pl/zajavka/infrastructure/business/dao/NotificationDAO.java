package pl.zajavka.infrastructure.business.dao;

import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;

import java.util.List;

public interface NotificationDAO {

    Notification findById(Integer notificationId);
    List<Notification> findByUser(User user);

}
