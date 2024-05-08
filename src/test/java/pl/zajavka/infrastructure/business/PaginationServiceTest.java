package pl.zajavka.infrastructure.business;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.jpa.CvJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.JobOfferJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.integration.AbstractIT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class PaginationServiceTest extends AbstractIT {

    @Mock
    private  JobOfferJpaRepository jobOfferRepository;
    @Mock
    private  CvJpaRepository cvRepository;
    @Mock
    private  JobOfferMapper jobOfferMapper;
    @Mock
    private  CvMapper cvMapper;
    @InjectMocks
    private PaginationService paginationService;

    @Test
    public void testCreateNotificationPage_WhenDataAvailableAndWithinPaginationRange() {
        // Arrange
        List<NotificationDTO> notifications = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            notifications.add(new NotificationDTO());
        }
        Pageable pageable = PageRequest.of(0, 5);

        // Act
        Page<NotificationDTO> result = paginationService.createNotificationPage(notifications, pageable);

        // Assert
        assertEquals(5, result.getSize());
        assertEquals(10, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
    }

    @Test
    public void testCreateNotificationPage_EmptyList() {
        // Arrange
        List<NotificationDTO> notifications = Collections.emptyList();
        Pageable pageable = PageRequest.of(0, 10);

        // Act
        Page<NotificationDTO> result = paginationService.createNotificationPage(notifications, pageable);

        // Assert
        assertTrue(result.isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }



    @Test
    public void testFindAll_WhenDataAvailable() {
        // Arrange
        List<CvEntity> cvEntities = new ArrayList<>();
        for (int i = 0; i < 5; i++) { // zmiana z 10 na 5
            cvEntities.add(new CvEntity());
        }
        Pageable pageable = PageRequest.of(0, 5);
        Page<CvEntity> cvEntitiesPage = new PageImpl<>(cvEntities);
        when(cvRepository.findAll(pageable)).thenReturn(cvEntitiesPage);

        // Act
        Page<CV> result = paginationService.findAll(pageable);

        // Assert
        assertEquals(5, result.getSize());
        assertEquals(5, result.getTotalElements()); // zmiana z 10 na 5
        assertEquals(1, result.getTotalPages()); // zmiana z 2 na 1
    }

    @Test
    public void testFindAllJobOffersForPage_WhenDataAvailable() {
        // Arrange
        List<JobOfferEntity> jobOfferEntities = new ArrayList<>();
        jobOfferEntities.add(new JobOfferEntity());
        jobOfferEntities.add(new JobOfferEntity());

        Pageable pageable = PageRequest.of(0, 2);
        Page<JobOfferEntity> jobOfferEntitiesPage = new PageImpl<>(jobOfferEntities);
        when(jobOfferRepository.findAll(pageable)).thenReturn(jobOfferEntitiesPage);

        // Act
        Page<JobOffer> result = paginationService.findAllJobOffersForPage(pageable);

        // Assert
        assertEquals(2, result.getSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
    }
}
