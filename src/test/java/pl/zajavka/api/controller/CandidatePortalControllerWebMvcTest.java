package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.hamcrest.Matchers;
import pl.zajavka.api.dto.JobOfferDTO;
import pl.zajavka.api.dto.JobOffersDTO;
import pl.zajavka.api.dto.NotificationDTO;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.api.dto.mapper.NotificationMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.*;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.Notification;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.NotificationFixtures;
import pl.zajavka.util.UserFixtures;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Slf4j
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
    void getCandidatePortalPage() throws Exception {
        // Given
        User loggedInUser = UserFixtures.someUser1();
        loggedInUser.setId(1);
        Mockito.when(userService.getLoggedInUser(Mockito.any())).thenReturn(loggedInUser);

        UserDTO userDTO = UserFixtures.someUserDTO1();
        Mockito.when(userMapperDTO.map(loggedInUser)).thenReturn(userDTO);

        Page<JobOfferDTO> jobOfferDTOsPage = createJobOfferPage(); // Utwórz stronę z ofertami pracy
        Mockito.when(jobOfferService.findAllJobOffersForPage(Mockito.any())).thenAnswer(invocation -> jobOfferDTOsPage);

        List<NotificationDTO> notificationDTOs = NotificationFixtures.sampleNotificationDTOList(); // Utwórz listę notyfikacji
        Mockito.when(notificationService.findByUser(Mockito.any())).thenReturn(createNotificationList());

        mockMvc.perform(get(CandidatePortalController.CANDIDATE_PORTAL))
                .andExpect(status().isOk()) // Oczekuj statusu OK
                .andExpect(view().name("candidate_portal")) // Oczekuj widoku "candidate_portal"
                .andExpect(model().attributeExists("userDTO")) // Oczekuj atrybutu "userDTO" w modelu
                .andExpect(model().attributeExists("jobOfferDTOs")) // Oczekuj atrybutu "jobOfferDTOs" w modelu
                .andExpect(model().attributeExists("currentJobOfferPage")) // Oczekuj atrybutu "currentJobOfferPage" w modelu
                .andExpect(model().attributeExists("totalJobOfferPages")); // Oczekuj atrybutu "totalJobOfferPages" w modelu
    }
//    }
@Test
void searchJobOffersTest() throws Exception {
    // Given
    String keyword = "Java Developer";
    String category = "position";

    // Dane testowe dla JobOffer
    List<JobOffer> jobOffers = JobOfferFixtures.jobOffers();

    // Zmieniona część: Zamockowanie metody mapowania JobOffer na JobOfferDTO
    Mockito.when(jobOfferMapperDTO.map(Mockito.any(JobOffer.class))).thenAnswer(invocation -> {
        JobOffer jobOffer = invocation.getArgument(0);
        log.info("a to co: " + jobOffer);
        // Tutaj możemy zwrócić przykładowy obiekt JobOfferDTO na podstawie jobOffer
        return new JobOfferDTO(
                jobOffer.getId(),
                jobOffer.getCompanyName(),
                jobOffer.getPosition(),
                jobOffer.getResponsibilities(),
                jobOffer.getRequiredTechnologies(),
                jobOffer.getBenefits(),
                jobOffer.getJobOfferDateTime(),
                jobOffer.getUser()

        );

    });

    // Stubowanie metody searchJobOffersByKeywordAndCategory
    Mockito.when(jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category))
            .thenReturn(jobOffers);

    // When, Then
    mockMvc.perform(post("/searchJobOffers")
                    .param("keyword", keyword)
                    .param("category", category))
            .andExpect(status().isOk()) // Oczekuj statusu OK
            .andExpect(view().name("search_job_offers_results")) // Oczekuj widoku "search_job_offers_results"
            .andExpect(model().attributeExists("searchResultsDTO")) // Oczekuj atrybutu "searchResultsDTO" w modelu
            .andExpect(model().attributeExists("keyword")) // Oczekuj atrybutu "keyword" w modelu
            .andExpect(model().attributeExists("category")); // Oczekuj atrybutu "category" w modelu
}

    // Metoda pomocnicza do tworzenia strony z ofertami pracy
    private Page<JobOfferDTO> createJobOfferPage() {
        List<JobOfferDTO> jobOfferDTOList = new ArrayList<>();
        // Tutaj dodaj jakieś oferty pracy do listy
        return new PageImpl<>(jobOfferDTOList);
    }

    // Metoda pomocnicza do tworzenia strony z ofertami pracy
    private List<Notification> createNotificationList() {
        List<Notification> notifications = new ArrayList<>();
        return notifications;
    }

    private Page<NotificationDTO> createNotificationPage() {
        List<NotificationDTO> notificationDTOList = NotificationFixtures.sampleNotificationDTOList();
        // Tutaj dodaj jakieś oferty pracy do listy
        return new PageImpl<>(notificationDTOList);
    }



