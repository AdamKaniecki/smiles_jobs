//package pl.zajavka.business;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import pl.zajavka.domain.JobOffer;
//import pl.zajavka.domain.User;
//import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
//import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
//import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
//import pl.zajavka.infrastructure.security.UserEntity;
//import pl.zajavka.infrastructure.security.UserRepository;
//import pl.zajavka.infrastructure.security.mapper.UserMapper;
//
//import java.math.BigDecimal;
//import java.time.OffsetDateTime;
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@AllArgsConstructor
//@Service
//public class JobOfferService {
//    private JobOfferRepository jobOfferRepository;
//    private UserRepository userRepository;
//    private UserMapper userMapper;
//    private JobOfferMapper jobOfferMapper;
//    private UserService userService;
//
//    @Transactional
//    public JobOffer create(JobOffer jobOffer, User user) {
//        System.out.println("twórz psie");
//        log.info("Received job offer: {}", jobOffer);
//        OffsetDateTime currentDateTime = OffsetDateTime.now();
//
//        JobOfferEntity newJobOfferEntity = JobOfferEntity.builder()
////                .id(jobOffer.getId())
//                .companyName(jobOffer.getCompanyName())
//                .position(jobOffer.getPosition())
//                .responsibilities(jobOffer.getResponsibilities())
//                .requiredTechnologies(jobOffer.getRequiredTechnologies())
//                .benefits(jobOffer.getBenefits())
////                .salaryMin(jobOffer.getSalaryMin())
////                .salaryMax(jobOffer.getSalaryMax())
//                .jobOfferDateTime(currentDateTime)
//                .user(userMapper.map(user))
//                .build();
//
////        newJobOfferEntity.setSalaryMin(minSalary);
////        newJobOfferEntity.setSalaryMax(maxSalary);
//
//        jobOfferRepository.saveAndFlush(newJobOfferEntity);
//        return jobOfferMapper.map(newJobOfferEntity);
//
//    }
//
//    private BigDecimal parseSalary(String salaryString) {
//        try {
//            // Spróbuj parsować wartość Salary jako BigDecimal
//            return new BigDecimal(salaryString);
//        } catch (NumberFormatException e) {
//            // Obsłuż błąd parsowania - możesz rzucić wyjątkiem lub zwrócić domyślną wartość
//            throw new IllegalArgumentException("Nieprawidłowa wartość Salary: " + salaryString, e);
//        }
//    }
//
//    public List<JobOffer> findAllJobOffersForPage() {
//        return jobOfferRepository.findAllJobOffersForPage().stream()
//                .map(jobOfferMapper::map)
//                .toList();
//    }
//
//    public List<JobOffer> searchJobOffersByKeywordAndCategory(String keyword, String category) {
//     List<JobOfferEntity> searchJobOfferEntities =  jobOfferRepository.findJobOffersByKeywordAndCategory(keyword, category);
//        return searchJobOfferEntities.stream()
//                .map(jobOfferMapper::map)
//                .toList();
//    }
//
//    private BigDecimal parseSalaryRange(String salaryRange) {
//        try {
//            // Spróbuj sparsować tekst na liczbę
//            return new BigDecimal(salaryRange.trim());
//        } catch (NumberFormatException e) {
//            // Obsłuż sytuację, gdy tekst nie może być sparsowany
//            throw new IllegalArgumentException("Nieprawidłowy format liczby: " + salaryRange);
//        }
//    }
//
//
//    public List<JobOffer> findByUser(User user) {
//        UserEntity userEntity = userRepository.findByUserName(user.getUserName());
//        List<JobOffer> jobOffers =  jobOfferRepository.findByUser(userEntity);
//        return jobOffers.stream()
//                .toList();
//    }
//
//    public Optional<JobOffer> findById(Integer id) {
//        log.debug("szukaj id w serwisie: w ", id);
//        return jobOfferRepository.findById(id).map(jobOfferMapper::map);
//    }
//}
package pl.zajavka.infrastructure.business;


import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.infrastructure.business.dao.JobOfferDAO;
import pl.zajavka.infrastructure.business.dao.NotificationDAO;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.database.repository.jpa.JobOfferJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.NotificationJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class JobOfferService {
    private JobOfferJpaRepository jobOfferRepository;
    private JobOfferMapper jobOfferMapper;
    private NotificationJpaRepository notificationJpaRepository;
    private final JobOfferDAO jobOfferDAO;
    private final NotificationDAO notificationDAO;

    @Transactional
    public JobOffer create(JobOffer jobOffer, User user) {
        return jobOfferDAO.create(jobOffer, user);
    }


    @Transactional
    public JobOffer updateJobOffer(JobOffer jobOffer) {
        return jobOfferDAO.updateJobOffer(jobOffer);
    }

    public void deleteJobOfferAndSetNullInNotifications(Integer jobOfferId) {
        JobOffer jobOffer = jobOfferDAO.findById(jobOfferId);
        notificationDAO.findListByJobOfferId(jobOffer.getId());
        jobOfferDAO.deleteById(jobOfferId);
    }

    public List<JobOffer> searchJobOffersByKeywordAndCategory(String keyword, String category) {
        return jobOfferDAO.searchJobOffersByKeywordAndCategory(keyword, category);

    }


    public List<JobOffer> searchJobOffersBySalary(String category, BigDecimal salary) {
        List<JobOfferEntity> searchJobOfferEntities = jobOfferRepository.findActiveJobOffersBySalaryAndCategory(category, salary);
        List<JobOffer> jobOffers = searchJobOfferEntities.stream()
                .map(jobOfferMapper::map)
                .toList();
        return jobOffers;
    }

    @Transactional
    public JobOffer saveJobOffer(JobOffer jobOffer) {
        return jobOfferDAO.saveJobOffer(jobOffer);
    }

    public JobOffer findById(Integer jobOfferId) {
        return jobOfferDAO.findById(jobOfferId);
    }

    public List<JobOffer> findListByUser(User loggedInUser) {
        return jobOfferDAO.findListByUser(loggedInUser);
    }

    public Optional<JobOffer> findByUser(User loggedInUser) {
        return jobOfferDAO.findByUser(loggedInUser);
    }
}




