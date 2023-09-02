package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.JobOfferDAO;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.infrastructure.database.repository.jpa.JobOfferJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CandidateEntityMapper;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferEntityMapper;

import java.util.List;
import java.util.Optional;
@Repository
@AllArgsConstructor
public class JobOfferRepository implements JobOfferDAO {

    private final JobOfferJpaRepository jobOfferJpaRepository;

    private final JobOfferEntityMapper candidateEntityMapper;
    @Override
    public Optional<JobOffer> findByNumber(String number) {
        return Optional.empty();
    }

    @Override
    public List<JobOffer> findJobOffers() {
        return null;
    }
}

