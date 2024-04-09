package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import pl.zajavka.infrastructure.database.entity.AddressEntity;

@UtilityClass
public class AddressFixtures {

    public static AddressEntity someAddressEntity1(){
          AddressEntity address = AddressEntity.builder()
                .country("Poland")
                .city("Warsaw")
                .postalCode("00-001")
                .streetAndNumber("Main Street 123")
                .build();
        return address;

    }
}
