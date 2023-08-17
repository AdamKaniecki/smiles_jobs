package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ActiveProfiles("test")
@WebMvcTest(controllers = HomeController.class)
//wazne: ostatnie 5minut projektu  z warsztatu #20!!!!
//wylaczenie mvc podczas testow integracyjnych z security bo testy sie wydupcą:
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class HomeControllerTest {

//    private MockMvc mockMvc;
//
//    @Test
//    void homeWorksCorrectly() throws Exception {
//        // given, when, then
//        mockMvc.perform(get(HomeController.HOME))
//                .andExpect(status().isOk())
//                .andExpect(view().name("home"));
//    }

}