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

    @Test
    public void testShowMyCV_CVNotFound_ReturnsCvNotFound() throws Exception {
        // Given
        int cvId = 123;
        when(cvService.findById(cvId)).thenReturn(null);

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.get("/showCV?id=" + cvId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cv_not_found"));
    }

    @Test
    public void testUpdateMyCV_UserLoggedIn_CVFound_ReturnsUpdateCvForm() throws Exception {
        // Given
        String username = "adam12";
        User loggedInUser = UserFixtures.someUser1();
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        Address address = AddressFixtures.someAddress();
        CV userCV = new CV();
        userCV.setAddress(address);
        CvDTO cvDTO = new CvDTO();
        UserDTO userDTO = new UserDTO(); // Assuming UserDTO is a DTO for User

        when(cvService.findByUser(loggedInUser)).thenReturn(userCV);
        when(cvMapperDTO.map(userCV)).thenReturn(cvDTO);
        when(userMapperDTO.map(loggedInUser)).thenReturn(userDTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/updateCvForm")
                .principal(authentication);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update_cv_form"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("cvDTO"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userDTO"))
                .andExpect(MockMvcResultMatchers.model().attribute("address", address));
    }

    @Test
    public void testUpdateMyCV_UserLoggedIn_CVNotFound_ReturnsCvNotFound() throws Exception {
        // Given
        String username = "adam12";
        User loggedInUser = UserFixtures.someUser1();
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(cvService.findByUser(loggedInUser)).thenReturn(null);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/updateCvForm")
                .principal(authentication);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cv_not_found"));
    }

    @Test
    public void testUpdateCv_CvFound_ReturnsCvUpdateSuccessfully() throws Exception {
        // Given
        String username = "adam12";
        User loggedInUser = UserFixtures.someUser1();
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        CvDTO updateCvDTO = new CvDTO(); // Assuming CvDTO contains necessary fields
        CV cv = new CV();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(cvService.findByUser(loggedInUser)).thenReturn(cv);
        when(cvMapperDTO.map(cv)).thenReturn(updateCvDTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateCVDone")
                .principal(authentication)
                .flashAttr("cvDTO", updateCvDTO);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cv_update successfully"));
    }
    @Test
    public void testUpdateCv_CvNotFound_ReturnsCvNotFound() throws Exception {
        // Given
        String username = "adam12";
        User loggedInUser = UserFixtures.someUser1();
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        CvDTO updateCvDTO = new CvDTO(); // Assuming CvDTO contains necessary fields

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(cvService.findByUser(loggedInUser)).thenReturn(null);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateCVDone")
                .principal(authentication)
                .flashAttr("cvDTO", updateCvDTO);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("cv_not_found"));
    }

    @Test
    public void testUpdateAddress_AddressFound_ReturnsUpdateAddressSuccessfullyForCandidate() throws Exception {
        // Given
        String username = "adam12";
        User loggedInUser = UserFixtures.someUser1();
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        Address updateAddress = AddressFixtures.someAddress();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(addressService.findById(updateAddress.getId())).thenReturn(updateAddress);
        when(addressService.determineRoleSpecificString(loggedInUser)).thenReturn("update_address_successfully_cv");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateAddressDone")
                .principal(authentication)
                .flashAttr("address", updateAddress);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update_address_successfully_cv"));
    }

    @Test
    public void testUpdateAddress_AddressFound_ReturnsUpdateAddressSuccessfullyForCompany() throws Exception {
        // Given
        String username = "adam122222";
        User loggedInUser = UserFixtures.someUser2();
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        Address updateAddress = AddressFixtures.someAddress();

        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(addressService.findById(updateAddress.getId())).thenReturn(updateAddress);
        when(addressService.determineRoleSpecificString(loggedInUser)).thenReturn("update_address_successfully_business_card");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateAddressDone")
                .principal(authentication)
                .flashAttr("address", updateAddress);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("update_address_successfully_business_card"));
    }

}