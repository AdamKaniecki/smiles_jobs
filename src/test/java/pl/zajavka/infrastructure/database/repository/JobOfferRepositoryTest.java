package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.jpa.JobOfferJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.UserFixtures;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class JobOfferRepositoryTest extends AbstractIT {

    @InjectMocks
    private JobOfferRepository jobOfferRepository;

    @Mock
    private JobOfferJpaRepository jobOfferJpaRepository;

    @Mock
    private JobOfferMapper jobOfferMapper;

    @Mock
    private UserMapper userMapper;


    @Test
    void testFindById() {
        // given
        Integer id = 1;
        JobOfferEntity jobOfferEntity = JobOfferFixtures.someJobOfferEntity1();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer1();

        when(jobOfferJpaRepository.findById(id)).thenReturn(Optional.of(jobOfferEntity));
        when(jobOfferMapper.map(jobOfferEntity)).thenReturn(jobOffer);

        // when
        JobOffer result = jobOfferRepository.findById(id);

        // then
        assertNotNull(result); // Sprawdza, czy zwrócony wynik nie jest null
        assertEquals(jobOffer, result); // Sprawdza, czy zwrócony wynik jest równy oczekiwanemu obiektowi JobOffer
        verify(jobOfferJpaRepository, times(1)).findById(id); // Sprawdza, czy metoda findById została wywołana dokładnie raz z odpowiednim argumentem
        verify(jobOfferMapper, times(1)).map(jobOfferEntity); // Sprawdza, czy metoda map z JobOfferMapper została wywołana dokładnie raz z odpowiednim argumentem
    }

    @Test
    void testFindById_EntityNotFoundException() {
        // given
        Integer id = 1;

        when(jobOfferJpaRepository.findById(id)).thenReturn(Optional.empty());

        // when
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> jobOfferRepository.findById(id));

        // then
        assertNotNull(exception);
        assertEquals("Not found JobOffer with ID: " + id, exception.getMessage());
        verify(jobOfferJpaRepository, times(1)).findById(id);
        verifyNoMoreInteractions(jobOfferMapper);
    }

    @Test
    void testFindListByUser() {
        // given
        User user = UserFixtures.someUser2();
        List<JobOfferEntity> jobOfferEntityList = List.of(JobOfferFixtures.someJobOfferEntity1(), JobOfferFixtures.someJobOfferEntity1());
        List<JobOffer> jobOfferList = List.of(JobOfferFixtures.someJobOffer1(), JobOfferFixtures.someJobOffer2());

        when(userMapper.map(user)).thenReturn(UserFixtures.someUserEntity2());
        when(jobOfferJpaRepository.findListByUser(any())).thenReturn(jobOfferEntityList);
        when(jobOfferMapper.map(jobOfferEntityList)).thenReturn(jobOfferList);

        // when
        List<JobOffer> result = jobOfferRepository.findListByUser(user);

        // then
        assertNotNull(result);
        assertEquals(jobOfferList, result);
        verify(userMapper, times(1)).map(user);
        verify(jobOfferJpaRepository, times(1)).findListByUser(any());
        verify(jobOfferMapper, times(1)).map(jobOfferEntityList);
    }

    @Test
    void testFindByUser() {
        // given
        User user = UserFixtures.someUser2();
        JobOfferEntity jobOfferEntity = JobOfferFixtures.someJobOfferEntity1();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer1();

        when(userMapper.map(user)).thenReturn(UserFixtures.someUserEntity2());
        when(jobOfferJpaRepository.findByUser(any())).thenReturn(Optional.of(jobOfferEntity));
        when(jobOfferMapper.map(jobOfferEntity)).thenReturn(jobOffer);

        // when
        Optional<JobOffer> result = jobOfferRepository.findByUser(user);

        // then
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(jobOffer, result.get());
        verify(userMapper, times(1)).map(user);
        verify(jobOfferJpaRepository, times(1)).findByUser(any());
        verify(jobOfferMapper, times(1)).map(jobOfferEntity);
    }

    @Test
    void testFindByUser_NotFound() {
        // given
        User user = new User();

        when(userMapper.map(user)).thenReturn(new UserEntity());
        when(jobOfferJpaRepository.findByUser(any())).thenReturn(Optional.empty());

        // when
        Optional<JobOffer> result = jobOfferRepository.findByUser(user);

        // then
        assertNotNull(result);
        assertFalse(result.isPresent());
        verify(userMapper, times(1)).map(user);
        verify(jobOfferJpaRepository, times(1)).findByUser(any());
        verifyNoMoreInteractions(jobOfferMapper);
    }

    @Test
    void testSaveJobOffer() {
        // given
        JobOfferEntity jobOfferEntity = JobOfferFixtures.someJobOfferEntity1();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer1();

        when(jobOfferMapper.map(jobOffer)).thenReturn(jobOfferEntity);
        when(jobOfferJpaRepository.save(jobOfferEntity)).thenReturn(jobOfferEntity);
        when(jobOfferMapper.map(jobOfferEntity)).thenReturn(jobOffer);

        // when
        JobOffer result = jobOfferRepository.saveJobOffer(jobOffer);

        // then
        assertNotNull(result);
        assertEquals(jobOffer, result);
        verify(jobOfferMapper, times(1)).map(jobOffer);
        verify(jobOfferJpaRepository, times(1)).save(jobOfferEntity);
        verify(jobOfferMapper, times(1)).map(jobOfferEntity);
    }

    @Test
    void createJobOffer_ReturnsValidJobOffer() {
        // Given
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3(); // Utwórz obiekt JobOffer
        User user = new User(); // Utwórz obiekt User

        JobOfferEntity jobOfferEntity = new JobOfferEntity(); // Utwórz obiekt JobOfferEntity
        jobOfferEntity.setId(1); // Ustaw identyfikator
        // Ustaw pozostałe właściwości dla jobOfferEntity

        when(jobOfferMapper.map(any(JobOfferEntity.class))).thenReturn(jobOffer); // Kiedy mapper zostanie wywołany, zwróć obiekt jobOffer

        // When
        JobOffer createdJobOffer = jobOfferRepository.create(jobOffer, user); // Wywołaj metodę create

        // Then
        assertNotNull(createdJobOffer); // Upewnij się, że zwrócony jobOffer nie jest nullem
        // Tutaj możesz dodać więcej asercji, aby sprawdzić, czy wszystkie właściwości są poprawnie ustawione
    }


    @Test
    void updateJobOffer_WithValidJobOffer_ReturnsUpdatedJobOffer() {
        // Given
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3(); // Utwórz obiekt JobOffer
        jobOffer.setId(1); // Ustaw identyfikator
        // Ustaw inne właściwości jobOffer

        JobOfferEntity jobOfferEntity = new JobOfferEntity(); // Utwórz obiekt JobOfferEntity
        jobOfferEntity.setId(1); // Ustaw identyfikator
        // Ustaw inne właściwości jobOfferEntity

        when(jobOfferJpaRepository.findById(1)).thenReturn(Optional.of(jobOfferEntity)); // Kiedy findById jest wywoływane z 1L, zwróć jobOfferEntity
        when(jobOfferJpaRepository.save(any(JobOfferEntity.class))).thenReturn(jobOfferEntity); // Kiedy save jest wywoływane z dowolnym JobOfferEntity, zwróć ten sam obiekt
        when(jobOfferMapper.map(jobOfferEntity)).thenReturn(jobOffer); // Kiedy map jest wywoływane z jobOfferEntity, zwróć jobOffer

        // When
        JobOffer updatedJobOffer = jobOfferRepository.updateJobOffer(jobOffer); // Wywołaj metodę updateJobOffer

        // Then
        assertNotNull(updatedJobOffer); // Upewnij się, że zwrócony jobOffer nie jest nullem
        assertEquals(jobOffer, updatedJobOffer); // Sprawdź, czy zwrócony jobOffer jest równy oryginalnemu obiektowi jobOffer
        verify(jobOfferJpaRepository, times(1)).findById(1); // Sprawdź, czy metoda findById została wywołana raz z odpowiednim argumentem
        verify(jobOfferJpaRepository, times(1)).save(jobOfferEntity); // Sprawdź, czy metoda save została wywołana raz z odpowiednim argumentem
        verify(jobOfferMapper, times(1)).map(jobOfferEntity); // Sprawdź, czy metoda map z JobOfferMapper została wywołana raz z odpowiednim argumentem
    }

    @Test
    void updateJobOffer_WithNullId_ThrowsEntityNotFoundException() {
        // Given
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();// Utwórz obiekt JobOffer bez ustawionego identyfikatora
        jobOffer.setId(null);
        // When, Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            jobOfferRepository.updateJobOffer(jobOffer); // Wywołaj metodę updateJobOffer, powinien rzucić wyjątek EntityNotFoundException
        });

        // Sprawdź treść komunikatu wyjątku
        assertEquals("JobOffer ID cannot be null", exception.getMessage());
        // Upewnij się, że nie było interakcji z żadnymi mockami
        verifyNoInteractions(jobOfferJpaRepository);
        verifyNoInteractions(jobOfferMapper);
    }

    @Test
    void deleteById_WithValidJobOfferId_CallsDeleteByIdOnce() {
        // Given
        Integer jobOfferId = 1; // Ustaw identyfikator oferty pracy

        // When
        jobOfferRepository.deleteById(jobOfferId); // Wywołaj metodę deleteById

        // Then
        verify(jobOfferJpaRepository, times(1)).deleteById(jobOfferId); // Sprawdź, czy metoda deleteById została wywołana raz z odpowiednim argumentem
    }

}

