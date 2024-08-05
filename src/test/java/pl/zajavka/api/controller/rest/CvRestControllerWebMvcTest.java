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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.zajavka.controller.api.CvRestController;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.SearchRequest;
import pl.zajavka.infrastructure.domain.User;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;


import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @MockBean
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();  // Inicjalizacja ObjectMapper przed użyciem
    }

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

    @Test
    void testShowCVSuccess() throws Exception {
        Integer cvId = 1;
        CV cv = new CV();
        CvDTO cvDTO = new CvDTO();

        when(cvService.findById(cvId)).thenReturn(cv);
        when(cvMapperDTO.map(cv)).thenReturn(cvDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/showCV/{id}", cvId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testShowCVNotFound() throws Exception {
        Integer cvId = 1;

        when(cvService.findById(cvId)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/showCV/{id}", cvId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCvSuccess() throws Exception {
        String username = "testUser";
        User loggedInUser = new User();
        loggedInUser.setUserName(username);
        CV cv = new CV();
        CvDTO cvDTO = new CvDTO();
        cvDTO.setName("New Name");
        cvDTO.setSurname("New Surname");
        cvDTO.setSex("Male");
        cvDTO.setDateOfBirth("1990-01-01");
        cvDTO.setMaritalStatus("Single");
        cvDTO.setPhoneNumber("+48 669 666 665");
        cvDTO.setContactEmail("new.email@example.com");
        cvDTO.setSkillsAndTools("Java, Spring");
        cvDTO.setProgrammingLanguage("Java");
        cvDTO.setFollowPosition("Developer");
        cvDTO.setAboutMe("Passionate developer");
        cvDTO.setCertificatesOfCourses("Java Certification");
        cvDTO.setProjects("Project A, Project B");
        cvDTO.setSocialMediaProfil("LinkedIn Profile");
        cvDTO.setEducation("Master's in Computer Science");
        cvDTO.setWorkExperience("5 years");
        cvDTO.setLanguage("English");
        cvDTO.setLanguageLevel("Fluent");
        cvDTO.setHobby("Reading");

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(cvService.findByUser(loggedInUser)).thenReturn(cv);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/updateCv")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cvDTO))
                        .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(content().string("CV updated successfully"));
    }


    @Test
    void testSearchCVUnauthorized() throws Exception {

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setKeyword("Java");
        searchRequest.setCategory("Programming");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/searchCV")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(searchRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testSearchCVAuthorized() throws Exception {
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setKeyword("Java");
        searchRequest.setCategory("Programming");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/searchCV")
                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(searchRequest))
                        .principal(authentication))

                .andExpect(status().isOk());
    }


}
