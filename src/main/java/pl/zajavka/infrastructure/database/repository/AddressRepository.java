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


    public Address findById(Integer addressId) {
        AddressEntity addressEntity = addressJpaRepository.findById(addressId)
                .orElseThrow(() -> new EntityNotFoundException("Address not found with id: " + addressId));
        return addressMapper.map(addressEntity);
    }



    @Override
    public boolean existsById(Integer id) {
        return addressJpaRepository.existsById(id);
    }

    @Override
    public void save(AddressEntity existingEntity) {
       addressJpaRepository.save(existingEntity);
    }

    @Override
    public void deleteById(Integer id) {
    addressJpaRepository.deleteById(id);
    }

    @Override
    public Address createAddress(Address address) {
        AddressEntity entity = AddressEntity.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .streetAndNumber(address.getStreetAndNumber())
                .postalCode(address.getPostalCode())
                .build();

        AddressEntity createEntity = addressJpaRepository.saveAndFlush(entity);

        return addressMapper.map(createEntity);
    }
}
