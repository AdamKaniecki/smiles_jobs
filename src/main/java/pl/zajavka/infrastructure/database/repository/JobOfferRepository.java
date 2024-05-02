package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.business.dao.JobOfferDAO;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.jpa.JobOfferJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JobOfferRepository implements JobOfferDAO {

    private final JobOfferJpaRepository jobOfferJpaRepository;
    private final JobOfferMapper jobOfferMapper;
    private final UserMapper userMapper;

    public Optional<JobOffer> findByIdOpt(Integer id) {
        return jobOfferJpaRepository.findById(id).map(jobOfferMapper::map);
    }

    public JobOffer findById(Integer id) {
        JobOfferEntity jobOfferEntity = jobOfferJpaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Not found JobOffer with ID: " + id));
        return jobOfferMapper.map(jobOfferEntity);
    }

    public List<JobOffer> findListByUser(User user) {
        UserEntity userEntity = userMapper.map(user);
        List<JobOfferEntity> jobOfferEntityList = jobOfferJpaRepository.findListByUser(userEntity);
        return jobOfferMapper.map(jobOfferEntityList);
    }

    public Optional<JobOffer> findByUser(User loggedInUser) {
        Optional<JobOfferEntity> jobOfferEntityOptional = jobOfferJpaRepository.findByUser(userMapper.map(loggedInUser));
        return jobOfferEntityOptional.map(jobOfferMapper::map);
    }

    @Override
    public JobOffer saveJobOffer(JobOffer jobOffer) {
        JobOfferEntity toSave = jobOfferMapper.map(jobOffer);
        JobOfferEntity saved = jobOfferJpaRepository.save(toSave);
        return jobOfferMapper.map(saved);
    }

    @Override
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

        jobOfferJpaRepository.saveAndFlush(newJobOfferEntity);
        return jobOfferMapper.map(newJobOfferEntity);

    }

    @Override
    public JobOffer updateJobOffer(JobOffer jobOffer) {
        if (jobOffer.getId() != null) {
            JobOfferEntity jobOfferEntity = jobOfferJpaRepository.findById(jobOffer.getId())
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


            JobOfferEntity jobOfferEntityUpdate = jobOfferJpaRepository.save(jobOfferEntity);

            return jobOfferMapper.map(jobOfferEntityUpdate);
        } else {
            throw new EntityNotFoundException("JobOffer ID cannot be null");
        }
    }

    @Override
    public void deleteById(Integer jobOfferId) {
        jobOfferJpaRepository.deleteById(jobOfferId);
    }

    @Override
    public List<JobOffer> searchJobOffersByKeywordAndCategory(String keyword, String category) {
        List<JobOfferEntity> searchJobOfferEntities = jobOfferJpaRepository.findActiveJobOffersByKeywordAndCategory(keyword, category);
        return searchJobOfferEntities.stream()
                .map(jobOfferMapper::map)
                .toList();

    }

    @Override
    public List<JobOffer> searchJobOffersBySalary(String category, BigDecimal salary) {
        List<JobOfferEntity> searchJobOfferEntities = jobOfferJpaRepository.findActiveJobOffersBySalaryAndCategory(category, salary);
        return searchJobOfferEntities.stream()
                .map(jobOfferMapper::map)
                .toList();

    }
}
