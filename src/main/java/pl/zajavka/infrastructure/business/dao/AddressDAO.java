package pl.zajavka.infrastructure.business.dao;

import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.domain.Address;

import java.util.Optional;

public interface AddressDAO {
    Address findById(Integer addressId);


    AddressEntity saveAndFlush(AddressEntity entity);

    boolean existsById(Integer id);

    void save(AddressEntity existingEntity);

    void deleteById(Integer id);
}
