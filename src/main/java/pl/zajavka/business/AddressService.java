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
import pl.zajavka.infrastructure.security.RoleEntity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {
    private AddressRepository addressRepository;
    private AddressMapper addressMapper;
    private UserService userService;

    @Transactional
    public Address createAddress(Address address, User user) {
        AddressEntity entity = AddressEntity.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .streetAndNumber(address.getStreetAndNumber())
                .postalCode(address.getPostalCode())
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
                existingEntity.setPostalCode(address.getPostalCode());

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

//    public Optional<Address> findById(Integer id) {
//        return addressRepository.findById(id).map(addressMapper::map);
//    }

    public Address findById(Integer addressId){
        AddressEntity addressEntity = addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Not found address with ID: " + addressId));
        return addressMapper.map(addressEntity);

    }

    @Transactional
    public void deleteAddress(Address address) {
        if(address != null){
            AddressEntity addressEntity = addressMapper.map(address);
            addressRepository.deleteById(addressEntity.getId());
        } else {
            throw new IllegalArgumentException("Address cannot be null");
        }
    }






    public String determineRoleSpecificString(User loggedInUser) {

        if (loggedInUser != null) {
            Set<String> roles = loggedInUser.getRoles().stream()
                    .map(RoleEntity::getRole)
                    .collect(Collectors.toSet());

            if (roles.contains("ROLE_CANDIDATE")) {
                return "update_address_successfully_cv";
            } else if (roles.contains("ROLE_COMPANY")) {
                return "update_address_successfully_business_card";
            }
        }

        // Domyślny wynik, jeśli rola nie jest określona lub użytkownik nie istnieje
        return "home";
    }

}
