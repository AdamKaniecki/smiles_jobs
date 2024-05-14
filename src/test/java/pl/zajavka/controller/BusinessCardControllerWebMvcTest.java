package pl.zajavka.controller;

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
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@WebMvcTest(controllers = BusinessCardController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessCardControllerWebMvcTest {



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
    public void testBusinessCardForm_ReturnsCreateBusinessCardView() throws Exception {
        // Given
        String username = "john_doe";
        User loggedInUser = UserFixtures.someUser2();
        UserDTO userDTO = UserFixtures.someUserDTO2();
        BusinessCardDTO businessCardDTO = new BusinessCardDTO();

        // Mockowanie autentykacji i serwisu użytkownika
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(userMapperDTO.map(loggedInUser)).thenReturn(userDTO);

        // When, Then
        mockMvc.perform(get("/BusinessCardForm").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("create_business_card"))
                .andExpect(model().attributeExists("userDTO"))
                .andExpect(model().attributeExists("businessCardDTO"))
                .andExpect(model().attribute("userDTO", userMapperDTO.map(loggedInUser)))
                .andExpect(model().attribute("businessCardDTO", businessCardDTO));
    }

    @Test
    public void testCreateBusinessCard_CreatesBusinessCardSuccessfully() throws Exception {
        // Given
        String username = "john_doe";
        User loggedInUser = UserFixtures.someUser2();
        BusinessCardDTO businessCardDTO = new BusinessCardDTO();

        // Mockowanie autentykacji i serwisu użytkownika
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(businessCardService.existByUser(loggedInUser)).thenReturn(false);

        // Mockowanie mapowania DTO na encję i odwrotnie
        BusinessCard businessCard = BusinessCardFixtures.someBusinessCard();
        when(businessCardMapperDTO.map(businessCardDTO)).thenReturn(businessCard);
        Address createdAddress = AddressFixtures.someAddress();
        when(addressService.createAddress(businessCard.getAddress())).thenReturn(createdAddress);

        // When, Then
        mockMvc.perform(post("/createBusinessCard").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("business_card_created_successfully"))
                .andExpect(model().attributeExists("businessCardDTO"));

    }

    @Test
    public void testCreateBusinessCard_ReturnsBusinessCardAlreadyCreatedView() throws Exception {
        // Given
        String username = "john_doe";
        User loggedInUser = UserFixtures.someUser1();

        // Mockowanie autentykacji i serwisu użytkownika
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(businessCardService.existByUser(loggedInUser)).thenReturn(true);

        // When, Then
        mockMvc.perform(post("/createBusinessCard").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("business_card_already_create"));
    }

    @Test
    public void testShowMyBusinessCard_ReturnsBusinessCardSuccessfully() throws Exception {
        // Given
        String username = "john_doe";
        User loggedInUser = UserFixtures.someUser1();
        BusinessCard businessCard = BusinessCardFixtures.someBusinessCard();
        Address address = AddressFixtures.someAddress();
        BusinessCardDTO businessCardDTO = new BusinessCardDTO();
        businessCardDTO.setAddress(address);

        // Mockowanie autentykacji i serwisu użytkownika
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(businessCardService.findByUser(loggedInUser)).thenReturn(businessCard);

        // Mockowanie mapowania encji na DTO
        when(businessCardMapperDTO.map(businessCard)).thenReturn(businessCardDTO);

        // When, Then
        mockMvc.perform(get("/showMyBusinessCard").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("show_my_businessCard"))
                .andExpect(model().attributeExists("businessCardDTO"))
                .andExpect(model().attribute("businessCardDTO", businessCardDTO));
    }

    @Test
    public void testShowMyBusinessCard_ReturnsBusinessCardNotFoundView() throws Exception {
        // Given
        String username = "john_doe";
        User loggedInUser = UserFixtures.someUser1();

        // Mockowanie autentykacji i serwisu użytkownika
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(businessCardService.findByUser(loggedInUser)).thenReturn(null);

        // When, Then
        mockMvc.perform(get("/showMyBusinessCard").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("businessCard_not_found"));
    }

    @Test
    public void testShowBusinessCard_BusinessCardFound_ReturnsBusinessCardSuccessfully() throws Exception {
        // Given
        int businessCardId = 1;
        BusinessCard businessCard = BusinessCardFixtures.someBusinessCard();
        Address address = AddressFixtures.someAddress();
        BusinessCardDTO businessCardDTO = new BusinessCardDTO();
        businessCardDTO.setAddress(address);

        // Mockowanie serwisu wizytówek
        when(businessCardService.findById(businessCardId)).thenReturn(businessCard);

        // Mockowanie mapowania encji na DTO
        when(businessCardMapperDTO.map(businessCard)).thenReturn(businessCardDTO);

        // When, Then
        mockMvc.perform(get("/businessCard/{businessCardId}", businessCardId))
                .andExpect(status().isOk())
                .andExpect(view().name("show_businessCard"))
                .andExpect(model().attributeExists("businessCardDTO"))
                .andExpect(model().attribute("businessCardDTO", businessCardDTO));
    }

    @Test
    public void testShowBusinessCard_BusinessCardNotFound_ReturnsBusinessCardNotFoundView() throws Exception {
        // Given
        int businessCardId = 1;

        // Mockowanie serwisu wizytówek
        when(businessCardService.findById(businessCardId)).thenReturn(null);

        // When, Then
        mockMvc.perform(get("/businessCard/{businessCardId}", businessCardId))
                .andExpect(status().isOk())
                .andExpect(view().name("businessCard_not_found"))
                .andExpect(model().attributeExists("businessCardDTO"))
                .andExpect(model().attribute("businessCardDTO", new BusinessCardDTO()));
    }

    @Test
    public void testUpdateMyBusinessCard_UserBusinessCardFound_ReturnsUpdateBusinessCardForm() throws Exception {
        // Given
        String username = "john_doe";
        User loggedInUser = UserFixtures.someUser1();
        BusinessCard userBusinessCard = BusinessCardFixtures.someBusinessCard();
        Address address = AddressFixtures.someAddress();
        BusinessCardDTO businessCardDTO = new BusinessCardDTO();
        businessCardDTO.setAddress(address);
        UserDTO userDTO = UserFixtures.someUserDTO1();

        // Mockowanie autentykacji, serwisu użytkownika i serwisu wizytówek
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(businessCardService.findByUser(loggedInUser)).thenReturn(userBusinessCard);

        // Mockowanie mapowania encji na DTO
        when(businessCardMapperDTO.map(userBusinessCard)).thenReturn(businessCardDTO);
        when(userMapperDTO.map(loggedInUser)).thenReturn(userDTO);

        // When, Then
        mockMvc.perform(get("/updateBusinessCardForm").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("update_business_card_form"))
                .andExpect(model().attributeExists("businessCardDTO"))
                .andExpect(model().attributeExists("userDTO"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attribute("businessCardDTO", businessCardDTO))
                .andExpect(model().attribute("userDTO", userDTO))
                .andExpect(model().attribute("address", userBusinessCard.getAddress()));
    }

    @Test
    public void testUpdateMyBusinessCard_UserBusinessCardNotFound_ReturnsBusinessCardNotFoundView() throws Exception {
        // Given
        String username = "john_doe";
        User loggedInUser = UserFixtures.someUser1();

        // Mockowanie autentykacji i serwisu użytkownika
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(businessCardService.findByUser(loggedInUser)).thenReturn(null);

        // When, Then
        mockMvc.perform(get("/updateBusinessCardForm").principal(authentication))
                .andExpect(status().isOk())
                .andExpect(view().name("businessCard_not_found"));
    }

    @Test
    public void testUpdateBusinessCard_UpdateSuccessful_ReturnsUpdateBusinessCardSuccessfullyView() throws Exception {
        // Given

        Authentication authentication = Mockito.mock(Authentication.class);
        BusinessCardDTO updateBusinessCardDTO = new BusinessCardDTO();
        BusinessCard businessCard = BusinessCardFixtures.someBusinessCard();

        // Mockowanie serwisu wizytówek
        when(businessCardService.findById(updateBusinessCardDTO.getId())).thenReturn(businessCard);



        // Utworzenie zapytania HTTP
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.put("/updateBusinessCardDone")
                .principal(authentication);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("update_business_card_successfully"));

    }

    @Test
    public void testDeleteBusinessCard_DeletionSuccessful_ReturnsBusinessCardDeletedSuccessfullyView() throws Exception {
        // Given
        Authentication authentication = Mockito.mock(Authentication.class);
        BusinessCardDTO businessCardDTO = new BusinessCardDTO();
        BusinessCard businessCard = BusinessCardFixtures.someBusinessCard();

        // Mockowanie serwisu wizytówek
        when(businessCardService.findById(businessCardDTO.getId())).thenReturn(businessCard);

        // Utworzenie zapytania HTTP DELETE
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/deleteBusinessCard")
                        .principal(authentication);

        // When, Then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("business_card_deleted_successfully"));
//             ;

        // Sprawdzenie, czy metoda deleteBusinessCard została wywołana
        verify(businessCardService, times(1)).deleteBusinessCard(businessCard);
        verify(addressService, times(1)).deleteAddress(businessCard.getAddress());
    }



}

