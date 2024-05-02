package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.jpa.BusinessCardJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.BusinessCardMapper;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.BusinessCardFixtures;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wiremock.com.google.common.base.Verify.verify;

public class BusinessCardRepositoryTest extends AbstractIT {

    @Mock
    private BusinessCardJpaRepository businessCardJpaRepository;
    @Mock
    private BusinessCardMapper businessCardMapper;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AddressMapper addressMapper;
    @InjectMocks
    private BusinessCardRepository businessCardRepository;


    @Test
    void testFindById2_WhenBusinessCardExists() {
        // Given
        Integer id = 1;
        BusinessCardEntity businessCardEntity = new BusinessCardEntity(); // Tworzymy przykładową encję BusinessCardEntity
        BusinessCard businessCard = new BusinessCard(); // Tworzymy przykładowy obiekt BusinessCard
        when(businessCardJpaRepository.findById(id)).thenReturn(Optional.of(businessCardEntity)); // Mockujemy metodę findById w repozytorium, aby zwróciła Optional z encją
        when(businessCardMapper.map(businessCardEntity)).thenReturn(businessCard); // Mockujemy mapowanie encji na obiekt domenowy

        // When
        Optional<BusinessCard> result = businessCardRepository.findById2(id); // Wywołujemy testowaną metodę

        // Then
        assertTrue(result.isPresent()); // Upewniamy się, że Optional zawiera wartość
        assertEquals(businessCard, result.get()); // Upewniamy się, że zwrócony obiekt jest poprawny
    }

    @Test
    void testFindById2_WhenBusinessCardDoesNotExist() {
        // Given
        Integer id = 1;
        when(businessCardJpaRepository.findById(id)).thenReturn(Optional.empty()); // Mockujemy metodę findById w repozytorium, aby zwróciła pusty Optional

        // When
        Optional<BusinessCard> result = businessCardRepository.findById2(id); // Wywołujemy testowaną metodę

        // Then
        assertTrue(result.isEmpty()); // Upewniamy się, że Optional jest pusty
    }

    @Test
    void testFindById_WhenBusinessCardExists() {
        // Given
        Integer id = 1;
        BusinessCardEntity businessCardEntity = new BusinessCardEntity(); // Tworzymy przykładową encję BusinessCardEntity
        BusinessCard businessCard = new BusinessCard(); // Tworzymy przykładowy obiekt BusinessCard
        when(businessCardJpaRepository.findById(id)).thenReturn(Optional.of(businessCardEntity)); // Mockujemy metodę findById w repozytorium, aby zwróciła Optional z encją
        when(businessCardMapper.map(businessCardEntity)).thenReturn(businessCard); // Mockujemy mapowanie encji na obiekt domenowy

        // When
        BusinessCard result = businessCardRepository.findById(id); // Wywołujemy testowaną metodę

        // Then
        assertNotNull(result); // Upewniamy się, że zwrócony obiekt nie jest nullem
        assertEquals(businessCard, result); // Upewniamy się, że zwrócony obiekt jest poprawny
    }

