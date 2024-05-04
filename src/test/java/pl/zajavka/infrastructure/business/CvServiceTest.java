package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.infrastructure.business.dao.CvDAO;
import pl.zajavka.infrastructure.business.dao.NotificationDAO;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.CvFixtures;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CvServiceTest extends AbstractIT {

    @Mock
    private CvDAO cvDAO;
    @Mock
    NotificationDAO notificationDAO;
    @Mock
    private CvMapperDTO cvMapperDTO;
    @InjectMocks
    private CvService cvService;
    @Test
    void createCV_Success() {
        // Given
        CV cv = new CV();
        User user = new User();

        // Konfiguracja mocka cvDAO
        when(cvDAO.createCV(cv, user)).thenReturn(cv);

        // When
        CV result = cvService.createCV(cv, user);

        // Then
        assertEquals(cv, result, "Returned CV should match the created one");
        verify(cvDAO, times(1)).createCV(cv, user);
    }

    @Test
    void createCV_NullCV() {
        // Given
        User user = new User();

        // When
        CV result = cvService.createCV(null, user);

        // Then
        assertEquals(null, result, "Returned CV should be null");
    }
    @Test
    void updateCV_Success() {
        // Given
        CV updatedCv = new CV();

        // When
        cvService.updateCV(updatedCv);

        // Then
        verify(cvDAO, times(1)).updateCV(updatedCv);
    }

    @Test
    void updateCV_CVNull() {
        // Given
        CV updatedCv = null;

        // When / Then
        assertThrows(NullPointerException.class, () -> cvService.updateCV(updatedCv),
                "Updating null CV should throw NullPointerException");
        verify(cvDAO, never()).updateCV(any());
    }

    @Test
    void deleteCVAndSetNullInNotifications_Success() {
        // Given
        Integer cvId = 1;
        CV cv = new CV();
        cv.setId(cvId);

        // Mockowanie zwracanych wartości przez metody DAO
        when(cvDAO.findById(cvId)).thenReturn(cv);

        // When
        cvService.deleteCVAndSetNullInNotifications(cvId);

        // Then

        // Sprawdzamy, czy metoda findById została wywołana z odpowiednim argumentem
        verify(cvDAO, times(1)).findById(cvId);
        // Sprawdzamy, czy metoda findByCvIdToDelete została wywołana z odpowiednim argumentem
        verify(notificationDAO, times(1)).findByCvIdToDelete(cvId);
        // Sprawdzamy, czy metoda deleteById została wywołana z odpowiednim argumentem
        verify(cvDAO, times(1)).deleteById(cvId);


    }

    @Test
    void existByUser_CVExists() {
        // Given
        User loggedInUser = new User();

        // Konfiguracja mocka cvDAO
        when(cvDAO.existByUser(loggedInUser)).thenReturn(true);

        // When
        boolean exists = cvService.existByUser(loggedInUser);

        // Then
        assertTrue(exists, "CV should exist for the logged-in user");
        verify(cvDAO, times(1)).existByUser(loggedInUser);
    }

    @Test
    void existByUser_CVDoesNotExist() {
        // Given
        User loggedInUser = new User();

        // Konfiguracja mocka cvDAO
        when(cvDAO.existByUser(loggedInUser)).thenReturn(false);

        // When
        boolean exists = cvService.existByUser(loggedInUser);

        // Then
        assertFalse(exists, "CV should not exist for the logged-in user");
        verify(cvDAO, times(1)).existByUser(loggedInUser);
    }

    @Test
    void findByUser_CVExists() {
        // Given
        User loggedInUser = new User();
        CV expectedCV = new CV();

        // Konfiguracja mocka cvDAO
        when(cvDAO.findByUser(loggedInUser)).thenReturn(expectedCV);

        // When
        CV result = cvService.findByUser(loggedInUser);

        // Then
        assertNotNull(result, "CV should exist for the logged-in user");
        assertEquals(expectedCV, result, "Returned CV should match the expected CV");
        verify(cvDAO, times(1)).findByUser(loggedInUser);
    }

    @Test
    void findByUser_CVDoesNotExist() {
        // Given
        User loggedInUser = new User();

        // Konfiguracja mocka cvDAO
        when(cvDAO.findByUser(loggedInUser)).thenReturn(null);

        // When
        CV result = cvService.findByUser(loggedInUser);

        // Then
        assertNull(result, "CV should not exist for the logged-in user");
        verify(cvDAO, times(1)).findByUser(loggedInUser);
    }

    @Test
    void findById_CVExists() {
        // Given
        Integer cvId = 1;
        CV expectedCV = new CV();

        // Konfiguracja mocka cvDAO
        when(cvDAO.findById(cvId)).thenReturn(expectedCV);

        // When
        CV result = cvService.findById(cvId);

        // Then
        assertNotNull(result, "CV should exist for the given ID");
        assertEquals(expectedCV, result, "Returned CV should match the expected CV");
        verify(cvDAO, times(1)).findById(cvId);
    }

    @Test
    void findById_CVDoesNotExist() {
        // Given
        Integer cvId = 1;

        // Konfiguracja mocka cvDAO
        when(cvDAO.findById(cvId)).thenReturn(null);

        // When
        CV result = cvService.findById(cvId);

        // Then
        assertNull(result, "CV should not exist for the given ID");
        verify(cvDAO, times(1)).findById(cvId);
    }

    @Test
    void searchCvByKeywordAndCategory_ResultNotEmpty() {
        // Given
        String keyword = "Java";
        String category = "Programming";
        List<CV> cvList = new ArrayList<>();
        cvList.add(new CV());
        cvList.add(new CV());

        // Konfiguracja mocka cvDAO
        when(cvDAO.searchCvByKeywordAndCategory(keyword, category)).thenReturn(cvList);
        when(cvMapperDTO.map(any(CV.class))).thenReturn(CvFixtures.someCvDTO());

        // When
        List<CvDTO> result = cvService.searchCvByKeywordAndCategory(keyword, category);

        // Then
        assertFalse(result.isEmpty(), "Result should not be empty");
        assertEquals(2, result.size(), "Result size should be 2");
        verify(cvDAO, times(1)).searchCvByKeywordAndCategory(keyword, category);

    }

    @Test
    void searchCvByKeywordAndCategory_ResultEmpty() {
        // Given
        String keyword = "Java";
        String category = "Programming";

        // Konfiguracja mocka cvDAO
        when(cvDAO.searchCvByKeywordAndCategory(keyword, category)).thenReturn(new ArrayList<>());

        // When
        List<CvDTO> result = cvService.searchCvByKeywordAndCategory(keyword, category);

        // Then
        assertTrue(result.isEmpty(), "Result should be empty");
        verify(cvDAO, times(1)).searchCvByKeywordAndCategory(keyword, category);
        verifyNoInteractions(cvMapperDTO);
    }

    @Test
    void findByUserOpt_CVExists() {
        // Given
        User user = new User();
        CV cv = new CV();

        // Konfiguracja mocka cvDAO
        when(cvDAO.findByUserOpt(user)).thenReturn(Optional.of(cv));

        // When
        Optional<CV> result = cvService.findByUserOpt(user);

        // Then
        assertTrue(result.isPresent(), "CV should exist for the given user");
        assertEquals(cv, result.get(), "Returned CV should match the expected CV");
        verify(cvDAO, times(1)).findByUserOpt(user);
    }

    @Test
    void findByUserOpt_CVDoesNotExist() {
        // Given
        User user = new User();

        // Konfiguracja mocka cvDAO
        when(cvDAO.findByUserOpt(user)).thenReturn(Optional.empty());

        // When
        Optional<CV> result = cvService.findByUserOpt(user);

        // Then
        assertFalse(result.isPresent(), "CV should not exist for the given user");
        verify(cvDAO, times(1)).findByUserOpt(user);
    }


}
