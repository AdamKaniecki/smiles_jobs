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
//
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.infrastructure.business.dao.JobOfferDAO;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.database.repository.jpa.JobOfferJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.NotificationJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class JobOfferService {
    private JobOfferJpaRepository jobOfferRepository;
    private UserMapper userMapper;
    private JobOfferMapper jobOfferMapper;
    private NotificationJpaRepository notificationJpaRepository;
    private final JobOfferDAO jobOfferDAO;

    @Transactional
    public JobOffer create(JobOffer jobOffer, User user) {

        OffsetDateTime currentDateTime = OffsetDateTime.now();

        JobOfferEntity newJobOfferEntity = JobOfferEntity.builder()
                .companyName(jobOffer.getCompanyName())
                .position(jobOffer.getPosition())
                .responsibilities(jobOffer.getResponsibilities())
                .requiredTechnologies(jobOffer.getRequiredTechnologies())
                .experience(jobOffer.getExperience())
                .jobLocation(jobOffer.getJobLocation())
                .typeOfContract(jobOffer.getTypeOfContract())
                .typeOfWork(jobOffer.getTypeOfWork())
                .salaryMin(jobOffer.getSalaryMin())
                .salaryMax(jobOffer.getSalaryMax())
                .requiredLanguage(jobOffer.getRequiredLanguage())
                .requiredLanguageLevel(jobOffer.getRequiredLanguageLevel())
                .benefits(jobOffer.getBenefits())
                .jobDescription(jobOffer.getJobDescription())
                .jobOfferDateTime(currentDateTime)
                .neededStaff(jobOffer.getNeededStaff())
                .hiredCount(0)
                .active(true)
                .user(userMapper.map(user))
                .build();


        jobOfferRepository.saveAndFlush(newJobOfferEntity);
        return jobOfferMapper.map(newJobOfferEntity);

    }



    @Transactional
    public JobOffer updateJobOffer(JobOffer jobOffer){
        if (jobOffer.getId() != null) {
            // Sprawdź, czy CV istnieje w bazie danych
            JobOfferEntity jobOfferEntity = jobOfferRepository.findById(jobOffer.getId())
                    .orElseThrow(() -> new EntityNotFoundException("JobOffer with ID " + jobOffer.getId() + " not found"));


            jobOfferEntity.setCompanyName(jobOffer.getCompanyName());
            jobOfferEntity.setPosition(jobOffer.getPosition());
            jobOfferEntity.setResponsibilities(jobOffer.getResponsibilities());
            jobOfferEntity.setRequiredTechnologies(jobOffer.getRequiredTechnologies());
            jobOfferEntity.setBenefits(jobOffer.getBenefits());
            jobOfferEntity.setExperience(jobOffer.getExperience());
            jobOfferEntity.setSalaryMin(jobOffer.getSalaryMin());
            jobOfferEntity.setSalaryMax(jobOffer.getSalaryMax());
            jobOfferEntity.setJobLocation(jobOffer.getJobLocation());
            jobOfferEntity.setTypeOfContract(jobOffer.getTypeOfContract());
            jobOfferEntity.setTypeOfWork(jobOffer.getTypeOfWork());
            jobOfferEntity.setJobDescription(jobOffer.getJobDescription());
            jobOfferEntity.setRequiredLanguage(jobOffer.getRequiredLanguage());


            // Zapisz zaktualizowany obiekt CV w bazie danych
            JobOfferEntity jobOfferEntityUpdate = jobOfferRepository.save(jobOfferEntity);

            return jobOfferMapper.map(jobOfferEntityUpdate);
        } else {
            // Obsłuż sytuację, gdy CV nie zostało znalezione w bazie danych
            throw new EntityNotFoundException("Job Offer ID cannot be null");
        }
    }

    public void deleteJobOfferAndSetNullInNotifications(Integer jobOfferId) {

        JobOfferEntity jobOfferEntity = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new IllegalArgumentException("Oferta pracy o identyfikatorze " + jobOfferId + " nie została znaleziona."));

        List<NotificationEntity> notifications = notificationJpaRepository.findByJobOfferId(jobOfferEntity.getId());


        for (NotificationEntity notification : notifications) {
            notification.setJobOffer(null);
            notification.setCompanyMessage("usunąłeś swoją ofertę pracy");
            notification.setCandidateMessage("użytkownik usunął ofertę pracy");
            notification.setStatus(Status.REJECT);

        }

        jobOfferRepository.deleteById(jobOfferEntity.getId());
    }

//    public Page<JobOffer> findAllJobOffersForPage(Pageable pageable) {
//        Page<JobOfferEntity> jobOfferEntities = jobOfferRepository.findAll(pageable);
//        return jobOfferEntities.map(jobOfferMapper::map);
//    }


    public List<JobOffer> searchJobOffersByKeywordAndCategory(String keyword, String category) {
        List<JobOfferEntity> searchJobOfferEntities =  jobOfferRepository.findActiveJobOffersByKeywordAndCategory(keyword, category);
        List<JobOffer> jobOffers = searchJobOfferEntities.stream()
                .map(jobOfferMapper::map)
                .toList();
        return jobOffers;
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
}





//    public List<JobOffer> findAllJobOffersForPage() {
//        return jobOfferRepository.findAll().stream()
//                .map(jobOfferMapper::map)
//                .toList();
//    }



//    public List<JobOffer> searchJobOffersByKeywordAndCategory(String keyword, String category) {
////        BigDecimal keywordAsBigDecimal = new BigDecimal(keyword);
//        List<JobOfferEntity> searchJobOfferEntities = jobOfferRepository.findJobOffersByKeywordAndCategory(keyword, category);
//        List<JobOffer> jobOffers = searchJobOfferEntities.stream()
//                .map(jobOfferMapper::map)
//                .toList();
//        return jobOffers;
//    }



//    public Optional<JobOffer> findById2(Integer id) {
//        return jobOfferRepository.findById(id).map(jobOfferMapper::map);
//    }

//    public JobOffer findById(Integer id) {
//        JobOfferEntity jobOfferEntity = jobOfferRepository.findById(id)
//                .orElseThrow(()-> new EntityNotFoundException("Not found JobOffer with ID: " + id));
//        return jobOfferMapper.map(jobOfferEntity);
//    }


//    public List<JobOffer> findListByUser(User user) {
//        UserEntity userEntity = userMapper.map(user);
//     List<JobOfferEntity> jobOfferEntityList = jobOfferRepository.findListByUser(userEntity);
//     List <JobOffer> jobOfferList = jobOfferMapper.map(jobOfferEntityList);
//     return jobOfferList;
//    }

//    public Optional<JobOffer> findByUser(User loggedInUser) {
//        Optional<JobOfferEntity> jobOfferEntityOptional = jobOfferRepository.findByUser(userMapper.map(loggedInUser));
//        return jobOfferEntityOptional.map(jobOfferMapper::map);
//    }