//    @Test
//    void getCandidatePortalPage() throws Exception {
//        // Given
//        UserDTO userDTO = UserFixtures.someUserDTO1(); // Utwórz użytkownika DTO
//        Page<JobOfferDTO> jobOfferDTOPage = createJobOfferPage(); // Utwórz stronę z ofertami pracy
//
//        // Kiedy userService.getLoggedInUser(authentication) zostanie wywołane, zwróć userDTO
//        when(userService.getLoggedInUser(any())).thenReturn(null);
//
//        // Kiedy jobOfferService.findAllJobOffersForPage(pageable) zostanie wywołane, zwróć jobOfferDTOPage
//        when(jobOfferService.findAllJobOffersForPage(any(Pageable.class))).thenReturn(null);
//
//        // Kiedy notificationService.findByUser(loggedInUser) zostanie wywołane, zwróć pustą listę
//        when(notificationService.findByUser(any())).thenReturn(new ArrayList<>());
//
//        // When, Then
//        mockMvc.perform(get(CANDIDATE_PORTAL))
//                .andExpect(status().isOk()) // Oczekuj statusu OK
//                .andExpect(view().name("candidate_portal")) // Oczekuj widoku "candidate_portal"
//                .andExpect(model().attributeExists("userDTO")) // Oczekuj atrybutu "userDTO" w modelu
//                .andExpect(model().attributeExists("jobOfferDTOs")) // Oczekuj atrybutu "jobOfferDTOs" w modelu
//                .andExpect(model().attributeExists("currentJobOfferPage")) // Oczekuj atrybutu "currentJobOfferPage" w modelu
//                .andExpect(model().attributeExists("totalJobOfferPages")) // Oczekuj atrybutu "totalJobOfferPages" w modelu
//                .andExpect(model().attributeExists("totalJobOfferItems")) // Oczekuj atrybutu "totalJobOfferItems" w modelu
//                .andExpect(model().attributeExists("notificationDTOs")) // Oczekuj atrybutu "notificationDTOs" w modelu
//                .andExpect(model().attributeExists("currentNotificationPage")) // Oczekuj atrybutu "currentNotificationPage" w modelu
//                .andExpect(model().attributeExists("totalNotificationPages")) // Oczekuj atrybutu "totalNotificationPages" w modelu
//                .andExpect(model().attributeExists("totalNotificationItems")); // Oczekuj atrybutu "totalNotificationItems" w modelu
//    }

    // Metoda pomocnicza do tworzenia strony z ofertami pracy
//    private Page<JobOfferDTO> createJobOfferPage() {
//        List<JobOfferDTO> jobOfferDTOList = new ArrayList<>();
//        // Tutaj dodaj jakieś oferty pracy do listy
//        return new PageImpl<>(jobOfferDTOList);
//    }


    // Utility method to create a list of notification DTOs
//    private List<NotificationDTO> mockNotificationDTOList() {
//        // Implement as needed
//        return null;




    }


