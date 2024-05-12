package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.BusinessCardService;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.UserFixtures;

import static org.mockito.Mockito.*;

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

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/JobOfferForm")
                .principal(authentication)
                .flashAttr("jobOfferDTO", jobOfferDTO);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create_job_offer"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("jobOfferDTO"));
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
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("job_offer_created_successfully"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("jobOfferDTO"));

        // Optionally, you can also verify if the jobOfferService.create method was called with the correct parameters
        verify(jobOfferService, times(1)).create(jobOffer, loggedInUser);
    }


}