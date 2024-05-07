package pl.zajavka.infrastructure.business;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.infrastructure.business.dao.JobOfferDAO;
import pl.zajavka.infrastructure.business.dao.NotificationDAO;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.JobOfferFixtures;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class JobOfferServiceTest extends AbstractIT {

    @Mock
    private JobOfferDAO jobOfferDAO;
    @Mock
    private NotificationDAO notificationDAO;
    @InjectMocks
    JobOfferService jobOfferService;

    @Before
    public void setUp() {
        jobOfferDAO = mock(JobOfferDAO.class);
        notificationDAO = mock(NotificationDAO.class);
        jobOfferService = new JobOfferService(jobOfferDAO, notificationDAO);
    }

    @Test
    public void testCreateJobOffer() {
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        User user = new User();

        when(jobOfferDAO.create(jobOffer, user)).thenReturn(jobOffer);

        JobOffer result = jobOfferService.create(jobOffer, user);

        assertEquals(jobOffer, result);
    }

    @Test
    public void testUpdateJobOffer() {
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();

        when(jobOfferDAO.updateJobOffer(jobOffer)).thenReturn(jobOffer);

        JobOffer result = jobOfferService.updateJobOffer(jobOffer);

        assertEquals(jobOffer, result);
    }

    @Test
    public void testDeleteJobOfferAndSetNullInNotifications() {
        Integer jobOfferId = 123;
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        when(jobOfferDAO.findById(jobOfferId)).thenReturn(jobOffer);

        jobOfferService.deleteJobOfferAndSetNullInNotifications(jobOfferId);

        verify(notificationDAO).findListByJobOfferIdToDelete(jobOffer.getId());
        verify(jobOfferDAO).deleteById(jobOfferId);
    }

    @Test
    public void testSearchJobOffersByKeywordAndCategory() {
        String keyword = "Java";
        String category = "Programming";

        List<JobOffer> expected = new ArrayList<>();
        when(jobOfferDAO.searchJobOffersByKeywordAndCategory(keyword, category)).thenReturn(expected);

        List<JobOffer> result = jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category);

        assertEquals(expected, result);
    }

    @Test
    public void testSearchJobOffersBySalary() {
        String category = "Programming";
        BigDecimal salary = BigDecimal.valueOf(50000);

        List<JobOffer> expected = new ArrayList<>();
        when(jobOfferDAO.searchJobOffersBySalary(category, salary)).thenReturn(expected);

        List<JobOffer> result = jobOfferService.searchJobOffersBySalary(category, salary);

        assertEquals(expected, result);
    }

    @Test
    public void testSaveJobOffer() {
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();

        when(jobOfferDAO.saveJobOffer(jobOffer)).thenReturn(jobOffer);

        JobOffer result = jobOfferService.saveJobOffer(jobOffer);

        assertEquals(jobOffer, result);
    }

    @Test
    public void testFindById() {
        Integer jobOfferId = 123;
        JobOffer expected = JobOfferFixtures.someJobOffer3();
        when(jobOfferDAO.findById(jobOfferId)).thenReturn(expected);

        JobOffer result = jobOfferService.findById(jobOfferId);

        assertEquals(expected, result);
    }

    @Test
    public void testFindListByUser() {
        User loggedInUser = new User();

        List<JobOffer> expected = new ArrayList<>();
        when(jobOfferDAO.findListByUser(loggedInUser)).thenReturn(expected);

        List<JobOffer> result = jobOfferService.findListByUser(loggedInUser);

        assertEquals(expected, result);
    }

    @Test
    public void testFindByUser() {
        User loggedInUser = new User();
        Optional<JobOffer> expected = Optional.of(JobOfferFixtures.someJobOffer3());
        when(jobOfferDAO.findByUser(loggedInUser)).thenReturn(expected);

        Optional<JobOffer> result = jobOfferService.findByUser(loggedInUser);

        assertEquals(expected, result);
    }



}
