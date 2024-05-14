package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zajavka.controller.dto.BusinessCardDTO;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.BusinessCardService;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.BusinessCardFixtures;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = JobOfferController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JobOfferControllerWebMvcTest {
    @MockBean
    private JobOfferService jobOfferService;
    @MockBean
    private UserService userService;
    @MockBean
    private UserMapperDTO userMapperDTO;
    @MockBean
    private JobOfferMapperDTO jobOfferMapperDTO;
    @MockBean
    private BusinessCardService businessCardService;
    @MockBean
    private BusinessCardMapperDTO businessCardMapperDTO;
    MockMvc mockMvc;

    @Test
    public void testJobOfferForm_ReturnsCreateJobOfferView() throws Exception {
        String username = "adam122222";
        User loggedInUser = UserFixtures.someUser2();
        Authentication authentication = Mockito.mock(Authentication.class);
        JobOfferDTO jobOfferDTO = JobOfferFixtures.someJobOffer3DTO();

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);

        MockHttpServletRequestBuilder requestBuilder = get("/JobOfferForm")
                .principal(authentication)
                .flashAttr("jobOfferDTO", jobOfferDTO);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("create_job_offer"))
                .andExpect(model().attributeExists("jobOfferDTO"));
    }


    @Test
    public void testCreateJobOffer_ReturnsJobOfferCreatedSuccessfullyView() throws Exception {
        String username = "adam122222";
        User loggedInUser = UserFixtures.someUser2();
        Authentication authentication = Mockito.mock(Authentication.class);
        JobOfferDTO jobOfferDTO = JobOfferFixtures.someJobOffer3DTO();

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);

        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferMapperDTO.map(jobOfferDTO)).thenReturn(jobOffer);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/createJobOffer")
                .principal(authentication)
                .flashAttr("jobOfferDTO", jobOfferDTO);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("job_offer_created_successfully"))
                .andExpect(model().attributeExists("jobOfferDTO"));

        // Optionally, you can also verify if the jobOfferService.create method was called with the correct parameters
        verify(jobOfferService, times(1)).create(jobOffer, loggedInUser);
    }

    @Test
    public void testShowJobOfferDetails_JobOfferFound_ReturnsJobOfferDetailsView() throws Exception {
        // Given
        Integer jobOfferId = 1;
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        JobOfferDTO jobOfferDTO = JobOfferFixtures.someJobOffer3DTO();
        BusinessCard businessCard = BusinessCardFixtures.someBusinessCard();
        BusinessCardDTO businessCardDTO = new BusinessCardDTO();

        when(jobOfferService.findById(jobOfferId)).thenReturn(jobOffer);
        when(jobOfferMapperDTO.map(jobOffer)).thenReturn(jobOfferDTO);
        when(businessCardService.findByUser(jobOffer.getUser())).thenReturn(businessCard);
        when(businessCardMapperDTO.map(businessCard)).thenReturn(businessCardDTO);

        MockHttpServletRequestBuilder requestBuilder = get("/jobOffer/{jobOfferId}", jobOfferId);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("jobOfferDTO"))
                .andExpect(model().attributeExists("businessCardDTO"))
                .andExpect(view().name("job_offer_details"));
    }

    @Test
    public void testShowJobOfferDetails_BusinessCardNotFound_ReturnsJobOfferDetailsViewWithoutBusinessCard() throws Exception {
        // Given
        Integer jobOfferId = 1;
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        JobOfferDTO jobOfferDTO = JobOfferFixtures.someJobOffer3DTO();
        BusinessCard businessCard = null;

        when(jobOfferService.findById(jobOfferId)).thenReturn(jobOffer);
        when(jobOfferMapperDTO.map(jobOffer)).thenReturn(jobOfferDTO);
        when(businessCardService.findByUser(jobOffer.getUser())).thenReturn(businessCard);

        MockHttpServletRequestBuilder requestBuilder = get("/jobOffer/{jobOfferId}", jobOfferId);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("jobOfferDTO"))
                .andExpect(model().attribute("businessCardDTO", Matchers.instanceOf(BusinessCardDTO.class)))
                .andExpect(view().name("job_offer_details"));
    }

