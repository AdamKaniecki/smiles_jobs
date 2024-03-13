package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.Model;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.util.UserFixtures;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static pl.zajavka.controller.RegistryController.CANDIDATE_REGISTRY;
import static pl.zajavka.controller.RegistryController.COMPANY_REGISTRY;

@WebMvcTest(controllers = RegistryController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RegistryControllerWebMvcTest {

    @MockBean
    private final UserService userService;
    @MockBean
    private final UserMapperDTO userMapperDTO;

    @MockBean
    private Model model;

    MockMvc mockMvc;

    @Test
    void testGetCompanyRegistryCorrectly() throws Exception {
        // Given

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get(COMPANY_REGISTRY);

        // When, Then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("company_registry"));

    }




    @Test
    public void testCreateCompanyCorrectly() throws Exception {
        // Given
        UserDTO userDTO = UserFixtures.someUserDTO2();
        when(userService.createCompany(any())).thenReturn(null);

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.post("/companyRegistry")
                        .param("username", userDTO.getUserName()))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("company_created_successfully"));
    }

    @Test
    public void testCreateCompanyWrong() throws Exception {
        // Given
        UserDTO userDTO = UserFixtures.someUserDTO2();
        userDTO.setEmail(null);
        when(userService.createCompany(any())).thenReturn(null); // Ustaw, że metoda createCompany zwraca null

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.post("/wrongUrl")  // Zmień adres URL na niepoprawny
                        .param("username", userDTO.getUserName()))

                .andExpect(MockMvcResultMatchers.status().isNotFound());  // Oczekuj statusu 404
    }

    @Test
    void testGetCandidateRegistryCorrectly() throws Exception {
        // Given

        MockHttpServletRequestBuilder getRequest = MockMvcRequestBuilders.get(CANDIDATE_REGISTRY);

        // When, Then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("candidate_registry"));

    }

    @Test
    public void testCreateCandidateCorrectly() throws Exception {
        // Given
        UserDTO userDTO = UserFixtures.someUserDTO1();

        when(userService.createCandidate(any())).thenReturn(null); // Ustaw, że metoda createCompany zwraca null

        // When, Then
        mockMvc.perform(MockMvcRequestBuilders.post("/candidateRegistry")
                        .param("username", userDTO.getUserName()))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.view().name("candidate_created_successfully"));
    }



}