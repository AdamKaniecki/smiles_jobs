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
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3forNotification();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferService.create(jobOffer,loggedInUser)).thenReturn(jobOffer);
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

}
