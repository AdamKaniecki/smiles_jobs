package pl.zajavka.api.controller.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pl.zajavka.controller.api.CvRestController;
import pl.zajavka.controller.api.JobOfferRestController;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.JobOfferFixtures;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = JobOfferRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JobOfferRestControllerWebMvcTest {

    @MockBean
    private UserService userService;

    @MockBean
    private JobOfferMapperDTO jobOfferMapperDTO;

    @MockBean
    private JobOfferService jobOfferService;

    @MockBean
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();  // Inicjalizacja ObjectMapper przed użyciem
    }

    private MockMvc mockMvc;

    @Test
    void testCreateJobOfferSuccess() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User loggedInUser = new User();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer1();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferService.create(jobOffer, loggedInUser)).thenReturn(jobOffer);
        when(jobOfferMapperDTO.map(any(JobOfferDTO.class))).thenReturn(jobOffer);

        JobOfferDTO jobOfferDTO = new JobOfferDTO();
        String jsonRequest = new ObjectMapper().writeValueAsString(jobOfferDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/createJobOffer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isCreated())
                .andExpect(content().string("Job offer created successfully"));
    }

    @Test
    void testCreateJobOfferUserUnauthorized() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(null);

        JobOfferDTO jobOfferDTO = new JobOfferDTO();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/createJobOffer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobOfferDTO))
                        .principal(authentication))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User not authenticated"));
    }


    @Test
    void testCreateJobOfferUserNotFound() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(null);

        JobOfferDTO jobOfferDTO = new JobOfferDTO();
        // Ustaw pola jobOfferDTO

        mockMvc.perform(MockMvcRequestBuilders.post("/api/createJobOffer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobOfferDTO))
                        .principal(authentication))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }


    @Test
    void testShowMyJobOffersSuccess() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User user = new User();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        JobOfferDTO jobOfferDTO = new JobOfferDTO();


        when(userService.findByUserName(username)).thenReturn(user);
        when(jobOfferService.findListByUser(user)).thenReturn(List.of(jobOffer));
        when(jobOfferMapperDTO.map(jobOffer)).thenReturn(jobOfferDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ShowMyJobOffers")
                        .principal(authentication))
                .andExpect(status().isOk());
    }

    @Test
    void testShowMyJobOffersNotFound() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ShowMyJobOffers")
                        .principal(authentication))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Job offers Not Found"));
    }

    @Test
    void testShowJobOffersSuccess() throws Exception {
        Integer jobOfferId = 1;
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        JobOfferDTO jobOfferDTO = new JobOfferDTO();
        when(jobOfferService.findById(jobOfferId)).thenReturn(jobOffer);
        when(jobOfferMapperDTO.map(jobOffer)).thenReturn(jobOfferDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/showJobOffer/{id}", jobOfferId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    @Test
    void testShowJobOffersNotFound() throws Exception {
        Integer jobOfferId = 1;
        when(jobOfferService.findById(jobOfferId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/showJobOffer/{id}", jobOfferId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteJobOfferSuccess() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.getName()).thenReturn(username);
        User loggedInUser = new User();
        Integer jobOfferId = 1;

        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        jobOffer.setId(jobOfferId);
        jobOffer.setUser(loggedInUser); // loggedInUser is the owner

        // Mockowanie zachowań
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferService.findById(jobOfferId)).thenReturn(jobOffer);

        // Wykonanie testu
        mockMvc.perform(delete("/api/deleteJobOffer/{id}", jobOfferId)
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("JobOffer deleted successfully"));

        // Weryfikacja
        verify(jobOfferService, times(1)).deleteJobOfferAndSetNullInNotifications(jobOfferId);
    }
    @Test
    public void testDeleteJobOfferInternalServerError() throws Exception {
        // Przygotowanie danych
        Integer jobOfferId = 1;
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);

        when(authentication.getName()).thenReturn(username);

        User loggedInUser = new User();
        loggedInUser.setUserName(username);

        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        jobOffer.setId(jobOfferId);
        jobOffer.setUser(loggedInUser); // loggedInUser is the owner

        // Mockowanie zachowań
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferService.findById(jobOfferId)).thenReturn(jobOffer);
        doThrow(new RuntimeException("Unexpected error")).when(jobOfferService).deleteJobOfferAndSetNullInNotifications(jobOfferId);

        // Wykonanie testu
        mockMvc.perform(delete("/api/deleteJobOffer/{id}", jobOfferId)
                        .principal(authentication))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while deleting job offer"));

        // Weryfikacja
        verify(jobOfferService, times(1)).deleteJobOfferAndSetNullInNotifications(jobOfferId);
    }

    @Test
    public void testDeleteJobOfferNotFound() throws Exception {
        // Przygotowanie danych

        Integer jobOfferId = 1;
        String username = "testuser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);


        User loggedInUser = new User();

        // Mockowanie zachowań
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferService.findById(jobOfferId)).thenReturn(null);

        // Wykonanie testu
        mockMvc.perform(delete("/api/deleteJobOffer/{id}", jobOfferId)
                        .principal(authentication))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Job Offer not found"));

        // Weryfikacja
        verify(jobOfferService, times(0)).deleteJobOfferAndSetNullInNotifications(jobOfferId);
    }

    @Test
    public void testDeleteJobOfferForbidden() throws Exception {
        // Przygotowanie danych
        Integer jobOfferId = 1;
        String username = "testuser";
        User loggedInUser = new User();
        loggedInUser.setUserName(username);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User differentUser = new User();
        differentUser.setUserName("differentUser");

        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        jobOffer.setId(jobOfferId);
        jobOffer.setUser(differentUser); // loggedInUser is not the owner

        // Mockowanie zachowań
        when(userService.findByUserName(username)).thenReturn(null);
        when(jobOfferService.findById(jobOfferId)).thenReturn(jobOffer);

        // Wykonanie testu
        mockMvc.perform(delete("/api/deleteJobOffer/{id}", jobOfferId)
                        .principal(authentication))
                .andExpect(status().isForbidden())
                .andExpect(content().string("You are not authorized to delete this job offer"));

        // Weryfikacja
        verify(jobOfferService, times(0)).deleteJobOfferAndSetNullInNotifications(jobOfferId);
    }
    @Test
    void testUpdateJobOfferSuccess() throws Exception {
        String username = "testUser";
        User loggedInUser = new User();
        loggedInUser.setUserName(username);
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        JobOfferDTO  jobOfferDTO = new JobOfferDTO();
        jobOfferDTO.setCompanyName("New Name");
        jobOfferDTO.setPosition("New Surname");
        jobOfferDTO.setResponsibilities("fg");
        jobOfferDTO.setRequiredTechnologies("fgh");
        jobOfferDTO.setExperience("ghj");
        jobOfferDTO.setJobLocation("hjk");
        jobOfferDTO.setTypeOfContract("jik");
        jobOfferDTO.setTypeOfWork("iku");
        jobOfferDTO.setSalaryMin(new BigDecimal("4000"));
        jobOfferDTO.setSalaryMax(new BigDecimal("8000"));
        jobOfferDTO.setRequiredLanguage("kok");
        jobOfferDTO.setRequiredLanguageLevel("kol");
        jobOfferDTO.setBenefits("kol");
        jobOfferDTO.setJobDescription("kol");
//        jobOfferDTO.setJobOfferDateTime(OffsetDateTime.now());
//        jobOfferDTO.setActive(();
//        jobOfferDTO.setNeededStaff(();
//        jobOfferDTO.setHiredCount(();

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferService.findById(jobOfferDTO.getId())).thenReturn(jobOffer);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/updateJobOffer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(jobOfferDTO))
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("Job offer updated successfully"));
    }



}




