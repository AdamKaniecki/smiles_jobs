package pl.zajavka.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.util.AddressFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AddressRepositoryDAtaJpaTest extends AbstractJpaIT {

    private AddressJpaRepository addressJpaRepository;

    @Test
    void shouldSaveAddress() {

        AddressEntity address = AddressFixtures.someAddressEntity1();
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
        AddressEntity address = AddressFixtures.someAddressEntity1();
        AddressEntity savedAddress = addressJpaRepository.save(address);

        // when
        addressJpaRepository.delete(savedAddress);

        // then
        assertThat(addressJpaRepository.findById(savedAddress.getId())).isEmpty();
    }
    @Test
    void shouldFindAddressById() {
        // given
        AddressEntity address = AddressFixtures.someAddressEntity1();
        AddressEntity savedAddress = addressJpaRepository.save(address);

        // when
        Optional<AddressEntity> foundAddressOptional = addressJpaRepository.findById(savedAddress.getId());

        // then
        assertThat(foundAddressOptional).isPresent();
        AddressEntity foundAddress = foundAddressOptional.get();
        assertThat(foundAddress.getId()).isEqualTo(savedAddress.getId());
        assertThat(foundAddress.getCountry()).isEqualTo(savedAddress.getCountry());
        assertThat(foundAddress.getCity()).isEqualTo(savedAddress.getCity());
        assertThat(foundAddress.getPostalCode()).isEqualTo(savedAddress.getPostalCode());
        assertThat(foundAddress.getStreetAndNumber()).isEqualTo(savedAddress.getStreetAndNumber());
    }




}
