package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.NotificationFixtures;
import pl.zajavka.util.UserFixtures;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = CommunicationController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CommunicationControllerWebMvcTest {

    @MockBean
    private UserService userService;
    @MockBean
    private JobOfferService jobOfferService;
    @MockBean
    private CvService cvService;
    @MockBean
    private NotificationService notificationService;

    MockMvc mockMvc;

    @Test
    void testSendCV_Success() throws Exception {
        // given
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        Notification notification = NotificationFixtures.sampleNotification1fully();
        CV cv = CvFixtures.someCv1();
        Authentication authentication = mock(Authentication.class);
        String username = "testUser";
        User loggedUser = cv.getUser();
        User recipient = UserFixtures.someUser2();
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(new User());
        when(cvService.findByUser(any(User.class))).thenReturn(cv);
        when(jobOfferService.findById(jobOffer.getId())).thenReturn(jobOffer);
        when(notificationService.createNotification(jobOffer, cv, loggedUser, recipient )).thenReturn(notification);

        //  when/then
        MockHttpServletRequestBuilder requestBuilder = post("/sendCV")
                .param("jobOfferId","1")
                .principal(authentication);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("cv_send_successfully"));
    }


    @Test
    void testSendCV_CVNotFound() throws Exception {
        // given
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        CV cv = null; // CV nie zostało znalezione
        Authentication authentication = mock(Authentication.class);
        String username = "testUser";
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(new User());
        when(cvService.findByUser(any(User.class))).thenReturn(cv);
        when(jobOfferService.findById(jobOffer.getId())).thenReturn(jobOffer);

        // when/then
        MockHttpServletRequestBuilder requestBuilder = post("/sendCV")
                .param("jobOfferId", "1")
                .principal(authentication);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("cv_not_found"));
    }

    @Test
    void testSendCV_CVAlreadySent() throws Exception {
        // given
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        CV cv = CvFixtures.someCv1();
        Authentication authentication = mock(Authentication.class);
        String username = "testUser";
        User loggedUser = cv.getUser();
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(new User());
        when(cvService.findByUser(any(User.class))).thenReturn(cv);
        when(jobOfferService.findById(jobOffer.getId())).thenReturn(jobOffer);
        when(notificationService.hasUserSentCVToJobOffer(loggedUser, jobOffer)).thenReturn(true);

        // when/then
        MockHttpServletRequestBuilder requestBuilder = post("/sendCV")
                .param("jobOfferId", "1")
                .principal(authentication);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("cv_already_sent"));
    }

    @Test
    void testChangeMeetingDate_Success() throws Exception {
        // given
        Integer notificationId = 1;
        Integer jobOfferId = 1;
        Authentication authentication = mock(Authentication.class);
        String username = "testUser";
        User loggedInUser = new User();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        Notification notification = NotificationFixtures.sampleNotification1fully();

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferService.findById(jobOfferId)).thenReturn(jobOffer);
        when(notificationService.findById(notificationId)).thenReturn(notification);

        // when/then
        mockMvc.perform(post("/changeMeetingDate")
                        .param("notificationId", notificationId.toString())
                        .param("jobOfferId", jobOfferId.toString())
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("change_meeting_date_successfully"));
    }

    @Test
    void testAcceptNotification_Success() throws Exception {
        // given
        Integer notificationId = 1;
        Integer jobOfferId = 1;
        Authentication authentication = mock(Authentication.class);
        String username = "testUser";
        User loggedInUser = new User();
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        Notification notification = NotificationFixtures.sampleNotification1fully();

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(jobOfferService.findById(jobOfferId)).thenReturn(jobOffer);
        when(notificationService.findById(notificationId)).thenReturn(notification);

        // when/then
        mockMvc.perform(post("/acceptMeetingDate")
                        .param("notificationId", notificationId.toString())
                        .param("jobOfferId", jobOfferId.toString())
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("meeting_date_successfully"));
    }

    @Test
    void testArrangeInterview_Success() throws Exception {
        // given
        Integer cvId = 1;
        Integer notificationId = 1;
        LocalDateTime proposedDateTime = LocalDateTime.now();
        Authentication authentication = mock(Authentication.class);
        String username = "testUser";
        User loggedInUser = new User();
        User cvUser = new User();
        Notification notification =NotificationFixtures.sampleNotification1fully();

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(userService.getUserByCv(cvId)).thenReturn(cvUser);
        when(notificationService.findById(notificationId)).thenReturn(notification);

        // when/then
        mockMvc.perform(post("/arrangeInterview")
                        .param("cvId", cvId.toString())
                        .param("notificationId", notificationId.toString())
                        .param("proposedDateTime", proposedDateTime.toString())
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("arrange_interview_successfully"));
    }

    @Test
    void testDeclineCandidate_Success() throws Exception {
        // given
        Integer cvId = 1;
        Integer notificationId = 1;
        Authentication authentication = mock(Authentication.class);
        String username = "testUser";
        User loggedInUser = new User();
        User cvUser = new User();
        Notification notification = NotificationFixtures.sampleNotification1fully();

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(userService.getUserByCv(cvId)).thenReturn(cvUser);
        when(notificationService.findById(notificationId)).thenReturn(notification);

        // when/then
        mockMvc.perform(post("/decline")
                        .param("notificationId", notificationId.toString())
                        .param("cvId", cvId.toString())
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("decline_successfully"));
    }

    @Test
    void testHiredCandidate_Success() throws Exception {
        // given
        Integer cvId = 1;
        Integer notificationId = 1;
        Authentication authentication = mock(Authentication.class);
        String username = "testUser";
        User loggedInUser = new User();
        User cvUser = new User();
        Notification notification = NotificationFixtures.sampleNotification1fully();

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(userService.getUserByCv(cvId)).thenReturn(cvUser);
        when(notificationService.findById(notificationId)).thenReturn(notification);

        // when/then
        mockMvc.perform(post("/hired")
                        .param("notificationId", notificationId.toString())
                        .param("cvId", cvId.toString())
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("hired_successfully"));
    }

}
