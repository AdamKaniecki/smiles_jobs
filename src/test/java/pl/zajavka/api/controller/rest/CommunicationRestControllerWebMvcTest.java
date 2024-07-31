package pl.zajavka.api.controller.rest;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.zajavka.controller.api.CommunicationRestController;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.*;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.NotificationFixtures;
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

    @Test
    void testSendCVNotFoundException() throws Exception {
        // Arrange
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        MeetingInterviewRequest request = new MeetingInterviewRequest();

        when(userService.findByUserName(username)).thenThrow(new EntityNotFoundException());

        MockHttpServletRequestBuilder mockRequest = post("/api/sendCV")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .principal(authentication);

        // Act & Assert
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("CV not found"));
    }

    @Test
    void testSendCVAlreadySent() throws Exception {
        // Arrange
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setJobOfferId(1);
        request.setCvId(1);
        request.setNotificationId(1);

        User user = new User();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        CV cv = new CV();
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(user);
        when(jobOfferService.findById(anyInt())).thenReturn(jobOffer);
        when(cvService.findByUser(any(User.class))).thenReturn(cv);
        when(notificationService.hasUserSentCVToJobOffer(any(User.class), any(JobOffer.class))).thenReturn(true);

        MockHttpServletRequestBuilder mockRequest = post("/api/sendCV")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .principal(authentication);

        // Act & Assert
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CV already sent"));
    }

    @Test
    void testArrangeInterviewSuccess() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setCvId(1);
        request.setNotificationId(1);
        request.setJobOfferId(1);

        User loggedInUser = new User();
        loggedInUser.setUserName(username);

        User cvUser = new User();
        cvUser.setUserName("cvUser");

        Notification notification = NotificationFixtures.sampleNotification1fully();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(userService.getUserByCv(anyInt())).thenReturn(cvUser);
        when(notificationService.findById(anyInt())).thenReturn(notification);

        String jsonRequest = objectMapper.writeValueAsString(request);
        System.out.println(jsonRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/arrangeInterview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("Interview arranged successfully"));
    }

    @Test
    void testArrangeInterviewEntityNotFoundException() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setCvId(1);
        request.setNotificationId(1);
        request.setJobOfferId(1);

        User loggedInUser = new User();
        loggedInUser.setUserName(username);

        when(userService.findByUserName(username)).thenReturn(null);
        when(userService.getUserByCv(anyInt())).thenThrow(new EntityNotFoundException());
        when(notificationService.findById(1)).thenReturn(NotificationFixtures.sampleNotification1forArrange());

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/arrangeInterview")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Notification or user not found"));
    }

    @Test
    void testChangeMeetingDateSuccess() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User loggedInUser = new User();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer1();
        Notification notification = NotificationFixtures.sampleNotification1();
        User adresat = new User();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferService.findById(anyInt())).thenReturn(jobOffer);
        when(notificationService.findById(anyInt())).thenReturn(notification);
        jobOffer.setUser(adresat);

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setJobOfferId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/changeMeetingDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("Meeting date changed successfully"));
    }

    @Test
    void testChangeMeetingDateEntityNotFound() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(new User());
        when(jobOfferService.findById(anyInt())).thenThrow(new EntityNotFoundException("JobOffer not found"));
        when(notificationService.findById(anyInt())).thenReturn(NotificationFixtures.sampleNotification1());

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setJobOfferId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/changeMeetingDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Notification or job offer not found"));
    }

    @Test
    void testChangeMeetingDateBadRequest() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(new User());
        when(jobOfferService.findById(anyInt())).thenReturn(JobOfferFixtures.someJobOffer1());
        when(notificationService.findById(anyInt())).thenReturn( NotificationFixtures.sampleNotification1());
        Mockito.doThrow(new RuntimeException("Some error")).when(notificationService).changeMeetingDate(any(), any(), any());

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setJobOfferId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/changeMeetingDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("An error occurred while changing meeting date"));
    }

    @Test
    void testAcceptNotificationSuccess() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User loggedInUser = new User();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer1();
        Notification notification = NotificationFixtures.sampleNotification1();
        User adresat = new User();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferService.findById(anyInt())).thenReturn(jobOffer);
        when(notificationService.findById(anyInt())).thenReturn(notification);
        jobOffer.setUser(adresat);

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setJobOfferId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/acceptMeetingDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("Meeting date accepted successfully"));
    }

    @Test
    void testAcceptNotificationEntityNotFound() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(new User());
        when(jobOfferService.findById(anyInt())).thenThrow(new EntityNotFoundException("JobOffer not found"));
        when(notificationService.findById(anyInt())).thenReturn(NotificationFixtures.sampleNotification1());

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setJobOfferId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/acceptMeetingDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Notification or job offer not found"));
    }

    @Test
    void testAcceptNotificationBadRequest() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(new User());
        when(jobOfferService.findById(anyInt())).thenReturn(JobOfferFixtures.someJobOffer1());
        when(notificationService.findById(anyInt())).thenReturn(NotificationFixtures.sampleNotification1());
        Mockito.doThrow(new RuntimeException("Some error")).when(notificationService).acceptMeetingDateTime(any(), any(), any());

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setJobOfferId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/acceptMeetingDate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("An error occurred while accepting meeting date"));
    }

    @Test
    void testDeclineNotificationSuccess() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User loggedInUser = new User();
        User cvUser = new User();
        Notification notification = NotificationFixtures.sampleNotification1();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(userService.getUserByCv(anyInt())).thenReturn(cvUser);
        when(notificationService.findById(anyInt())).thenReturn(notification);

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setCvId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/decline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification declined successfully"));
    }

    @Test
    void testDeclineNotificationEntityNotFound() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(new User());
        when(userService.getUserByCv(anyInt())).thenThrow(new EntityNotFoundException("User not found"));
        when(notificationService.findById(anyInt())).thenReturn(NotificationFixtures.sampleNotification1());

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setCvId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/decline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Notification or user not found"));
    }

    @Test
    void testDeclineNotificationBadRequest() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(new User());
        when(userService.getUserByCv(anyInt())).thenReturn(new User());
        when(notificationService.findById(anyInt())).thenReturn(NotificationFixtures.sampleNotification1());
        Mockito.doThrow(new RuntimeException("Some error")).when(notificationService).declineCandidate(any(), any(), any());

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setCvId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/decline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("An error occurred while declining notification"));
    }

    @Test
    void testHiredNotificationSuccess() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User loggedInUser = new User();
        User cvUser = new User();
        Notification notification = NotificationFixtures.sampleNotification1();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(userService.getUserByCv(anyInt())).thenReturn(cvUser);
        when(notificationService.findById(anyInt())).thenReturn(notification);

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setCvId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/hired")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("Candidate hired successfully"));
    }

    @Test
    void testHiredNotificationEntityNotFound() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(new User());
        when(userService.getUserByCv(anyInt())).thenThrow(new EntityNotFoundException("User not found"));
        when(notificationService.findById(anyInt())).thenReturn(NotificationFixtures.sampleNotification1());

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setCvId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/hired")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Notification or user not found"));
    }

    @Test
    void testHiredNotificationBadRequest() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(new User());
        when(userService.getUserByCv(anyInt())).thenReturn(new User());
        when(notificationService.findById(anyInt())).thenReturn(NotificationFixtures.sampleNotification1());
        Mockito.doThrow(new RuntimeException("Some error")).when(notificationService).hiredCandidate(any(), any(), any());

        MeetingInterviewRequest request = new MeetingInterviewRequest();
        request.setCvId(1);
        request.setNotificationId(1);

        String jsonRequest = new ObjectMapper().writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/hired")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("An error occurred while hiring candidate"));
    }

}
