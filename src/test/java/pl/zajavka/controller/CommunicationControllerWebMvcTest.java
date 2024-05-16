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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    void sendCV_Success() throws Exception {
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
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sendCV")
                .param("jobOfferId","1")
                .principal(authentication);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("cv_send_successfully"));
    }


    @Test
    void sendCV_CVNotFound() throws Exception {
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
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sendCV")
                .param("jobOfferId", "1")
                .principal(authentication);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("cv_not_found"));
    }

    @Test
    void sendCV_CVAlreadySent() throws Exception {
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
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/sendCV")
                .param("jobOfferId", "1")
                .principal(authentication);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("cv_already_sent"));
    }

}
