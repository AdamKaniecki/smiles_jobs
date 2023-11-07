package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;

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
//        UserEntity userEntity = userRepository.findById(userId)
//                .orElseThrow(() -> new EntityNotFoundException("Brak użytkownika o userId: " + userId));
        System.out.println("twórz psie");
        // Stwórz nową reklamę
        JobOfferEntity newJobOfferEntity = JobOfferEntity.builder()
                .companyName(jobOffer.getCompanyName())
                .position(jobOffer.getPosition())
                .responsibilities(jobOffer.getResponsibilities())
                .requiredTechnologies(jobOffer.getRequiredTechnologies())
                .benefits(jobOffer.getBenefits())
                .salaryRange(jobOffer.getSalaryRange())
                .user(userMapper.map(user))
                .build();

        jobOfferRepository.saveAndFlush(newJobOfferEntity);

        return jobOfferMapper.map(newJobOfferEntity);


        // Zapisz użytkownika w bazie danych
//        userRepository.save(userEntity);
//       jobOfferRepository.save(jobOfferEntity);


    }

    public List<JobOfferEntity> getAllJobOffers() {

        return jobOfferRepository.findAll();
    }
}