@Test
public void testShowMyJobOffers_LoggedInUser_ReturnsJobOffersSuccessfully() throws Exception {
    // Given
    String username = "john_doe";
    User loggedInUser = UserFixtures.someUser1();
    List<JobOffer> jobOffers = List.of(JobOfferFixtures.someJobOffer3());
    List<JobOfferDTO> jobOffersDTO = List.of(JobOfferFixtures.someJobOffer3DTO());

    when(jobOfferMapperDTO.map(any(JobOffer.class))).thenAnswer(invocation -> {
        JobOffer jobOffer = invocation.getArgument(0);
        return jobOffersDTO.stream()
                .filter(dto -> dto.getId().equals(jobOffer.getId())) // zakładając, że DTO i oryginalna oferta mają te same identyfikatory
                .findFirst()
                .orElse(null); // W rzeczywistości ta linia powinna być zaimplementowana w sposób odpowiadający logice mapowania
    });


    // Mockowanie autentykacji i serwisu użytkownika
    Authentication authentication = Mockito.mock(Authentication.class);
    when(authentication.getName()).thenReturn(username);
    when(userService.findByUserName(username)).thenReturn(loggedInUser);
    when(jobOfferService.findListByUser(loggedInUser)).thenReturn(jobOffers);

    // When, Then
    mockMvc.perform(get("/showMyJobOffers").principal(authentication))
            .andExpect(status().isOk())
            .andExpect(view().name("show_my_job_offers"))
            .andExpect(model().attributeExists("jobOffersDTO"));
}

    @Test
    public void testShowMyJobOffers_NotLoggedInUser_RedirectsToLogin() throws Exception {
        // Given
        String username = "anonymousUser";
        User loggedInUser = null;

        // Mockowanie autentykacji i serwisu użytkownika
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);

        // When, Then
        mockMvc.perform(get("/showMyJobOffers").principal(authentication))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testUpdateMyJobOffer_ReturnsUpdateForm() throws Exception {
        // Given
        int jobId = 1;
        JobOffer jobOffer = JobOfferFixtures.someJobOffer1();
        User user = jobOffer.getUser();

        JobOfferDTO jobOfferDTO = JobOfferFixtures.someJobOffer3DTO();
        UserDTO userDTO = UserFixtures.someUserDTO1();

        // Mockowanie serwisu ofert pracy
        when(jobOfferService.findById(jobId)).thenReturn(jobOffer);

        // Mockowanie mapowania oferty pracy na DTO
        when(jobOfferMapperDTO.map(jobOffer)).thenReturn(jobOfferDTO);

        // Mockowanie mapowania użytkownika na DTO
        when(userMapperDTO.map(user)).thenReturn(userDTO);

        // When, Then
        mockMvc.perform(get("/updateJobOfferForm").param("id", String.valueOf(jobId)))
                .andExpect(status().isOk())
                .andExpect(view().name("update_job_offer_form"))
                .andExpect(model().attributeExists("jobOfferDTO"))
                .andExpect(model().attributeExists("userDTO"));
    }



    @Test
    public void testUpdateJobOffer_ReturnsUpdateSuccessView() throws Exception {
        // Given
        JobOfferDTO updateJobOfferDTO = JobOfferFixtures.someJobOffer3DTO();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer1();

        // Mockowanie serwisu ofert pracy
        when(jobOfferService.findById(updateJobOfferDTO.getId())).thenReturn(jobOffer);

        // Mockowanie mapowania DTO na ofertę pracy
        when(jobOfferMapperDTO.map(jobOffer)).thenReturn(updateJobOfferDTO);

        // When, Then
        mockMvc.perform(put("/updateJobOfferDone").flashAttr("jobOfferDTO", updateJobOfferDTO))
                .andExpect(status().isOk())
                .andExpect(view().name("job_offer_update_successfully"))
                .andExpect(model().attributeExists("jobOfferDTO"));

        // Sprawdzenie czy serwis został wywołany z odpowiednim obiektem oferty pracy
        verify(jobOfferService, times(1)).updateJobOffer(jobOffer);
    }

}