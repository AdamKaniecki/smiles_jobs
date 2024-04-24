package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.infrastructure.business.dao.AddressDAO;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.security.RoleEntity;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {

    private AddressMapper addressMapper;
    private final AddressDAO addressDAO;


    @Transactional
    public Address createAddress(Address address) {
        return addressDAO.createAddress(address);

    }

    @Transactional
    public void updateAddress(Address address) {

        if (!addressDAO.existsById(address.getId())) {
            throw new EntityNotFoundException("Address with id " + address.getId() + " not found");
        }

        AddressEntity existingEntity = addressMapper.map(address);

        existingEntity.setCountry(address.getCountry());
        existingEntity.setCity(address.getCity());
        existingEntity.setStreetAndNumber(address.getStreetAndNumber());
        existingEntity.setPostalCode(address.getPostalCode());

        addressDAO.save(existingEntity);
    }



    @Transactional
    public void deleteAddress(Address address) {
        if(address != null){
            AddressEntity addressEntity = addressMapper.map(address);
            addressDAO.deleteById(addressEntity.getId());
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
        return "home";
    }


    public Address findById(Integer id) {
      return   addressDAO.findById(id);
    }
}
