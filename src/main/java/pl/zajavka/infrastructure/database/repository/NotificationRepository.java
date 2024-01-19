package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.zajavka.domain.Notification;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,Integer> {



    List<NotificationEntity> findBySenderUser(UserEntity user);
    List<NotificationEntity> findByReceiverUser(UserEntity user);
    @Query("SELECT n FROM NotificationEntity n WHERE n.senderUser = :user OR n.receiverUser = :user")
    List<NotificationEntity> findByUser(UserEntity user);

    void deleteByCvId(Integer cvId);


    List<NotificationEntity> findByCvId(Integer id);
}
