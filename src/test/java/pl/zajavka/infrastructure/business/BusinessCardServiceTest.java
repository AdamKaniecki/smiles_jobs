package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.EmptyResultDataAccessException;
import pl.zajavka.infrastructure.business.dao.BusinessCardDAO;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.BusinessCardMapper;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.BusinessCardFixtures;

import java.lang.reflect.Executable;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BusinessCardServiceTest extends AbstractIT {
    @Mock
    private BusinessCardMapper businessCardMapper;
    @Mock
    private BusinessCardDAO businessCardDAO;
    @InjectMocks
    private BusinessCardService businessCardService;


    @Test
    public void createBusinessCardTest() {
        // Given
        BusinessCard businessCard = BusinessCardFixtures.someBusinessCard();
        User user = new User(); // Utwórz użytkownika

        // Konfiguracja mocka businessCardDAO
        when(businessCardDAO.createBusinessCard(any(BusinessCard.class), any(User.class))).thenReturn(businessCard);

        // When
        BusinessCard result = businessCardService.createBusinessCard(businessCard, user);

        // Then
        assertNotNull(result, "Returned business card should not be null");
        // Możesz dodać więcej asercji, aby sprawdzić, czy metoda działa zgodnie z oczekiwaniami
    }


    @Test
    public void updateBusinessCardWithExistingIdTest() {
        // Given
        BusinessCard updatedBusinessCard = BusinessCardFixtures.someBusinessCard();

        // Konfiguracja mocka businessCardDAO
        when(businessCardDAO.findById(updatedBusinessCard.getId())).thenReturn(updatedBusinessCard);
        when(businessCardDAO.updateBusinessCard(updatedBusinessCard)).thenReturn(updatedBusinessCard);

        // When
        BusinessCard result = businessCardService.updateBusinessCard(updatedBusinessCard);

        // Then
        assertNotNull(result, "Returned business card should not be null");
        assertEquals(updatedBusinessCard, result, "Returned business card should match the updated one");
    }

    @Test
    void testDeleteBusinessCard_Success() {
        // Given
        BusinessCard businessCard = BusinessCardFixtures.someBusinessCard();
        BusinessCardEntity businessCardEntity = BusinessCardFixtures.someBusinessCardEntity();
        when(businessCardMapper.map(businessCard)).thenReturn(businessCardEntity);

        // When
        businessCardService.deleteBusinessCard(businessCard);

        // Then
        verify(businessCardMapper, times(1)).map(businessCard);
        verify(businessCardDAO, times(1)).deleteById(businessCardEntity.getId());
    }

    @Test
    void testDeleteBusinessCard_EntityNotFoundException() {
        // Given
        BusinessCard businessCard = BusinessCardFixtures.someBusinessCard();
        BusinessCardEntity businessCardEntity = BusinessCardFixtures.someBusinessCardEntity();
        when(businessCardMapper.map(businessCard)).thenReturn(businessCardEntity);


        // When
        try {
            businessCardService.deleteBusinessCard(businessCard);
        } catch (EntityNotFoundException e) {
            // Then
            assertEquals("Entity not found for delete", e.getMessage());
        }
    }

    @Test
    void testFindByUser() {
        // Given
        User user = new User(); // Tworzenie użytkownika
        BusinessCard expectedBusinessCard = new BusinessCard(); // Przykładowa karta biznesowa

        // Konfiguracja mocka businessCardDAO
        when(businessCardDAO.findByUser(any(User.class))).thenReturn(expectedBusinessCard);

        // When
        BusinessCard result = businessCardService.findByUser(user);

        // Then
        assertEquals(expectedBusinessCard, result, "Returned business card should match the expected one");
    }

    @Test
    void testExistByUser_UserExists() {
        // Given
        User existingUser = new User(); // Tworzenie istniejącego użytkownika

        // Konfiguracja mocka businessCardDAO
        when(businessCardDAO.existByUser(any(User.class))).thenReturn(true);

        // When
        boolean result = businessCardService.existByUser(existingUser);

        // Then
        assertTrue(result, "User should exist in the system");
    }
    @Test
    void testExistByUser_UserDoesNotExist() {
        // Given
        User nonExistingUser = new User(); // Tworzenie nieistniejącego użytkownika

        // Konfiguracja mocka businessCardDAO
        when(businessCardDAO.existByUser(any(User.class))).thenReturn(false);

        // When
        boolean result = businessCardService.existByUser(nonExistingUser);

        // Then
        assertFalse(result, "User should not exist in the system");
    }

    @Test
    void testFindById_CardExists() {
        // Given
        int existingId = 123; // Przykładowe istniejące ID karty biznesowej
        BusinessCard expectedCard = new BusinessCard(); // Przykładowa karta biznesowa

        // Konfiguracja mocka businessCardDAO
        when(businessCardDAO.findById(existingId)).thenReturn(expectedCard);

        // When
        BusinessCard result = businessCardService.findById(existingId);

        // Then
        assertEquals(expectedCard, result, "Returned business card should match the expected one");
    }

    @Test
    void testFindById_CardDoesNotExist() {
        // Given
        int nonExistingId = 456; // Przykładowe nieistniejące ID karty biznesowej

        // Konfiguracja mocka businessCardDAO
        when(businessCardDAO.findById(nonExistingId)).thenReturn(null);

        // When
        BusinessCard result = businessCardService.findById(nonExistingId);

        // Then
        assertNull(result, "Returned business card should be null for non-existing ID");
    }
}
