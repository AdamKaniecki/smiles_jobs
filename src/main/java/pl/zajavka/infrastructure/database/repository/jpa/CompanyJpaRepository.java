package pl.zajavka.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zajavka.infrastructure.database.entity.CompanyEntity;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Integer> {
}
