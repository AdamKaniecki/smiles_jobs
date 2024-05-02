package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.repository.jpa.AddressJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.AddressFixtures;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class AddressRepositoryTest extends AbstractIT {

    @Mock
    private  AddressJpaRepository addressJpaRepository;
    @Mock
    private  AddressMapper addressMapper;
    @InjectMocks
    private AddressRepository addressRepository;


    @Test
    void findById_ShouldReturnAddress_WhenAddressExists() {
        // Given
        Integer addressId = 1;
        AddressEntity addressEntity = new AddressEntity();
        Address expectedAddress = new Address();
        when(addressJpaRepository.findById(addressId)).thenReturn(Optional.of(addressEntity));
        when(addressMapper.map(addressEntity)).thenReturn(expectedAddress);

        // When
        Address actualAddress = addressRepository.findById(addressId);

        // Then
        assertEquals(expectedAddress, actualAddress); // Sprawdzenie, czy zwrócona adresa jest poprawna
        verify(addressJpaRepository).findById(addressId); // Sprawdzenie, czy metoda findById została wywołana z odpowiednim argumentem
        verify(addressMapper).map(addressEntity); // Sprawdzenie, czy metoda map została wywołana z odpowiednim argumentem
    }

    @Test
    void findById_ShouldThrowEntityNotFoundException_WhenAddressDoesNotExist() {
        // Given
        Integer addressId = 1;
        when(addressJpaRepository.findById(addressId)).thenReturn(Optional.empty());

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            addressRepository.findById(addressId);
        });

        // Then
        assertEquals("Address not found with id: " + addressId, exception.getMessage()); // Sprawdzenie, czy wyjątek został rzucany z odpowiednią wiadomością
        verify(addressJpaRepository).findById(addressId); // Sprawdzenie, czy metoda findById została wywołana z odpowiednim argumentem
        verifyNoInteractions(addressMapper); // Sprawdzenie, czy nie było interakcji z mapperem w przypadku rzucenia wyjątku
    }

    @Test
    void existsById_ShouldReturnTrue_WhenAddressExists() {
        // Given
        Integer addressId = 1;
        when(addressJpaRepository.existsById(addressId)).thenReturn(true);

        // When
        boolean result = addressRepository.existsById(addressId);

        // Then
        assertTrue(result); // Sprawdzenie, czy istnienie adresu zwraca true
    }

    @Test
    void existsById_ShouldReturnFalse_WhenAddressDoesNotExist() {
        // Given
        Integer addressId = 1;
        when(addressJpaRepository.existsById(addressId)).thenReturn(false);

        // When
        boolean result = addressRepository.existsById(addressId);

        // Then
        assertFalse(result); // Sprawdzenie, czy brak adresu zwraca false
    }

    @Test
    void save_ShouldCallSaveMethod() {
        // Given
        AddressEntity existingEntity = new AddressEntity();

        // When
        addressRepository.save(existingEntity);

        // Then
        verify(addressJpaRepository).save(existingEntity); // Sprawdzenie, czy metoda save została wywołana z odpowiednim argumentem
    }

    @Test
    void deleteById_ShouldCallDeleteByIdMethodOfJpaRepository() {
        // Given
        Integer id = 1;

        // When
        addressRepository.deleteById(id);

        // Then
        verify(addressJpaRepository).deleteById(id); // Sprawdzenie, czy metoda deleteById została wywołana z odpowiednim argumentem
    }

    @Test
    void deleteById_ShouldBeCalledWithCorrectId() {
        // Given
        Integer id = 2;

        // When
        addressRepository.deleteById(id);

        // Then
        verify(addressJpaRepository).deleteById(id); // Sprawdzenie, czy metoda deleteById została wywołana z poprawnym identyfikatorem
    }

    @Test
    void createAddress_ShouldReturnMappedAddress() {
        // Given
        Address address = AddressFixtures.someAddress();
        AddressEntity entity = AddressFixtures.someAddressEntity1();

        when(addressJpaRepository.saveAndFlush(entity)).thenReturn(entity); // Symuluj zachowanie repozytorium podczas zapisu
        when(addressMapper.map(entity)).thenReturn(address); // Symuluj zachowanie mapper'a podczas mapowania

        // When
        Address resultAddress = addressRepository.createAddress(address);

        // Then
        assertEquals(address, resultAddress); // Sprawdź, czy zwrócony adres jest taki sam jak oczekiwany
        verify(addressJpaRepository).saveAndFlush(entity); // Sprawdź, czy metoda saveAndFlush została wywołana z odpowiednim argumentem
        verify(addressMapper).map(entity); // Sprawdź, czy metoda map została wywołana z odpowiednim argumentem
    }

    @Test
    public void testUpdateAddress_Success() {
        // Given
        Address address = new Address();
        address.setId(1);
        address.setCountry("Poland");
        address.setCity("Warsaw");
        address.setStreetAndNumber("Example Street 123");
        address.setPostalCode("00-000");

        AddressEntity addressEntity = new AddressEntity();
        when(addressMapper.map(address)).thenReturn(addressEntity);
        when(addressJpaRepository.save(addressEntity)).thenReturn(addressEntity);

        // When
        addressRepository.updateAddress(address);

        // Then
        verify(addressJpaRepository, times(1)).save(addressEntity);
        assertEquals(address.getCountry(), addressEntity.getCountry());
        assertEquals(address.getCity(), addressEntity.getCity());
        assertEquals(address.getStreetAndNumber(), addressEntity.getStreetAndNumber());
        assertEquals(address.getPostalCode(), addressEntity.getPostalCode());
    }

}
