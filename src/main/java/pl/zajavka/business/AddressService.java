package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.repository.AddressRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;

@Service
@AllArgsConstructor
public class AddressService {
    private AddressRepository addressRepository;
    private AddressMapper addressMapper;

    @Transactional
    public Address createAddress(Address address, User user) {
        System.out.println("Tworzę adres");

        AddressEntity entity = AddressEntity.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .streetAndNumber(address.getStreetAndNumber())
                .build();

        AddressEntity created = addressRepository.saveAndFlush(entity);

        return addressMapper.map(created);
    }

}
