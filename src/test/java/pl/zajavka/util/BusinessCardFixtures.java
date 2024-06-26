package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.repository.jpa.BusinessCardJpaRepository;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.Set;

@UtilityClass
public class BusinessCardFixtures {


    public static BusinessCardEntity someBusinessCardEntity() {

        AddressEntity address = AddressEntity.builder()
                .city("Sample City")
                .country("Sample Country")
                .streetAndNumber("Sample Street 125")
                .postalCode("09-500")
                .build();

        RoleEntity companyRole = RoleEntity.builder().role("ROLE_COMPANY").build();
        UserEntity userEntity = UserEntity.builder()
                .userName("john34")
                .roles(Set.of(companyRole))
                .password("john34")
                .email("john34@example.com")
                .active(true)
                .build();

        BusinessCardEntity businessCardEntity = BusinessCardEntity.builder()
                .office("Office 123")
                .scopeOperations("Scope of operations")
                .recruitmentEmail("recruitment@example.com")
                .phoneNumber("+12 345 678 090")
                .companyDescription("Company description")
                .technologiesAndTools("Technologies and tools")
                .certificatesAndAwards("Certificates and awards")
                .address(address)
                .user(userEntity)
                .build();



        return businessCardEntity;

    }

    public static BusinessCard someBusinessCard() {

        Address address = Address.builder()
                .id(1)
                .city("Sample City")
                .country("Sample Country")
                .streetAndNumber("Sample Street 125")
                .postalCode("09-500")
                .build();

        RoleEntity companyRole = RoleEntity.builder().role("ROLE_COMPANY").build();
        User user = User.builder()
                .id(1)
                .userName("john34")
                .password("john34")
                .email("john34@example.com")
                .roles(Set.of(companyRole))
                .active(true)
                .build();

        BusinessCard businessCard = BusinessCard.builder()
                .id(1)
                .office("Office 123")
                .scopeOperations("Scope of operations")
                .recruitmentEmail("recruitment@example.com")
                .phoneNumber("+12 345 678 090")
                .companyDescription("Company description")
                .technologiesAndTools("Technologies and tools")
                .certificatesAndAwards("Certificates and awards")
                .address(address)
                .user(user)
                .build();


        return businessCard;
    }


}
