package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.infrastructure.business.dao.AddressDAO;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.AddressFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddressServiceTest extends AbstractIT {

    @Mock
    private AddressDAO addressDAO;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    @Test
    public void createAddress_ShouldReturnCreatedAddress() {
        // Given
        Address address = AddressFixtures.someAddress();


        // Mockowanie zachowania AddressDAO
        when(addressDAO.createAddress(address)).thenReturn(address);

        // When
        Address createdAddress = addressService.createAddress(address);

        // Then
        // Sprawdzenie, czy metoda createAddress z AddressDAO została wywołana z odpowiednim argumentem
        verify(addressDAO).createAddress(address);

        // Sprawdzenie, czy zwrócono poprawny adres
        assertEquals(address.getId(), createdAddress.getId());
        assertEquals(address.getCountry(), createdAddress.getCountry());
        assertEquals(address.getCity(), createdAddress.getCity());
        assertEquals(address.getStreetAndNumber(), createdAddress.getStreetAndNumber());
        assertEquals(address.getPostalCode(), createdAddress.getPostalCode());
    }

    @Test
    public void updateAddress_ShouldUpdateExistingAddress() {
        // Given
        Address address = AddressFixtures.someAddress();

        // Symulacja istnienia adresu w bazie danych
        when(addressDAO.existsById(address.getId())).thenReturn(true);

        // When
        addressService.updateAddress(address);

        // Then
        // Sprawdzenie, czy metoda existsById z AddressDAO została wywołana z odpowiednim argumentem
        verify(addressDAO).existsById(address.getId());

        // Sprawdzenie, czy metoda updateAddress z AddressDAO została wywołana z odpowiednim argumentem
        verify(addressDAO).updateAddress(address);
    }

    @Test
    public void updateAddress_ShouldThrowEntityNotFoundException_WhenAddressDoesNotExist() {
        // Given
        Address address = AddressFixtures.someAddress();

        // Symulacja braku adresu w bazie danych
        when(addressDAO.existsById(address.getId())).thenReturn(false);

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> addressService.updateAddress(address));
    }

    @Test
    public void deleteAddress_ShouldDeleteAddress_WhenAddressIsNotNull() {
        // Given
        Address address = AddressFixtures.someAddress();
        AddressEntity addressEntity = AddressFixtures.someAddressEntity1();

        // Symulacja mapowania adresu na encję
        when(addressMapper.map(address)).thenReturn(addressEntity);

        // When
        addressService.deleteAddress(address);

        // Then
        // Sprawdzenie, czy metoda deleteById z AddressDAO została wywołana z odpowiednim argumentem
        verify(addressDAO).deleteById(address.getId());
    }

    @Test
    public void deleteAddress_ShouldThrowIllegalArgumentException_WhenAddressIsNull() {
        // Given
        Address address = null;

        // When, Then
        // Sprawdzenie, czy metoda deleteAddress rzuca wyjątek IllegalArgumentException, gdy adres jest null
        assertThrows(IllegalArgumentException.class, () -> addressService.deleteAddress(address));
    }

    @Test
    public void determineRoleSpecificString_ShouldReturnUpdateAddressSuccessfullyCV_WhenUserIsCandidate() {
        // Given
        User loggedInUser = UserFixtures.someUser1();

        // When
        String result = addressService.determineRoleSpecificString(loggedInUser);

        // Then
        assertEquals("update_address_successfully_cv", result);
    }

    @Test
    public void determineRoleSpecificString_ShouldReturnUpdateAddressSuccessfullyBusinessCard_WhenUserIsCompany() {
        // Given
        User loggedInUser = UserFixtures.someUser2();

        // When
        String result = addressService.determineRoleSpecificString(loggedInUser);

        // Then
        assertEquals("update_address_successfully_business_card", result);
    }

    @Test
    public void determineRoleSpecificString_ShouldReturnHome_WhenUserIsNull() {

        User loggedInUser = null;

        // When
        String result = addressService.determineRoleSpecificString(loggedInUser);

        // Then
        assertEquals("home", result);
    }

    @Test
    public void findById_ShouldReturnAddress_WhenIdExists() {
        // Given
        Integer id = 1;
        Address expectedAddress = new Address();
        when(addressDAO.findById(id)).thenReturn(expectedAddress);

        // When
        Address actualAddress = addressService.findById(id);

        // Then
        assertEquals(expectedAddress, actualAddress);
    }

    @Test
    public void findById_ShouldReturnNull_WhenIdDoesNotExist() {
        // Given
        Integer id = 1;
        when(addressDAO.findById(id)).thenReturn(null);

        // When
        Address actualAddress = addressService.findById(id);

        // Then
        assertEquals(null, actualAddress);
    }

}
