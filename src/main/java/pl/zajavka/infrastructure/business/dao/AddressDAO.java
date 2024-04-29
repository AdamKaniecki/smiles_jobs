package pl.zajavka.infrastructure.business.dao;

import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.domain.Address;

import java.util.Optional;

public interface AddressDAO {
    Address findById(Integer addressId);

    boolean existsById(Integer id);

    void save(AddressEntity existingEntity);

    void deleteById(Integer id);
    Address createAddress(Address address);

    void updateAddress(Address address);
}
