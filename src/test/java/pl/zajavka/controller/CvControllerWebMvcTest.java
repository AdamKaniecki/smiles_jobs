package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.validation.BindingResult;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.AddressService;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.AddressFixtures;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = CvController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CvControllerWebMvcTest {
    @MockBean
    private CvService cvService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private UserService userService;
    @MockBean
    private CvMapperDTO cvMapperDTO;
    @MockBean
    private UserMapperDTO userMapperDTO;

    MockMvc mockMvc;

    @Test
    public void testCvForm() throws Exception {
        // given
        User user = UserFixtures.someUser1();
        CvDTO cvDTO = new CvDTO();
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("adam12");
        when(userService.findByUserName("adam12")).thenReturn(user);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/CvForm").principal(authentication)
                        .flashAttr("cvDTO", cvDTO))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create_cv"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userDTO"))
                .andExpect(MockMvcResultMatchers.model().attribute("cvDTO", cvDTO))
                .andExpect(MockMvcResultMatchers.model().attribute("userDTO", user));
    }

    @Test
    public void testCvFormWithoutAuthentication() throws Exception {
        // given
        Authentication authentication = mock(Authentication.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false); // Ustawienie braku błędów

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/CvForm").principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("error"));
    }


    @Test
    public void testCreateCV_Successfully() throws Exception {
        // given
        CvDTO cvDTO = new CvDTO();
        User loggedInUser = new User();

        // Mockowanie logiki serwisu
        when(userService.findByUserName("testUser")).thenReturn(loggedInUser);
        when(cvService.existByUser(loggedInUser)).thenReturn(false);

        // Ustawienie obiektu Authentication z nazwą użytkownika
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.post("/createCV").principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cv_created_successfully"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("cvDTO"))
                .andExpect(MockMvcResultMatchers.model().attribute("userDTO", loggedInUser));;
    }


    @Test
    public void testCreateCV_CVAlreadyCreated() throws Exception {
        // given
        User loggedInUser = new User();
        // Mockowanie logiki serwisu
        when(userService.findByUserName("testUser")).thenReturn(loggedInUser);
        when(cvService.existByUser(loggedInUser)).thenReturn(true);
        // Ustawienie obiektu Authentication z nazwą użytkownika
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");



        //when/ then
        mockMvc.perform(MockMvcRequestBuilders.post("/createCV").principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cv_already_created"));

        // sprawdzenie, czy serwis został wywołany z odpowiednimi parametrami
        Mockito.verify(cvService, Mockito.times(0)).createCV(Mockito.any(), Mockito.any());
    }

    @Test
    public void testRedirectToShowMyCV_UserHasCV_ReturnsShowMyCv() throws Exception {
        // Given
        Address address = AddressFixtures.someAddress();
        Authentication authentication = new UsernamePasswordAuthenticationToken("adam12", "testPassword");
        User loggedInUser = UserFixtures.someUser1();
        when(userService.findByUserName("adam12")).thenReturn(loggedInUser);

        CV cv = new CV();
        when(cvService.findByUserOpt(loggedInUser)).thenReturn(Optional.of(cv));

        CvDTO cvDTO = new CvDTO();
        cvDTO.setAddress(address);
        when(cvMapperDTO.map(cv)).thenReturn(cvDTO);

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.get("/ShowMyCV").principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("show_my_cv"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("cvDTO"));
    }


    @Test
    public void testRedirectToShowMyCV_UserHasNoCV_ReturnsCvNotFound() throws Exception {
        // Given
        Authentication authentication = new UsernamePasswordAuthenticationToken("adam12", "testPassword");
        User loggedInUser = UserFixtures.someUser1();
        when(userService.findByUserName("adam12")).thenReturn(loggedInUser);

        when(cvService.findByUserOpt(loggedInUser)).thenReturn(Optional.empty());

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.get("/ShowMyCV").principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cv_not_found"))
                .andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cvDTO"));
    }


    @Test
    public void testShowCV_CVExists_ReturnsRedirect() throws Exception {
        // Given
        int cvId = 123;
        CV cv = new CV();
        when(cvService.findById(cvId)).thenReturn(cv);

        CvDTO cvDTO = CvFixtures.someCvDTO();
        when(cvMapperDTO.map(cv)).thenReturn(cvDTO);

        UserDTO userDTO = new UserDTO(); // Assuming UserDTO is a DTO for User
        when(userMapperDTO.map(cv.getUser())).thenReturn(userDTO);

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.get("/showCV?id=" + cvId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/showCV?id=" + cvId));
    }
}