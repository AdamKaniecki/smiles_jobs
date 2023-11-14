package pl.zajavka.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;

@Repository
public interface BusinessCardRepository extends JpaRepository<BusinessCardEntity, Integer> {
}
