package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
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
//        BigDecimal minSalary = (jobOffer.getSalaryMin() != null && jobOffer.getSalaryMin().compareTo(BigDecimal.ZERO) > 0) ? jobOffer.getSalaryMin() : null;
//        BigDecimal maxSalary = (jobOffer.getSalaryMax() != null && jobOffer.getSalaryMax().compareTo(BigDecimal.ZERO) > 0) ? jobOffer.getSalaryMax() : null;

        JobOfferEntity newJobOfferEntity = JobOfferEntity.builder()
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

    public List<JobOfferEntity> getAllJobOffers() {
        return jobOfferRepository.findAll();
    }

    public List<JobOfferEntity> searchJobOffersByKeywordAndCategory(String keyword, String category) {
        return jobOfferRepository.findJobOffersByKeywordAndCategory(keyword, category);
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
}
