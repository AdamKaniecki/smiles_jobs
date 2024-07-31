package pl.zajavka.api.controller.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.zajavka.controller.api.CvRestController;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = CvRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CvRestControllerWebMvcTest {

    @MockBean
    private CvService cvService;
    @MockBean
    private UserService userService;
    @MockBean
    private CvMapperDTO cvMapperDTO;

    private MockMvc mockMvc;

    @Test
    void testCreateCVSuccess() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User loggedInUser = new User();
        CV cv = new CV();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(cvService.existByUser(loggedInUser)).thenReturn(false);
        when(cvMapperDTO.map(any(CvDTO.class))).thenReturn(cv);

        CvDTO cvDTO = new CvDTO();
        String jsonRequest = new ObjectMapper().writeValueAsString(cvDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/createCV")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isCreated())
                .andExpect(content().string("CV created successfully"));
    }

    @Test
    void testCreateCVUnauthorized() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(null);

        CvDTO cvDTO = new CvDTO();
        String jsonRequest = new ObjectMapper().writeValueAsString(cvDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/createCV")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("User not authenticated"));
    }


    @Test
    void testCreateCVUserNotFound() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(null);

        CvDTO cvDTO = new CvDTO();
        String jsonRequest = new ObjectMapper().writeValueAsString(cvDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/createCV")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }

    @Test
    void testCreateCVAlreadyExists() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User loggedInUser = new User();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(cvService.existByUser(loggedInUser)).thenReturn(true);

        CvDTO cvDTO = new CvDTO();
        String jsonRequest = new ObjectMapper().writeValueAsString(cvDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/createCV")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest)
                        .principal(authentication))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("CV already created"));
    }


    @Test
    void testRedirectToShowMyCVSuccess() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User loggedInUser = new User();
        CV userCV = new CV();
        CvDTO cvDTO = new CvDTO();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(cvService.findByUser(loggedInUser)).thenReturn(userCV);
        when(cvMapperDTO.map(userCV)).thenReturn(cvDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ShowMyCV")
                        .principal(authentication))
                .andExpect(status().isOk());
    }


    @Test
    void testRedirectToShowMyCVNotFound() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        User loggedInUser = new User();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(cvService.findByUser(loggedInUser)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/ShowMyCV")
                        .principal(authentication))
                .andExpect(status().isNotFound())
                .andExpect(content().string("You have not created your CV yet"));
    }


}
