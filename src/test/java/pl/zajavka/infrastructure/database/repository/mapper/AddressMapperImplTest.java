package pl.zajavka.infrastructure.database.repository.mapper;

import org.junit.jupiter.api.Test;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapperImpl;
import pl.zajavka.infrastructure.domain.Address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AddressMapperImplTest {

    private final AddressMapperImpl mapper = new AddressMapperImpl();

    @Test
    void map_ShouldMapAddressEntityToAddress() {
        // Given
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(1);
        addressEntity.setCountry("Poland");
        addressEntity.setCity("Warsaw");
        addressEntity.setStreetAndNumber("Example Street 123");
        addressEntity.setPostalCode("00-000");

        // When
        Address address = mapper.map(addressEntity);

        // Then
        assertEquals(1, address.getId());
        assertEquals("Poland", address.getCountry());
        assertEquals("Warsaw", address.getCity());
        assertEquals("Example Street 123", address.getStreetAndNumber());
        assertEquals("00-000", address.getPostalCode());
    }

//    @Test
//    void map_ShouldMapNullAddressEntityToNull() {
//        // When
//        Address address = mapper.map(null);
//
//        // Then
//        assertNull(address);
//    }

    @Test
    void map_ShouldMapAddressToAddressEntity() {
        // Given
        Address address = new Address();
        address.setId(1);
        address.setCountry("Poland");
        address.setCity("Warsaw");
        address.setStreetAndNumber("Example Street 123");
        address.setPostalCode("00-000");

        // When
        AddressEntity addressEntity = mapper.map(address);

        // Then
        assertEquals(1, addressEntity.getId());
        assertEquals("Poland", addressEntity.getCountry());
        assertEquals("Warsaw", addressEntity.getCity());
        assertEquals("Example Street 123", addressEntity.getStreetAndNumber());
        assertEquals("00-000", addressEntity.getPostalCode());
    }

//    @Test
//    void map_ShouldMapNullAddressToNull() {
//        // When
//        AddressEntity addressEntity = mapper.map(null);
//
//        // Then
//        assertNull(addressEntity);
//    }
}
