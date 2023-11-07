package pl.zajavka.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;

@Repository
public interface JobOfferRepository extends JpaRepository<JobOfferEntity, Integer> {

}
