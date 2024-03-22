package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.infrastructure.business.dao.AddressDAO;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.domain.Address;

@Repository
@AllArgsConstructor
public class AddressRepository implements AddressDAO {
    private final AddressJpaRepository addressJpaRepository;
    private final AddressMapper addressMapper;


    public Address findById(Integer addressId){
        AddressEntity addressEntity = addressJpaRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Not found entity Address with ID: " + addressId));
        return addressMapper.map(addressEntity);

    }
}
