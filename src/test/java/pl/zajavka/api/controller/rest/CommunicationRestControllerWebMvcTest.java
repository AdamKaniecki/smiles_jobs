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
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import pl.zajavka.controller.api.CommunicationRestController;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.MeetingInterviewRequest;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.UserFixtures;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = CommunicationRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CommunicationRestControllerWebMvcTest {

    @MockBean
    private UserService userService;
    @MockBean
    private JobOfferService jobOfferService;
    @MockBean
    private CvService cvService;
    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();  // Inicjalizacja ObjectMapper przed użyciem
    }

    @Test
    void testObjectMapperSerialization() throws wiremock.com.fasterxml.jackson.core.JsonProcessingException {
        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setJobOfferId(1);
        request.setCvId(1);
        request.setNotificationId(1);
        request.setProposedDateTime(LocalDateTime.now());

        String json = objectMapper.writeValueAsString(request);
        System.out.println(json);  // Sprawdź, czy JSON jest poprawnie generowany
    }


    @Test
    void testSendCVSuccess() throws Exception {
        // Arrange
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setJobOfferId(1);
        request.setCvId(1);
        request.setNotificationId(1);

        User user = UserFixtures.someUser1();
        user.setUserName(username);
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        CV cv = new CV();
        cv.setUser(user);

        when(userService.findByUserName(username)).thenReturn(user);
        when(jobOfferService.findById(anyInt())).thenReturn(jobOffer);
        when(cvService.findByUser(any(User.class))).thenReturn(cv);
        when(notificationService.hasUserSentCVToJobOffer(any(User.class), any(JobOffer.class))).thenReturn(false);


        String jsonRequest = objectMapper.writeValueAsString(request);

        MockHttpServletRequestBuilder mockRequest = post("/api/sendCV")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest)
                .principal(authentication);

        // Act & Assert
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(content().string("CV sent successfully"));
    }


}
