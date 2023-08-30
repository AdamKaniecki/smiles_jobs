package pl.zajavka.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zajavka.domain.Candidate;
import pl.zajavka.infrastructure.database.entity.CandidateAdvertisementEntity;

import java.util.Optional;

public interface CandidateAdvertisementJpaRepository extends JpaRepository<CandidateAdvertisementEntity, Integer> {
    Optional <CandidateAdvertisementEntity> findByNumber(String number);


}
