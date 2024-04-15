package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.business.dao.NotificationDAO;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.database.repository.jpa.NotificationJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.NotificationMapper;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class NotificationRepository implements NotificationDAO {
private final NotificationJpaRepository notificationJpaRepository;
private final NotificationMapper notificationMapper;
private final UserMapper userMapper;

    public Notification findById(Integer notificationId) {
        NotificationEntity notificationEntity = notificationJpaRepository.findById(notificationId)
                .orElse(null);
        return notificationMapper.map(notificationEntity);

    }

    public List<Notification> findByUser(User user) {
        UserEntity userEntity = userMapper.map(user);
        List<NotificationEntity> notificationEntities = notificationJpaRepository.findByUser(userEntity);
        return notificationMapper.mapToList(notificationEntities);
    }
    public List<Notification> findLatestByUser(User user) {
        UserEntity userEntity = userMapper.map(user);
        List<NotificationEntity> notificationEntities = notificationJpaRepository.findLatestByUser(userEntity);
        return notificationMapper.mapToList(notificationEntities);
    }

    @Override
    public List<Notification> findByCvId(Integer id) {
        List<NotificationEntity> notifications = notificationJpaRepository.findByCvId(id);
        return notificationMapper.mapToList(notifications);
    }


    @Override
    public void save(NotificationEntity notificationEntity) {
        notificationJpaRepository.save(notificationEntity);
    }

    @Override
    public void deleteByCvId(Integer cvId) {
        notificationJpaRepository.deleteById(cvId);
    }

    @Override
    public boolean existsBySenderUserAndJobOffer(UserEntity userEntity, JobOfferEntity jobOfferEntity) {
      return   notificationJpaRepository.existsBySenderUserAndJobOffer(userEntity,jobOfferEntity);
    }

    @Override
    public Page<Notification> findAll(Pageable pageable) {
        Page<NotificationEntity> notificationEntities =notificationJpaRepository.findAll(pageable);
        return notificationEntities.map(notificationMapper::map);
    }


    @Override
    public List<Notification> findListByJobOfferIdToDelete(Integer id) {
        List<NotificationEntity> notifications = notificationJpaRepository.findByJobOfferId(id);
        for (NotificationEntity notification : notifications) {
            notification.setJobOffer(null);
            notification.setCompanyMessage("Your Job Offer has been deleted");
            notification.setCandidateMessage("The company deleted their Job Offer");
            notification.setStatus(Status.REJECT);

        }
        return notificationMapper.mapToList(notifications);
    }




}
