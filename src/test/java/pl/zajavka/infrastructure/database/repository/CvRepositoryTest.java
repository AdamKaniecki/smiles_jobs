package pl.zajavka.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.repository.jpa.CvJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.AddressMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class CvRepositoryTest extends AbstractIT {


    @InjectMocks
    private CvRepository cvRepository;
    @Mock
    private CvJpaRepository cvJpaRepository;
    @Mock
    private CvMapper cvMapper;
    @Mock
    private UserMapper userMapper;

    @Mock
    private AddressMapper addressMapper;


    @Test
    void testFindAll() {
        // given
        CvEntity cvEntity1 = CvFixtures.someCvEntity1();
        CvEntity cvEntity2 = CvFixtures.someCvEntity2();

        // Mockowanie zachowania cvJpaRepository.findAll()
        when(cvJpaRepository.findAll()).thenReturn(List.of(cvEntity1, cvEntity2));


        CV cv1 = CvFixtures.someCv1();
        CV cv2 = CvFixtures.someCv2();
        // Mockowanie mapowania cvEntity na CV
        when(cvMapper.map(cvEntity1)).thenReturn(cv1);
        when(cvMapper.map(cvEntity2)).thenReturn(cv2);

        //when
        List<CV> cvList = cvRepository.findAll();

        // then

        // 1. Sprawdzenie czy lista zawiera oczekiwane obiekty
        assertThat(cvList).containsExactly(cv1, cv2);

        // 2. Sprawdzenie czy lista zawiera dokładnie dwa elementy
        assertThat(cvList).hasSize(2);

        // 3. Sprawdzenie czy lista nie jest pusta
        assertThat(cvList).isNotEmpty();

        // 4. Sprawdzenie czy cvRepository.findAll() zostało wywołane dokładnie raz
        verify(cvJpaRepository, times(1)).findAll();

        // 5. Sprawdzenie czy cvMapper.map(cvEntity1) zostało wywołane dokładnie raz
        verify(cvMapper, times(1)).map(cvEntity1);

        // 6. Sprawdzenie czy cvMapper.map(cvEntity2) zostało wywołane dokładnie raz
        verify(cvMapper, times(1)).map(cvEntity2);

        // 7. Sprawdzenie czy lista zawiera określony element na określonej pozycji
        assertThat(cvList.get(0)).isEqualTo(cv1);
    }

    @Test
    void testExistByUser_WhenExists() {
        // given
        User loggedInUser = UserFixtures.someUser1();
        // Zakładam, że użytkownik istnieje w bazie danych
        when(cvJpaRepository.existsByUser(userMapper.map(loggedInUser))).thenReturn(true);

        // when
        boolean exists = cvRepository.existByUser(loggedInUser);

        // then
        assertTrue(exists);
        // Sprawdzenie, czy metoda existsByUser została poprawnie wywołana na mocku CvJpaRepository
        verify(cvJpaRepository, times(1)).existsByUser(any());
        // Sprawdzenie, czy nie było innych wywołań na mocku
        verifyNoMoreInteractions(cvJpaRepository);
    }

    @Test
    void testExistByUser_WhenNotExists() {
        // given
        User loggedInUser = new User();
        // Zakładamy, że użytkownik nie istnieje w bazie danych
        when(cvJpaRepository.existsByUser(userMapper.map(loggedInUser))).thenReturn(false);

        // when
        boolean exists = cvRepository.existByUser(loggedInUser);

        // then
        assertFalse(exists);
        // Sprawdzenie, czy metoda existsByUser została poprawnie wywołana na mocku CvJpaRepository
        verify(cvJpaRepository, times(1)).existsByUser(any());
        // Sprawdzenie, czy nie było innych wywołań na mocku
        verifyNoMoreInteractions(cvJpaRepository);
    }

    @Test
    void testFindByUser_WhenExists() {
        // given
        User user = UserFixtures.someUser1();
        CvEntity cvEntity = CvFixtures.someCvEntity1();
        CV expectedCV = CvFixtures.someCv1();
        // Zakładam, że encja CV dla użytkownika istnieje w bazie danych
        when(cvJpaRepository.findByUser(any())).thenReturn(Optional.of(cvEntity));
        // Mockowanie mapowania CvEntity na CV
        when(cvMapper.map(cvEntity)).thenReturn(expectedCV);

        // when
        CV resultCV = cvRepository.findByUser(user);

        // then

        assertNotNull(resultCV); // Sprawdza czy zwrócone CV nie jest null
        assertSame(expectedCV, resultCV); // Sprawdza czy zwrócone CV to oczekiwane CV
        assertEquals(expectedCV, resultCV); // Sprawdza czy zwrócone CV jest równe oczekiwanemu CV
        assertNotSame(user, resultCV.getUser()); // Sprawdza czy użytkownik w zwróconym CV nie jest tym samym obiektem co użytkownik podany jako argument
        assertEquals(user, resultCV.getUser()); // Sprawdza czy użytkownik w zwróconym CV jest tym samym obiektem co użytkownik podany jako argument
        verify(cvJpaRepository, times(1)).findByUser(any()); // Sprawdza czy metoda findByUser została wywołana dokładnie raz
        verifyNoMoreInteractions(cvJpaRepository); // Sprawdza czy nie było innych wywołań na mocku CvJpaRepository
    }


    @Test
    void testFindByUser_WhenNotExists() {
        // given
        User user = new User();
        // Zakładamy, że encja CV dla użytkownika nie istnieje w bazie danych
        when(cvJpaRepository.findByUser(any())).thenReturn(Optional.empty());

        // when
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> cvRepository.findByUser(user));

        // then
        assertNotNull(exception); // 1. Sprawdzenie, czy rzucany jest wyjątek EntityNotFoundException
        assertEquals("CV not found for the user", exception.getMessage()); // 2. Sprawdzenie, czy wiadomość wyjątku jest poprawna
        verify(cvJpaRepository, times(1)).findByUser(any()); // 3. Sprawdzenie, czy wywołanie metody findByUser na mocku CvJpaRepository wystąpiło dokładnie raz
        verifyNoMoreInteractions(cvJpaRepository); // 4. Sprawdzenie, czy nie było żadnych dodatkowych wywołań na mocku CvJpaRepository
    }


    @Test
    void testSearchCvByKeywordAndCategory() {
        // given
        String keyword = "Java";
        String category = "programmingLanguage";

        CvEntity cvEntity1 = CvFixtures.someCvEntity1();
        CvEntity cvEntity2 = CvFixtures.someCvEntity2();

        // Zakładamy, że istnieją encje CV pasujące do kryteriów wyszukiwania
        when(cvJpaRepository.findCvByKeywordAndCategory(keyword, category))
                .thenReturn(List.of(cvEntity1, cvEntity2));

        CV cv1 = CvFixtures.someCv1();
        CV cv2 = CvFixtures.someCv2();

        // Mockowanie mapowania cvEntity na CV
        when(cvMapper.map(cvEntity1)).thenReturn(cv1);
        when(cvMapper.map(cvEntity2)).thenReturn(cv2);

        // when
        List<CV> cvList = cvRepository.searchCvByKeywordAndCategory(keyword, category);

        // then
        assertNotNull(cvList); // 1. Sprawdzenie, czy lista CV nie jest null
        assertEquals(2, cvList.size()); // 2. Sprawdzenie, czy lista zawiera 2 elementy (odpowiednio zmapowane CV)
        assertTrue(cvList.contains(cv1)); // 3. Sprawdzenie, czy lista zawiera pierwsze zmapowane CV
        assertTrue(cvList.contains(cv2)); // 4. Sprawdzenie, czy lista zawiera drugie zmapowane CV
        assertSame(cv1, cvList.get(0)); // 5. Sprawdzenie, czy pierwszy element listy to dokładnie pierwsze zmapowane CV
        assertSame(cv2, cvList.get(1)); // 6. Sprawdzenie, czy drugi element listy to dokładnie drugie zmapowane CV
        assertNotSame(cvList.get(0), cvList.get(1)); // 7. Sprawdzenie, czy pierwsze i drugie zmapowane CV są różne
        assertFalse(cvList.isEmpty()); // 8. Sprawdzenie, czy lista CV nie jest pusta
        assertDoesNotThrow(() -> cvList.forEach(cv -> assertNotNull(cv))); // 9. Sprawdzenie, czy każde CV w liście nie jest null

    }

    @Test
    void testSaveCV() {
        // given
        CV cv = CvFixtures.someCv1();
        CvEntity cvEntity = CvFixtures.someCvEntity1();

        // when
        when(cvMapper.map(cv)).thenReturn(cvEntity);
        cvRepository.saveCV(cv);

        // then
        verify(cvMapper).map(cv); // Sprawdzenie, czy metoda map z CvMapper została wywołana
        verify(cvJpaRepository).save(cvEntity); // Sprawdzenie, czy metoda save z CvJpaRepository została wywołana z odpowiednim argumentem
    }

    @Test
    void testDeleteById_WhenExists() {
        // given
        Integer id = 1;

        // when
        cvRepository.deleteById(id);

        // then
        verify(cvJpaRepository).deleteById(id); // Sprawdzenie, czy metoda deleteById z CvJpaRepository została wywołana z odpowiednim argumentem
    }

    @Test
    void testDeleteById_WhenNotExists() {
        // given
        Integer id = 1;

        // Mockowanie zachowania cvJpaRepository.deleteById() w przypadku, gdy rzucany jest wyjątek EntityNotFoundException
        doThrow(new EntityNotFoundException("CV not found for id: " + id))
                .when(cvJpaRepository).deleteById(id);

        // when
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> cvRepository.deleteById(id));

        // then
        assertNotNull(exception); // Sprawdzenie, czy rzucany jest wyjątek EntityNotFoundException
        assertEquals("CV not found for id: " + id, exception.getMessage()); // Sprawdzenie, czy wiadomość wyjątku jest poprawna
        verify(cvJpaRepository).deleteById(id); // Sprawdzenie, czy metoda deleteById z CvJpaRepository została wywołana z odpowiednim argumentem
    }

    @Test
    void testUpdateCV_WhenExists() {
        // given
        CV updateCV = CvFixtures.someCv1();
        CvEntity cvEntity = CvFixtures.someCvEntity1();

        // when
        when(cvMapper.map(updateCV)).thenReturn(cvEntity);
        cvRepository.updateCV(updateCV);

        // then
        verify(cvMapper).map(updateCV); // 1. Sprawdzenie, czy metoda map z CvMapper została wywołana z odpowiednim argumentem
        assertEquals(updateCV.getName(), cvEntity.getName()); // 2. Sprawdzenie, czy pole name encji CV zostało ustawione na odpowiednią wartość z obiektu CV
        assertEquals(updateCV.getSurname(), cvEntity.getSurname()); // 3. Sprawdzenie, czy pole surname encji CV zostało ustawione na odpowiednią wartość z obiektu CV
        assertEquals(updateCV.getDateOfBirth(), cvEntity.getDateOfBirth()); // 4. Sprawdzenie, czy pole dateOfBirth encji CV zostało ustawione na odpowiednią wartość z obiektu CV
        assertEquals(updateCV.getSex(), cvEntity.getSex()); // 5. Sprawdzenie, czy pole sex encji CV zostało ustawione na odpowiednią wartość z obiektu CV
        assertEquals(updateCV.getMaritalStatus(), cvEntity.getMaritalStatus()); // 6. Sprawdzenie, czy pole maritalStatus encji CV zostało ustawione na odpowiednią wartość z obiektu CV
        assertEquals(updateCV.getContactEmail(), cvEntity.getContactEmail()); // 7. Sprawdzenie, czy pole contactEmail encji CV zostało ustawione na odpowiednią wartość z obiektu CV
        assertEquals(updateCV.getPhoneNumber(), cvEntity.getPhoneNumber()); // 8. Sprawdzenie, czy pole phoneNumber encji CV zostało ustawione na odpowiednią wartość z obiektu CV
        assertEquals(updateCV.getEducation(), cvEntity.getEducation()); // 9. Sprawdzenie, czy pole education encji CV zostało ustawione na odpowiednią wartość z obiektu CV
        assertEquals(updateCV.getWorkExperience(), cvEntity.getWorkExperience()); // 10. Sprawdzenie, czy pole workExperience encji CV zostało ustawione na odpowiednią wartość z obiektu CV
    }

    @Test
    void testCreateCV() {
        CV createdCV = CvFixtures.someCv1();
        User user = createdCV.getUser();

        // when
        when(cvMapper.map(any(CvEntity.class))).thenReturn(createdCV);
        // when
        CV result = cvRepository.createCV(createdCV, user);

        // then
        assertNotNull(result); // Sprawdzenie, czy utworzony obiekt CV nie jest null

        // Sprawdzenie, czy pola obiektu CV nie są null
        assertNotNull(result.getName());
        assertNotNull(result.getSurname());
        assertNotNull(result.getDateOfBirth());
        assertNotNull(result.getSex());
        assertNotNull(result.getMaritalStatus());
        assertNotNull(result.getContactEmail());
        assertNotNull(result.getPhoneNumber());
        assertNotNull(result.getEducation());
        assertNotNull(result.getWorkExperience());
        assertNotNull(result.getSocialMediaProfil());
        assertNotNull(result.getProjects());
        assertNotNull(result.getAboutMe());
        assertNotNull(result.getCertificatesOfCourses());
        assertNotNull(result.getProgrammingLanguage());
        assertNotNull(result.getSkillsAndTools());
        assertNotNull(result.getLanguage());
        assertNotNull(result.getLanguageLevel());
        assertNotNull(result.getHobby());
        assertNotNull(result.getFollowPosition());

    }


}


