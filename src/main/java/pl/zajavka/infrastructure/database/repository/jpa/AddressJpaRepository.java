package pl.zajavka.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.domain.Address;

import java.util.Optional;

@Repository
public interface AddressJpaRepository extends JpaRepository<AddressEntity, Integer> {

    @Override
    Optional<AddressEntity> findById(Integer integer);

}
