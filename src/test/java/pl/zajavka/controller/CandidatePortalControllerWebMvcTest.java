package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.PaginationService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = CandidatePortalController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CandidatePortalControllerWebMvcTest {


    @MockBean
    private UserService userService;
    @MockBean
    private JobOfferService jobOfferService;
    @MockBean
    private JobOfferMapperDTO jobOfferMapperDTO;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private PaginationService paginationService;


    MockMvc mockMvc;


    @Test
    public void testGetCandidatePortalPage() throws Exception {
        // given
        User user = UserFixtures.someUser1();
        List<JobOffer> jobOffers = new ArrayList<>();
        jobOffers.add(JobOfferFixtures.someJobOffer3());
        Page<JobOffer> jobOfferPage = new PageImpl<>(jobOffers);
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        notificationDTOs.add(new NotificationDTO());

        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("adam112");
        // Mock service behavior
        when(userService.findByUserName("adam112")).thenReturn(user);
        // Mock paginationService behavior
        when(paginationService.findAllJobOffersForPage(any(Pageable.class))).thenReturn(jobOfferPage);
        // Mock jobOfferMapperDTO behavior
        when(jobOfferMapperDTO.map(any(JobOffer.class))).thenReturn(new JobOfferDTO());
        // Mock notificationService behavior
        when(notificationService.findLatestByUser(user)).thenReturn(notificationDTOs);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/candidate_portal").principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("candidate_portal"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("jobOfferDTOs"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("currentJobOfferPage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("totalJobOfferPages"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("totalJobOfferItems"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("notificationDTOs"));

        // Verify that userService.findByUserName() was called with correct username
        Mockito.verify(userService).findByUserName("adam112");
    }

    @Test
    public void testGetAllNotifications() throws Exception {
        // given
        User user = UserFixtures.someUser1();
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        notificationDTOs.add(new NotificationDTO());

        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("adam112");
        // Mock service behavior
        when(userService.findByUserName("adam112")).thenReturn(user);
        // Mock notificationService behavior
        when(notificationService.findByUser(user)).thenReturn(notificationDTOs);
        // Mock paginationService behavior
        Page<NotificationDTO> notificationDTOsPage = new PageImpl<>(notificationDTOs);
        when(paginationService.createNotificationPage(notificationDTOs, PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"))))
                .thenReturn(notificationDTOsPage);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/candidateNotifications").principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("candidate_notifications"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("notificationDTOs"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("currentNotificationPage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("totalNotificationPages"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("totalNotificationItems"));

        // Verify that userService.findByUserName() was called with correct username
        Mockito.verify(userService).findByUserName("adam112");
    }



    @Test
    void searchJobOffersTest() throws Exception {
        // Given
        String keyword = "Java Developer";
        String category = "position";

        List<JobOffer> jobOffers = List.of();
        // Zmieniona część: Zamockowanie metody mapowania JobOffer na JobOfferDTO
        Mockito.when(jobOfferMapperDTO.map(Mockito.any(JobOffer.class)))
                .thenReturn(JobOfferFixtures.someJobOffer3DTO());
        // Stubowanie metody searchJobOffersByKeywordAndCategory
        Mockito.when(jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category))
                .thenReturn(jobOffers);

        // When, Then
        mockMvc.perform(get("/searchJobOffers")
                        .param("keyword", keyword)
                        .param("category", category))
                .andExpect(status().isOk()) // Oczekuj statusu OK
                .andExpect(view().name("search_job_offers_results")) // Oczekuj widoku "search_job_offers_results"
                .andExpect(model().attributeExists("searchResultsDTO")) // Oczekuj atrybutu "searchResultsDTO" w modelu
                .andExpect(model().attributeExists("keyword")) // Oczekuj atrybutu "keyword" w modelu
                .andExpect(model().attributeExists("category")); // Oczekuj atrybutu "category" w modelu
    }



    @Test
    void searchJobOffersTest_WithInvalidKeywordBySalaryMin() throws Exception {
        // Given
        String keyword = "invalidKeyword";
        String category = "salaryMin";

        // Stubowanie metody searchJobOffersByKeywordAndCategory zwracającej wyjątek
        Mockito.when(jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category))
                .thenThrow(new NumberFormatException());

        // When, Then
        mockMvc.perform(get("/searchJobOffers")
                        .param("keyword", keyword)
                        .param("category", category))
                .andExpect(status().isOk()) // Oczekuj statusu OK
                .andExpect(view().name("error")); // Oczekuj widoku "error_page"
    }

    @Test
    void searchJobOffersTest_WithCorrectKeywordBySalaryMin() throws Exception {
        // Given
        String keyword = "5000";
        String category = "salaryMin";
        List<JobOffer> jobOffers = List.of();

        // Stubowanie metody searchJobOffersByKeywordAndCategory zwracającej wyjątek
        Mockito.when(jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category))
                .thenReturn(jobOffers);

        // When, Then
        mockMvc.perform(get("/searchJobOffers")
                        .param("keyword", keyword)
                        .param("category", category))
                .andExpect(status().isOk()) // Oczekuj statusu OK
                .andExpect(view().name("search_job_offers_results")); // Oczekuj widoku "error_page"
    }

}
//    @Test
//    void sendCVTest() {
//        // Given
//        Integer jobOfferId = 1;
//        Authentication authentication = mock(Authentication.class);
//        HttpSession httpSession = mock(HttpSession.class);
//        User loggedInUser = mock(User.class);
//        Mockito.when(userService.getLoggedInUser(authentication)).thenReturn(loggedInUser);
//        Mockito.when()
//
//        // When
//        String result = candidatePortalController.sendCV(jobOfferId, mock(Model.class), authentication, httpSession);
//
//        // Then
//        assertEquals("cv_send_successfully", result);
//        // Sprawdź, czy metoda getLoggedInUser została wywołana raz z poprawnym argumentem
//        Mockito.verify(userService, times(1)).getLoggedInUser(authentication);
//    }

































