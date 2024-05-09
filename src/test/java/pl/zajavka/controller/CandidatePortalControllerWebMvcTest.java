package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.controller.dto.mapper.NotificationMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;

import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.business.*;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;

import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.NotificationFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.ArrayList;
import java.util.Collections;
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

    //    @MockBean
//    private HttpSession httpSession;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapperDTO userMapperDTO;
    @MockBean
    private JobOfferService jobOfferService;
    @MockBean
    private JobOfferMapperDTO jobOfferMapperDTO;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private NotificationMapperDTO notificationMapperDTO;
    @MockBean
    private CvService cvService;
    @MockBean
    private PaginationService paginationService;

    @MockBean
    private JobOfferMapper jobOfferMapper;

    MockMvc mockMvc;



    @Test
    public void testGetCandidatePortalPage() throws Exception {
        // Mock data
        User user = UserFixtures.someUser1();


        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("adam112");

        // Mock service behavior
        when(userService.findByUserName("adam112")).thenReturn(user);



        // Mock paginationService behavior
        List<JobOffer> jobOffers = new ArrayList<>();
        // Add some sample JobOffer objects to the list
        jobOffers.add(JobOfferFixtures.someJobOffer3());


        Page<JobOffer> jobOfferPage = new PageImpl<>(jobOffers);
        when(paginationService.findAllJobOffersForPage(any(Pageable.class))).thenReturn(jobOfferPage);

        // Mock jobOfferMapperDTO behavior
        when(jobOfferMapperDTO.map(any(JobOffer.class))).thenReturn(new JobOfferDTO());


        // Mock notificationService behavior
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        notificationDTOs.add(new NotificationDTO());

        when(notificationService.findLatestByUser(user)).thenReturn(notificationDTOs);

        // Perform GET request
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







//    @Test
//    void getCandidatePortalPage() throws Exception {
//        // Given
//        User loggedInUser = UserFixtures.someUser1();
//        loggedInUser.setId(1);
//        Mockito.when(userService.findByUserName(Mockito.any())).thenReturn(loggedInUser);
//
//        UserDTO userDTO = UserFixtures.someUserDTO1();
//        Mockito.when(userMapperDTO.map(loggedInUser)).thenReturn(userDTO);
//
//
//        Page<JobOfferDTO> jobOfferDTOsPage = createJobOfferPage(); // Utwórz stronę z ofertami pracy
//        Mockito.when(paginationService.findAllJobOffersForPage(Mockito.any())).thenAnswer(invocation -> jobOfferDTOsPage);
//
//        List<NotificationDTO> notificationDTOs = NotificationFixtures.sampleNotificationDTOList(); // Utwórz listę notyfikacji
//        Mockito.when(notificationService.findByUser(Mockito.any())).thenReturn(createNotificationList());
//
//        mockMvc.perform(get(CandidatePortalController.CANDIDATE_PORTAL))
//                .andExpect(status().isOk()) // Oczekuj statusu OK
//                .andExpect(view().name("candidate_portal")) // Oczekuj widoku "candidate_portal"
//                .andExpect(model().attributeExists("userDTO")) // Oczekuj atrybutu "userDTO" w modelu
//                .andExpect(model().attributeExists("jobOfferDTOs")) // Oczekuj atrybutu "jobOfferDTOs" w modelu
//                .andExpect(model().attributeExists("currentJobOfferPage")) // Oczekuj atrybutu "currentJobOfferPage" w modelu
//                .andExpect(model().attributeExists("totalJobOfferPages")); // Oczekuj atrybutu "totalJobOfferPages" w modelu
//    }
//    }
}

//@Test
//void searchJobOffersTest() throws Exception {
//    // Given
//    String keyword = "Java Developer";
//    String category = "position";
//
//    List<JobOffer> jobOffers = List.of();
//    // Zmieniona część: Zamockowanie metody mapowania JobOffer na JobOfferDTO
//    Mockito.when(jobOfferMapperDTO.map(Mockito.any(JobOffer.class)))
//            .thenReturn(JobOfferFixtures.someJobOfferDTO());
//    // Stubowanie metody searchJobOffersByKeywordAndCategory
//    Mockito.when(jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category))
//            .thenReturn(jobOffers);
//
//    // When, Then
//    mockMvc.perform(get("/searchJobOffers")
//                    .param("keyword", keyword)
//                    .param("category", category))
//            .andExpect(status().isOk()) // Oczekuj statusu OK
//            .andExpect(view().name("search_job_offers_results")) // Oczekuj widoku "search_job_offers_results"
//            .andExpect(model().attributeExists("searchResultsDTO")) // Oczekuj atrybutu "searchResultsDTO" w modelu
//            .andExpect(model().attributeExists("keyword")) // Oczekuj atrybutu "keyword" w modelu
//            .andExpect(model().attributeExists("category")); // Oczekuj atrybutu "category" w modelu
//}

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

































