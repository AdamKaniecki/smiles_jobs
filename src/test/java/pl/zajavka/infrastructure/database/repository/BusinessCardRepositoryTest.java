package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.repository.jpa.BusinessCardJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.BusinessCardMapper;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static wiremock.com.google.common.base.Verify.verify;

public class BusinessCardRepositoryTest extends AbstractIT {

    @Mock
    private BusinessCardJpaRepository businessCardJpaRepository;
    @Mock
    private BusinessCardMapper businessCardMapper;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    BusinessCardRepository businessCardRepository;


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
}