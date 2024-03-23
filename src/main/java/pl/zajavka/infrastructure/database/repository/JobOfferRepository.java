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

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JobOfferRepository implements JobOfferDAO {

    private final JobOfferJpaRepository jobOfferJpaRepository;
    private final JobOfferMapper jobOfferMapper;
    private final UserMapper userMapper;
    public Optional<JobOffer> findById2(Integer id) {
        return jobOfferJpaRepository.findById(id).map(jobOfferMapper::map);
    }

    public JobOffer findById(Integer id) {
        JobOfferEntity jobOfferEntity = jobOfferJpaRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Not found JobOffer with ID: " + id));
        return jobOfferMapper.map(jobOfferEntity);
    }

    public List<JobOffer> findListByUser(User user) {
        UserEntity userEntity = userMapper.map(user);
        List<JobOfferEntity> jobOfferEntityList = jobOfferJpaRepository.findListByUser(userEntity);
        List <JobOffer> jobOfferList = jobOfferMapper.map(jobOfferEntityList);
        return jobOfferList;
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
}
