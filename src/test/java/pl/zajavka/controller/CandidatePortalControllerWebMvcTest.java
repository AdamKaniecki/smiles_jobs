//package pl.zajavka.controller;
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.test.web.servlet.MockMvc;
//import pl.zajavka.controller.dto.JobOfferDTO;
//import pl.zajavka.controller.dto.NotificationDTO;
//import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
//import pl.zajavka.controller.dto.mapper.NotificationMapperDTO;
//import pl.zajavka.controller.dto.mapper.UserMapperDTO;
//
//import pl.zajavka.infrastructure.domain.JobOffer;
//import pl.zajavka.infrastructure.domain.Notification;
//import pl.zajavka.infrastructure.business.*;
//import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
//
//import pl.zajavka.util.NotificationFixtures;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//@Slf4j
//@WebMvcTest(controllers = CandidatePortalController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@AllArgsConstructor(onConstructor = @__(@Autowired))
//public class CandidatePortalControllerWebMvcTest {
//
////    @MockBean
////    private HttpSession httpSession;
//    @MockBean
//    private UserService userService;
//    @MockBean
//    private UserMapperDTO userMapperDTO;
//    @MockBean
//    private JobOfferService jobOfferService;
//    @MockBean
//    private JobOfferMapperDTO jobOfferMapperDTO;
//    @MockBean
//    private NotificationService notificationService;
//    @MockBean
//    private NotificationMapperDTO notificationMapperDTO;
//    @MockBean
//    private CvService cvService;
//    @MockBean
//    private PaginationService paginationService;
//
//    @MockBean
//    private JobOfferMapper jobOfferMapper;
//
//    MockMvc mockMvc;
//
////    @Test
////    void getCandidatePortalPage() throws Exception {
////        // Given
////        User loggedInUser = UserFixtures.someUser1();
////        loggedInUser.setId(1);
////        Mockito.when(userService.getLoggedInUser(Mockito.any())).thenReturn(loggedInUser);
////
////        UserDTO userDTO = UserFixtures.someUserDTO1();
////        Mockito.when(userMapperDTO.map(loggedInUser)).thenReturn(userDTO);
////
////        Page<JobOfferDTO> jobOfferDTOsPage = createJobOfferPage(); // Utwórz stronę z ofertami pracy
////        Mockito.when(jobOfferService.findAllJobOffersForPage(Mockito.any())).thenAnswer(invocation -> jobOfferDTOsPage);
////
////        List<NotificationDTO> notificationDTOs = NotificationFixtures.sampleNotificationDTOList(); // Utwórz listę notyfikacji
////        Mockito.when(notificationService.findByUser(Mockito.any())).thenReturn(createNotificationList());
////
////        mockMvc.perform(get(CandidatePortalController.CANDIDATE_PORTAL))
////                .andExpect(status().isOk()) // Oczekuj statusu OK
////                .andExpect(view().name("candidate_portal")) // Oczekuj widoku "candidate_portal"
////                .andExpect(model().attributeExists("userDTO")) // Oczekuj atrybutu "userDTO" w modelu
////                .andExpect(model().attributeExists("jobOfferDTOs")) // Oczekuj atrybutu "jobOfferDTOs" w modelu
////                .andExpect(model().attributeExists("currentJobOfferPage")) // Oczekuj atrybutu "currentJobOfferPage" w modelu
////                .andExpect(model().attributeExists("totalJobOfferPages")); // Oczekuj atrybutu "totalJobOfferPages" w modelu
////    }
////    }
//@Test
//void searchJobOffersTest() throws Exception {
//    // Given
//    String keyword = "Java Developer";
//    String category = "position";
//
//    List<JobOffer> jobOffers = JobOfferFixtures.jobOffers();
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
//
////    @Test
////    void sendCVTest() {
////        // Given
////        Integer jobOfferId = 1;
////        Authentication authentication = mock(Authentication.class);
////        HttpSession httpSession = mock(HttpSession.class);
////        User loggedInUser = mock(User.class);
////        Mockito.when(userService.getLoggedInUser(authentication)).thenReturn(loggedInUser);
////        Mockito.when()
////
////        // When
////        String result = candidatePortalController.sendCV(jobOfferId, mock(Model.class), authentication, httpSession);
////
////        // Then
////        assertEquals("cv_send_successfully", result);
////        // Sprawdź, czy metoda getLoggedInUser została wywołana raz z poprawnym argumentem
////        Mockito.verify(userService, times(1)).getLoggedInUser(authentication);
////    }
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//    // Metoda pomocnicza do tworzenia strony z ofertami pracy
//    private Page<JobOfferDTO> createJobOfferPage() {
//        List<JobOfferDTO> jobOfferDTOList = new ArrayList<>();
//        // Tutaj dodaj jakieś oferty pracy do listy
//        return new PageImpl<>(jobOfferDTOList);
//    }
//
//    // Metoda pomocnicza do tworzenia strony z ofertami pracy
//    private List<Notification> createNotificationList() {
//        List<Notification> notifications = new ArrayList<>();
//        return notifications;
//    }
//
//    private Page<NotificationDTO> createNotificationPage() {
//        List<NotificationDTO> notificationDTOList = NotificationFixtures.sampleNotificationDTOList();
//        // Tutaj dodaj jakieś oferty pracy do listy
//        return new PageImpl<>(notificationDTOList);
//    }
//
//
//
//
//
//    }
//
//
