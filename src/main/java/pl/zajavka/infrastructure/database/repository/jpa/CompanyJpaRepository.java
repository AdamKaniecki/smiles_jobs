package pl.zajavka.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.zajavka.infrastructure.database.entity.CandidateEntity;
import pl.zajavka.infrastructure.database.entity.CompanyEntity;

import java.util.Optional;

public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, Integer> {
    Optional<CompanyEntity> findByEmail(String email);
}
