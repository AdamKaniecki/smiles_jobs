package pl.zajavka.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Integer> {


}