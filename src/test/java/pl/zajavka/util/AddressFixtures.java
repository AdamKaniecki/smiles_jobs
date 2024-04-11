package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.domain.Address;

@UtilityClass
public class AddressFixtures {

    public static AddressEntity someAddressEntity1() {
        AddressEntity addressEntity = AddressEntity.builder()
                .country("Poland")
                .city("Warsaw")
                .postalCode("00-001")
                .streetAndNumber("Main Street 123")
                .build();
        return addressEntity;

    }

    public static Address someAddress() {

        Address address = Address.builder()
                .country("Poland")
                .city("Warsaw")
                .postalCode("00-001")
                .streetAndNumber("Main Street 123")
                .build();
        return address;
    }
}
