package pl.zajavka.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.repository.AddressRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;

import java.util.Optional;

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

    @Transactional
    public void updateAddress(Address address) {
        // Sprawdź, czy adres istnieje w bazie danych
        if (address.getId() != null && addressRepository.existsById(address.getId())) {
            // Jeśli istnieje, zaktualizuj istniejący rekord
            Optional<AddressEntity> existingEntityOptional = addressRepository.findById(address.getId());

            // Pobierz obiekt AddressEntity z Optional
            if (existingEntityOptional.isPresent()) {
                AddressEntity existingEntity = existingEntityOptional.get();

                // Zaktualizuj pola bezpośrednio
                existingEntity.setCountry(address.getCountry());
                existingEntity.setCity(address.getCity());
                existingEntity.setStreetAndNumber(address.getStreetAndNumber());

                addressRepository.save(existingEntity);
            } else {
                // Jeśli Optional nie zawiera wartości, obsłuż odpowiednio (np. rzutuj wyjątek)
                throw new EntityNotFoundException("Address with id " + address.getId() + " not found");
            }
        } else {
            // Jeśli nie istnieje, obsłuż odpowiednio (np. rzutuj wyjątek)
            throw new EntityNotFoundException("Address with id " + address.getId() + " not found");
        }
    }

    public Optional<Address> findById(Integer id) {
        return addressRepository.findById(id).map(addressMapper::map);
    }

    public void deleteAddress(Address address) {
        if(address != null){
            AddressEntity addressEntity = addressMapper.map(address);
            addressRepository.deleteById(addressEntity.getId());
        } else {
            throw new IllegalArgumentException("Address cannot be null");
        }
    }

//    public void deleteCV(CV cv) {
//        if (cv != null) {
//            // Mapuj CV na CvEntity przed usunięciem z bazy danych
//            CvEntity cvEntity = cvMapper.map(cv);
//            cvRepository.deleteById(cvEntity.getId());
//        } else {
//            throw new IllegalArgumentException("CV cannot be null");
//        }
//    }
}
