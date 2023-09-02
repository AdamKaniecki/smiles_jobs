package pl.zajavka.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;

import java.util.Optional;

public interface JobOfferJpaRepository extends JpaRepository<JobOfferEntity, Integer> {
    Optional<JobOfferEntity> findByNumber(String number);
}
