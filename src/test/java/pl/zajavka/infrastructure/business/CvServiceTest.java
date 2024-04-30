package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.infrastructure.business.dao.CvDAO;
import pl.zajavka.infrastructure.business.dao.NotificationDAO;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.integration.AbstractIT;

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


}
