package pl.zajavka.api.controller.rest;

import lombok.AllArgsConstructor;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.zajavka.controller.BusinessCardController;
import pl.zajavka.controller.api.BusinessCardRestController;
import pl.zajavka.controller.dto.BusinessCardDTO;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.AddressService;
import pl.zajavka.infrastructure.business.BusinessCardService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.AddressFixtures;
import pl.zajavka.util.BusinessCardFixtures;
import pl.zajavka.util.UserFixtures;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@WebMvcTest(controllers = BusinessCardRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessCardRestControllerWebMvcTest {



    @MockBean
    private UserService userService;

    @MockBean
    private BusinessCardService businessCardService;

    @MockBean
    private UserMapperDTO userMapperDTO;
    @MockBean
    private AddressService addressService;

    @MockBean
    private BusinessCardMapperDTO businessCardMapperDTO;

    private MockMvc mockMvc;



    @Test
    void testCreateBusinessCard() throws Exception {
        // Arrange
        String username = "testUser";
        BusinessCard businessCard = new BusinessCard();
        Address address = new Address();
        User user = new User();

        when(userService.findByUserName(anyString())).thenReturn(user);
        when(businessCardService.existByUser(any(User.class))).thenReturn(false);
        when(businessCardMapperDTO.map(any(BusinessCardDTO.class))).thenReturn(businessCard);
        when(addressService.createAddress(any(Address.class))).thenReturn(address);

        businessCard.setAddress(address);
        businessCard.setUser(user);

        when(businessCardService.createBusinessCard(any(BusinessCard.class), any(User.class))).thenReturn(businessCard);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        MockHttpServletRequestBuilder request = post("/api/createBusinessCard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Business Card\"}")
                .principal(authentication);

        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().string("Business card created successfully"));
    }
    @Test
    void testCreateBusinessCardAlreadyExists() throws Exception {
        // Arrange
        String username = "testUser";
        User user = new User();

        when(userService.findByUserName(anyString())).thenReturn(user);
        when(businessCardService.existByUser(any(User.class))).thenReturn(true);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        MockHttpServletRequestBuilder request = post("/api/createBusinessCard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Test Business Card\"}")
                .principal(authentication);

        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Business card already created"));
    }

    @Test
    void testShowMyBusinessCardExists() throws Exception {
        // Arrange
        String username = "testUser";
        User user = new User();
        BusinessCard businessCard = new BusinessCard();
        BusinessCardDTO businessCardDTO = new BusinessCardDTO();

        when(userService.findByUserName(anyString())).thenReturn(user);
        when(businessCardService.findByUser(any(User.class))).thenReturn(businessCard);
        when(businessCardMapperDTO.map(any(BusinessCard.class))).thenReturn(businessCardDTO);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        MockHttpServletRequestBuilder request = get("/api/showMyBusinessCard")
                .principal(authentication);

        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{}")); // You can customize this to match the actual expected JSON structure of BusinessCardDTO
    }

    @Test
    void testShowMyBusinessCardNotExists() throws Exception {
        // Arrange
        String username = "testUser";
        User user = new User();

        when(userService.findByUserName(anyString())).thenReturn(user);
        when(businessCardService.findByUser(any(User.class))).thenReturn(null);

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        MockHttpServletRequestBuilder request = get("/api/showMyBusinessCard")
                .principal(authentication);

        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    void testShowBusinessCardExists() throws Exception {
        // Arrange
        Integer businessCardId = 1;
        BusinessCard businessCard = new BusinessCard();
        BusinessCardDTO businessCardDTO = new BusinessCardDTO();

        when(businessCardService.findById(anyInt())).thenReturn(businessCard);
        when(businessCardMapperDTO.map(businessCard)).thenReturn(businessCardDTO);

        MockHttpServletRequestBuilder request = get("/api/showBusinessCard/{businessCardId}", businessCardId);

        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("{}")); // Customize to match the actual expected JSON structure of BusinessCardDTO
    }

    @Test
    void testShowBusinessCardNotExists() throws Exception {
        // Arrange
        Integer businessCardId = 1;

        when(businessCardService.findById(anyInt())).thenReturn(null);

        MockHttpServletRequestBuilder request = get("/api/showBusinessCard/{businessCardId}", businessCardId);

        // Act & Assert
        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }



}
