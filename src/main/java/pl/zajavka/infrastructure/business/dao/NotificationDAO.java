package pl.zajavka.infrastructure.business.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.List;

public interface NotificationDAO {

    Notification findById(Integer notificationId);
    List<Notification> findByUser(User user);

    List<NotificationEntity> findByCvId(Integer id);


    List<Notification> findLatestByUser(User loggedInUser);

    void save(NotificationEntity notificationEntity);

    void deleteByCvId(Integer cvId);

    boolean existsBySenderUserAndJobOffer(UserEntity userEntity, JobOfferEntity jobOfferEntity);

    Page<NotificationEntity> findAll(Pageable pageable);

    List<Notification> findListByJobOfferId(Integer id);
}
