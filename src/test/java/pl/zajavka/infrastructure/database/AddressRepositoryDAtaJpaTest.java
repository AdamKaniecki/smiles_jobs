package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.repository.jpa.AddressJpaRepository;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AddressRepositoryDAtaJpaTest extends AbstractJpaIT{

    private AddressJpaRepository addressJpaRepository;

    @Test
    void shouldSaveAddress() {
        // given
        AddressEntity address = AddressEntity.builder()
                .country("Poland")
                .city("Warsaw")
                .postalCode("00-001")
                .streetAndNumber("Main Street 123")
                .build();

        // when
        AddressEntity savedAddress = addressJpaRepository.save(address);

        // then
        assertThat(savedAddress).isNotNull();
        assertThat(savedAddress.getId()).isNotNull();
        assertThat(savedAddress.getCountry()).isEqualTo("Poland");
        assertThat(savedAddress.getCity()).isEqualTo("Warsaw");
        assertThat(savedAddress.getPostalCode()).isEqualTo("00-001");
        assertThat(savedAddress.getStreetAndNumber()).isEqualTo("Main Street 123");
    }

    @Test
    void shouldDeleteAddress() {
        // given
        AddressEntity address = AddressEntity.builder()
                .country("Poland")
                .city("Warsaw")
                .postalCode("00-001")
                .streetAndNumber("Main Street 123")
                .build();
        AddressEntity savedAddress = addressJpaRepository.save(address);

        // when
        addressJpaRepository.delete(savedAddress);

        // then
        assertThat(addressJpaRepository.findById(savedAddress.getId())).isEmpty();
    }





}
