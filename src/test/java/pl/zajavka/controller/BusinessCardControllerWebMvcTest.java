//package pl.zajavka.api.controller;
//
//import lombok.AllArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.core.Authentication;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.util.LinkedMultiValueMap;
//import pl.zajavka.api.controller.BusinessCardController;
//import pl.zajavka.api.dto.BusinessCardDTO;
//import pl.zajavka.api.dto.mapper.BusinessCardMapperDTO;
//import pl.zajavka.api.dto.mapper.UserMapperDTO;
//import pl.zajavka.business.AddressService;
//import pl.zajavka.business.BusinessCardService;
//import pl.zajavka.business.UserService;
//import pl.zajavka.domain.Address;
//import pl.zajavka.domain.BusinessCard;
//import pl.zajavka.domain.User;
//import pl.zajavka.infrastructure.database.entity.AddressEntity;
//import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
//import pl.zajavka.infrastructure.security.UserEntity;
//
//import java.util.Map;
//import java.util.stream.Stream;
//
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@ActiveProfiles("test")
//@WebMvcTest(controllers = BusinessCardController.class)
//@AutoConfigureMockMvc(addFilters = false)
//@AllArgsConstructor(onConstructor = @__(@Autowired))
//public class BusinessCardControllerWebMvcTest {
//
//    private MockMvc mockMvc;
//
//    @MockBean
//    private UserService userService;
//
//    @MockBean
//    private BusinessCardService businessCardService;
//
//    @MockBean
//    private UserMapperDTO userMapperDTO;
//    @MockBean
//    private AddressService addressService;
//
//    @MockBean
//    private BusinessCardMapperDTO businessCardMapperDTO;
//
//
//    @Test
//    public void testCreateBusinessCard_SuccessfulCreation() throws Exception {
//
//
//        User user = User.builder()
//                .userName("john_doe")
//                .password("password")
//                .email("john@example.com")
//                .active(true)
//                .build();
//
//        Address address = Address.builder()
//                .country("Poland")
//                .city("Warsaw")
//                .postalCode("00-001")
//                .streetAndNumber("Main Street 123")
//                .build();
//
//
//        BusinessCard businessCard = BusinessCard.builder()
//                .office("Office 123")
//                .scopeOperations("Scope of operations")
//                .recruitmentEmail("recruitment@example.com")
//                .phoneNumber("+12 345 678 555")
//                .companyDescription("Company description")
//                .technologiesAndTools("Technologies and tools")
//                .certificatesAndAwards("Certificates and awards")
//                .user(user)
//                .address(address)
//                .build();
//
//        BusinessCardDTO businessCardDTO = businessCardMapperDTO.map(businessCard);
//
//
//        // Symulujemy przekazanie tego obiektu w żądaniu POST do kontrolera
//        mockMvc.perform(MockMvcRequestBuilders.post("/createBusinessCard"))
//                       // Przekazujemy obiekt jako "businessCardDTO" atrybut modelu
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().attributeExists("businessCardDTO")); // Sprawdzamy, czy model zawiera oczekiwany atrybut
//    }
//}
////    @Test
////    void carPurchaseWorksCorrectly() throws Exception {
////        // given
////        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
////        CarPurchaseDTO.buildDefaultData().asMap().forEach(parameters::add);
////
////        Invoice expectedInvoice = Invoice.builder().invoiceNumber("test").build();
////        Mockito.when(carPurchaseService.purchase(Mockito.any())).thenReturn(expectedInvoice);
////
////        // when, then
////        mockMvc.perform(post(PurchaseController.PURCHASE).params(parameters))
////                .andExpect(status().isOk())
////                .andExpect(model().attributeExists("invoiceNumber"))
////                .andExpect(model().attributeExists("customerName"))
////                .andExpect(model().attributeExists("customerSurname"))
////                .andExpect(view().name("car_purchase_done"));
////    }
////}
