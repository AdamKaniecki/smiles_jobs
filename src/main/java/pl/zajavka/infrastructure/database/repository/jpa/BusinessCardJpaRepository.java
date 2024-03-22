package pl.zajavka.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.Optional;

@Repository
public interface BusinessCardJpaRepository extends JpaRepository<BusinessCardEntity, Integer> {

    Optional<BusinessCardEntity> findByUser(UserEntity userEntity);

    boolean existsByUser(UserEntity map);

    Optional <BusinessCardEntity> findById(Integer id);
}
