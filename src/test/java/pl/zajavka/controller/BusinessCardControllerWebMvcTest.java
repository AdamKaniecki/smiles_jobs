package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
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

import static org.mockito.Mockito.when;
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

}

