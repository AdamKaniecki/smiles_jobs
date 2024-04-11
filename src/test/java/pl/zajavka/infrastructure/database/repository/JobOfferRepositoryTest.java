package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.jpa.JobOfferJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.UserFixtures;

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
        List<JobOfferEntity> jobOfferEntityList = List.of(JobOfferFixtures.someJobOfferEntity1(),JobOfferFixtures.someJobOfferEntity1());
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

}
