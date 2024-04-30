package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.infrastructure.business.dao.NotificationDAO;
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

import java.time.LocalDateTime;
import java.util.List;

import static pl.zajavka.infrastructure.database.entity.Status.HIRED;

@Repository
@AllArgsConstructor
public class NotificationRepository implements NotificationDAO {
private final NotificationJpaRepository notificationJpaRepository;
private final NotificationMapper notificationMapper;
private final UserMapper userMapper;
private final JobOfferMapper jobOfferMapper;
private final CvMapper cvMapper;
private final UserRepository userRepository;
private final CvRepository cvRepository;
private final JobOfferRepository jobOfferRepository;


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
    public boolean existsBySenderUserAndJobOffer(User user, JobOffer jobOffer) {
                UserEntity userEntity = userMapper.map(user);
                JobOfferEntity jobOfferEntity = jobOfferMapper.map(jobOffer);
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

    @Transactional
    @Override
    public Notification createNotification(JobOffer jobOffer, CV cv, User loggedInUser, User recipient) {
        NotificationEntity notificationEntity = NotificationEntity.builder()
                .status(Status.UNDER_REVIEW)
                .candidateMessage("CV sent, await interview offer")
                .companyMessage("I would like to work for you")
                .jobOffer(jobOfferMapper.map(jobOffer))
                .cv(cvMapper.map(cv))
                .senderUser(userMapper.map(loggedInUser))
                .receiverUser(userMapper.map(recipient))
                .build();
        notificationJpaRepository.save(notificationEntity);
        return notificationMapper.map(notificationEntity);
    }

    @Transactional
    @Override
    public void arrangeInterview(Notification notification, User loggedInUser, User recipient, LocalDateTime proposedDateTime) {

        Status currentStatus = notification.getStatus();
        if (currentStatus == Status.UNDER_REVIEW || currentStatus == Status.MEETING_SCHEDULING) {
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setStatus(Status.MEETING_SCHEDULING);
            notificationEntity.setDateTime(proposedDateTime);
            notificationEntity.setCandidateMessage("Accept the meeting schedule or request another");
            notificationEntity.setCompanyMessage("The meeting schedule proposal has been sent");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(recipient));
            notificationJpaRepository.save(notificationEntity);
            userRepository.save(userMapper.map(loggedInUser));
            userRepository.save(userMapper.map(recipient));
        } else {
            throw new IllegalStateException("Cannot arrange interview when the status is not Under Review or Meeting Scheduling.");
        }
    }

    @Transactional
    @Override
    public void changeMeetingDate(Notification notification, User loggedInUser, User recipient) {
        if (notification.getStatus() == Status.MEETING_SCHEDULING) {
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setDateTime(null);
            notificationEntity.setCompanyMessage("Please request a change of schedule");
            notificationEntity.setCandidateMessage("The request to change the schedule has been sent");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(recipient));
            notificationJpaRepository.save(notificationEntity);
            userRepository.save(userMapper.map(loggedInUser));
            userRepository.save(userMapper.map(recipient));
        } else {
            throw new IllegalStateException("Cannot change meeting date when the status is not Meeting Scheduling.");
        }

    }

    @Transactional
    @Override
    public void acceptMeetingDateTime(Notification notification, User loggedInUser, User recipient) {
        Status currentStatus = notification.getStatus();
        if (currentStatus == Status.MEETING_SCHEDULING) {
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setStatus(Status.WAITING_FOR_INTERVIEW);
            notificationEntity.setCompanyMessage("The meeting schedule has been accepted");
            notificationEntity.setCandidateMessage("The meeting schedule has been accepted");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(recipient));
            notificationJpaRepository.save(notificationEntity);
            userRepository.save(userMapper.map(loggedInUser));
            userRepository.save(userMapper.map(recipient));
        } else {
            throw new IllegalStateException("Cannot accept meeting date when the status is not Meeting Scheduling.");
        }
    }

    @Override
    public void declineCandidate(Notification notification, User loggedInUser, User recipient) {
        NotificationEntity notificationEntity = notificationMapper.map(notification);
        notificationEntity.setStatus(Status.REJECT);
        notificationEntity.setCompanyMessage("The rejection response has been sent");
        notificationEntity.setCandidateMessage("Unfortunately, you have not been hired");
        notificationEntity.setSenderUser(userMapper.map(loggedInUser));
        notificationEntity.setReceiverUser(userMapper.map(recipient));
        notificationJpaRepository.save(notificationEntity);
        userRepository.save(userMapper.map(loggedInUser));
        userRepository.save(userMapper.map(recipient));
    }

    @Override
    public void hiredCandidate(Notification notification, User loggedInUser, User recipient) {
        Status currentStatus = notification.getStatus();
        if (currentStatus != Status.REJECT) {
            NotificationEntity notificationEntity = notificationMapper.map(notification);
            notificationEntity.setStatus(HIRED);
            notificationEntity.setCompanyMessage("The positive response has been sent");
            notificationEntity.setCandidateMessage("Congratulations! You have been hired; your status is now changed to invisible");
            notificationEntity.setSenderUser(userMapper.map(loggedInUser));
            notificationEntity.setReceiverUser(userMapper.map(recipient));
            notificationJpaRepository.save(notificationEntity);

            CV cv = notification.getCv();
            cv.setVisible(false);
            cvRepository.saveCV(cv);
            userRepository.save(userMapper.map(loggedInUser));
            userRepository.save(userMapper.map(recipient));

            JobOffer jobOffer = notification.getJobOffer();
            jobOffer.setHiredCount(jobOffer.getHiredCount() + 1);
            if (jobOffer.isFullyStaffed()) {
                jobOffer.setActive(false);
            }

            jobOfferRepository.saveJobOffer(jobOffer);
        } else {
            throw new IllegalStateException("Cannot hire candidate when the status is REJECT.");
        }
    }

    @Override
    public List<Notification> findByCvIdToDelete(Integer id) {
        List<NotificationEntity> notifications = notificationJpaRepository.findByCvId(id);
        for (NotificationEntity notification : notifications) {
            notification.setCv(null);
            notification.setCompanyMessage("The Candidate has been deleted his CV");
            notification.setCandidateMessage("Your CV has been deleted");
            notification.setStatus(Status.REJECT);

        }
        return notificationMapper.mapToList(notifications);
    }

    }