    @Test
    void testFindById_WhenBusinessCardDoesNotExist() {
        // Given
        Integer id = 1;
        when(businessCardJpaRepository.findById(id)).thenReturn(Optional.empty()); // Mockujemy metodę findById w repozytorium, aby zwróciła pusty Optional

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> businessCardRepository.findById(id)); // Upewniamy się, że wyjątek EntityNotFoundException jest rzucony
    }

    @Test
    void testSave() {
        // Given
        BusinessCardEntity businessCardEntity = new BusinessCardEntity(); // Tworzymy przykładową encję BusinessCardEntity

        // When
        businessCardRepository.save(businessCardEntity); // Wywołujemy testowaną metodę

        // Then
        Mockito.verify(businessCardJpaRepository).save(businessCardEntity); // Sprawdzamy, czy metoda save w repozytorium została wywołana z odpowiednim argumentem
    }

    @Test
    void testDeleteById() {
        // Given
        Integer id = 1; // Przykładowe ID karty biznesowej

        // When
        businessCardRepository.deleteById(id); // Wywołujemy testowaną metodę

        // Then
        Mockito.verify(businessCardJpaRepository).deleteById(id); // Sprawdzamy, czy metoda deleteById w repozytorium została wywołana z odpowiednim argumentem
    }

    @Test
    void testFindByUser_WhenBusinessCardExists() {
        // Given
        User loggedInUser = new User(); // Tworzymy przykładowego użytkownika
        BusinessCardEntity businessCardEntity = new BusinessCardEntity(); // Tworzymy przykładową encję BusinessCardEntity
        BusinessCard businessCard = new BusinessCard(); // Tworzymy przykładowy obiekt BusinessCard
        when(userMapper.map(loggedInUser)).thenReturn(new UserEntity()); // Mockujemy mapowanie użytkownika na encję
        when(businessCardJpaRepository.findByUser(Mockito.any(UserEntity.class))).thenReturn(Optional.of(businessCardEntity)); // Mockujemy metodę findByUser w repozytorium, aby zwróciła Optional z encją
        when(businessCardMapper.map(businessCardEntity)).thenReturn(businessCard); // Mockujemy mapowanie encji na obiekt domenowy

        // When
        BusinessCard result = businessCardRepository.findByUser(loggedInUser); // Wywołujemy testowaną metodę

        // Then
        assertNotNull(result); // Upewniamy się, że zwrócony obiekt nie jest nullem
        assertEquals(businessCard, result); // Upewniamy się, że zwrócony obiekt jest poprawny
    }

    @Test
    void testFindByUser_WhenBusinessCardDoesNotExist() {
        // Given
        User loggedInUser = new User(); // Tworzymy przykładowego użytkownika
        when(userMapper.map(loggedInUser)).thenReturn(new UserEntity()); // Mockujemy mapowanie użytkownika na encję
        when(businessCardJpaRepository.findByUser(Mockito.any(UserEntity.class))).thenReturn(Optional.empty()); // Mockujemy metodę findByUser w repozytorium, aby zwróciła pusty Optional

        // When
        BusinessCard result = businessCardRepository.findByUser(loggedInUser); // Wywołujemy testowaną metodę

        // Then
        assertNull(result); // Upewniamy się, że zwrócony obiekt jest nullem
    }

    @Test
    void testExistByUser_WhenBusinessCardExists() {
        // Given
        User loggedInUser = new User(); // Tworzymy przykładowego użytkownika
        when(userMapper.map(loggedInUser)).thenReturn(new UserEntity()); // Mockujemy mapowanie użytkownika na encję
        when(businessCardJpaRepository.existsByUser(Mockito.any(UserEntity.class))).thenReturn(true); // Mockujemy metodę existsByUser w repozytorium, aby zwróciła true

        // When
        boolean result = businessCardRepository.existByUser(loggedInUser); // Wywołujemy testowaną metodę

        // Then
        assertTrue(result); // Upewniamy się, że zwrócony wynik jest true
    }

    @Test
    void testExistByUser_WhenBusinessCardDoesNotExist() {
        // Given
        User loggedInUser = new User(); // Tworzymy przykładowego użytkownika
        when(userMapper.map(loggedInUser)).thenReturn(new UserEntity()); // Mockujemy mapowanie użytkownika na encję
        when(businessCardJpaRepository.existsByUser(Mockito.any(UserEntity.class))).thenReturn(false); // Mockujemy metodę existsByUser w repozytorium, aby zwróciła false

        // When
        boolean result = businessCardRepository.existByUser(loggedInUser); // Wywołujemy testowaną metodę

        // Then
        assertFalse(result); // Upewniamy się, że zwrócony wynik jest false
    }

    @Test
    public void testCreateBusinessCard() {
        // Given

        BusinessCard businessCard = BusinessCardFixtures.someBusinessCard();
        User user = businessCard.getUser();
        when(businessCardMapper.map(any(BusinessCardEntity.class))).thenReturn(businessCard);

        // When
        BusinessCard result = businessCardRepository.createBusinessCard(businessCard, user);

        // Then
        // Verify that result is not null
        assertNotNull(result);
        assertNotNull(result.getPhoneNumber());
        assertNotNull(result.getCertificatesAndAwards());
        assertNotNull(result.getCompanyDescription());
        assertNotNull(result.getOffice());
        assertNotNull(result.getRecruitmentEmail());
        assertNotNull(result.getScopeOperations());
        assertNotNull(result.getTechnologiesAndTools());

    }

    @Test
    public void testUpdateBusinessCard_Success() {
        // Given
        BusinessCard updateBusinessCard = new BusinessCard(); // Stwórz obiekt BusinessCard do testów
        updateBusinessCard.setId(1);
        updateBusinessCard.setOffice("New Office");
        updateBusinessCard.setScopeOperations("New Scope Operations");
        updateBusinessCard.setRecruitmentEmail("new@example.com");
        updateBusinessCard.setPhoneNumber("123456789");
        updateBusinessCard.setCompanyDescription("New Company Description");
        updateBusinessCard.setTechnologiesAndTools("New Technologies and Tools");
        updateBusinessCard.setCertificatesAndAwards("New Certificates and Awards");

        BusinessCardEntity businessCardEntity = new BusinessCardEntity(); // Mockuj Entity
        when(businessCardMapper.map(updateBusinessCard)).thenReturn(businessCardEntity);
        when(businessCardJpaRepository.save(businessCardEntity)).thenReturn(businessCardEntity);
        when(businessCardMapper.map(businessCardEntity)).thenReturn(updateBusinessCard);

        // When
        BusinessCard updatedCard = businessCardRepository.updateBusinessCard(updateBusinessCard);

        // Then
        assertEquals(updateBusinessCard.getId(), updatedCard.getId());
        assertEquals(updateBusinessCard.getOffice(), updatedCard.getOffice());
        assertEquals(updateBusinessCard.getScopeOperations(), updatedCard.getScopeOperations());
        assertEquals(updateBusinessCard.getRecruitmentEmail(), updatedCard.getRecruitmentEmail());
        assertEquals(updateBusinessCard.getPhoneNumber(), updatedCard.getPhoneNumber());
        assertEquals(updateBusinessCard.getCompanyDescription(), updatedCard.getCompanyDescription());
        assertEquals(updateBusinessCard.getTechnologiesAndTools(), updatedCard.getTechnologiesAndTools());
        assertEquals(updateBusinessCard.getCertificatesAndAwards(), updatedCard.getCertificatesAndAwards());
    }




}