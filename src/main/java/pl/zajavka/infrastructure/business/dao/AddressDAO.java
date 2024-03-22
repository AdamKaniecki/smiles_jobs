package pl.zajavka.infrastructure.business.dao;

import pl.zajavka.infrastructure.domain.Address;

public interface AddressDAO {
    Address findById(Integer addressId);
}
