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
//    public List<JobOffer> findAll() {
//        return jobOfferRepository.findAll().stream()
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
package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Service
public class JobOfferService {
    private JobOfferRepository jobOfferRepository;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private JobOfferMapper jobOfferMapper;
    private UserService userService;

    @Transactional
    public JobOffer create(JobOffer jobOffer, User user) {
        System.out.println("twórz psie");
        log.info("Received job offer: {}", jobOffer);
        OffsetDateTime currentDateTime = OffsetDateTime.now();

        JobOfferEntity newJobOfferEntity = JobOfferEntity.builder()
//                .id(jobOffer.getId())
                .companyName(jobOffer.getCompanyName())
                .position(jobOffer.getPosition())
                .responsibilities(jobOffer.getResponsibilities())
                .requiredTechnologies(jobOffer.getRequiredTechnologies())
                .benefits(jobOffer.getBenefits())
//                .salaryMin(jobOffer.getSalaryMin())
//                .salaryMax(jobOffer.getSalaryMax())
                .jobOfferDateTime(currentDateTime)
                .user(userMapper.map(user))
                .build();

//        newJobOfferEntity.setSalaryMin(minSalary);
//        newJobOfferEntity.setSalaryMax(maxSalary);

        jobOfferRepository.saveAndFlush(newJobOfferEntity);
        return jobOfferMapper.map(newJobOfferEntity);

    }

    private BigDecimal parseSalary(String salaryString) {
        try {
            // Spróbuj parsować wartość Salary jako BigDecimal
            return new BigDecimal(salaryString);
        } catch (NumberFormatException e) {
            // Obsłuż błąd parsowania - możesz rzucić wyjątkiem lub zwrócić domyślną wartość
            throw new IllegalArgumentException("Nieprawidłowa wartość Salary: " + salaryString, e);
        }
    }

    public List<JobOffer> findAll() {
        return jobOfferRepository.findAll().stream()
                .map(jobOfferMapper::map)
                .toList();
    }

    public List<JobOffer> searchJobOffersByKeywordAndCategory(String keyword, String category) {
        List<JobOfferEntity> searchJobOfferEntities =  jobOfferRepository.findJobOffersByKeywordAndCategory(keyword, category);
        return searchJobOfferEntities.stream()
                .map(jobOfferMapper::map)
                .toList();
    }

    private BigDecimal parseSalaryRange(String salaryRange) {
        try {
            // Spróbuj sparsować tekst na liczbę
            return new BigDecimal(salaryRange.trim());
        } catch (NumberFormatException e) {
            // Obsłuż sytuację, gdy tekst nie może być sparsowany
            throw new IllegalArgumentException("Nieprawidłowy format liczby: " + salaryRange);
        }
    }

    public Optional<JobOffer> findById(Integer id) {
        return jobOfferRepository.findById(id).map(jobOfferMapper::map);
    }


    public List<JobOffer> findListByUser(User user) {
        UserEntity userEntity = userMapper.map(user);
     List<JobOfferEntity> jobOfferEntityList = jobOfferRepository.findListByUser(userEntity);
     List <JobOffer> jobOfferList = jobOfferMapper.map(jobOfferEntityList);
     return jobOfferList;
    }

    public Optional<JobOffer> findByUser(User loggedInUser) {
        Optional<JobOfferEntity> jobOfferEntityOptional = jobOfferRepository.findByUser(userMapper.map(loggedInUser));
        return jobOfferEntityOptional.map(jobOfferMapper::map);
    }


    @Transactional
    public JobOffer updateJobOffer(JobOffer jobOffer){
        if (jobOffer.getId() != null) {
            // Sprawdź, czy CV istnieje w bazie danych
            JobOfferEntity jobOfferEntity = jobOfferRepository.findById(jobOffer.getId())
                    .orElseThrow(() -> new NotFoundException("JobOffer with ID " + jobOffer.getId() + " not found"));


            jobOfferEntity.setCompanyName(jobOffer.getCompanyName());
            jobOfferEntity.setPosition(jobOffer.getPosition());
            jobOfferEntity.setResponsibilities(jobOffer.getResponsibilities());
            jobOfferEntity.setRequiredTechnologies(jobOffer.getRequiredTechnologies());
            jobOfferEntity.setBenefits(jobOffer.getBenefits());


            // Zapisz zaktualizowany obiekt CV w bazie danych
            JobOfferEntity jobOfferEntityUpdate = jobOfferRepository.save(jobOfferEntity);

            return jobOfferMapper.map(jobOfferEntityUpdate);
        } else {
            // Obsłuż sytuację, gdy CV nie zostało znalezione w bazie danych
            throw new NotFoundException("Job Offer ID cannot be null");
        }
    }

    public void deleteJobOffer(Integer jobOfferId) {
        // Pobierz ofertę pracy na podstawie jej identyfikatora
        JobOfferEntity jobOfferEntity = jobOfferRepository.findById(jobOfferId)
                .orElseThrow(() -> new IllegalArgumentException("Oferta pracy o identyfikatorze " + jobOfferId + " nie została znaleziona."));

        // Mapuj encję na domenową klasę JobOffer
        JobOffer jobOffer = jobOfferMapper.map(jobOfferEntity);
        // Usuń ofertę pracy
        jobOfferRepository.delete(jobOfferEntity);
    }


}



