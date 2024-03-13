package pl.zajavka.controller;


import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.util.UserFixtures;

import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(controllers = LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class LoginControllerWebMvcTest {

    @MockBean
    private UserService userService;

    private MockMvc mockMvc;

    @Test
    void loginPageWorksCorrectly() throws Exception {
//        given/when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

    }

    @Test
    public void testLoginUser_SuccessfulLoginAsCandidate() throws Exception {
        // given
        String username = "sampleUsername";
        String password = "samplePassword";
        User user = UserFixtures.someUser1();
        user.setUserName(username);
        user.setPassword(password);

        when(userService.findByUserName(username)).thenReturn(user);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/loginUser")
                        .param("username", username)
                        .param("password", password)
                        .session(new MockHttpSession()))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.view().name("candidate_portal"));
    }


    @Test
    public void testLoginUser_SuccessfulCompanyLogin() throws Exception {
        // Given
        String username = "sampleUsername";
        String password = "samplePassword";
        User user = UserFixtures.someUser2();
        user.setUserName(username);
        user.setPassword(password);

        when(userService.findByUserName(username)).thenReturn(user);

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/loginUser")
                        .param("username", username)
                        .param("password", password)
                        .session(new MockHttpSession()))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"))
                .andExpect(MockMvcResultMatchers.view().name("company_portal"));
    }

    @Test
    public void testLoginUser_UnsuccessfulLogin() throws Exception {
        // Given
        String username = "sampleUsername";
        String password = "wrongPassword";

        when(userService.findByUserName(username)).thenReturn(null);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.post("/loginUser")
                        .param("username", username)
                        .param("password", password)
                        .session(new MockHttpSession())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    @Test
    public void testLoginUser_UserHasNoRequiredRole() throws Exception {
        // Given
        String username = "sampleUsername";
        String password = "samplePassword";

        // Mock UserService to return a valid user without required role
        User user = new User();
        user.setUserName(username);
        user.setPassword(password);
        user.setRoles(Set.of( RoleEntity.builder().role("ROLE_WRONG").build()));
        when(userService.findByUserName(username)).thenReturn(user);

        // When/Then
        mockMvc.perform(MockMvcRequestBuilders.post("/loginUser")
                        .param("username", username)
                        .param("password", password)
                        .session(new MockHttpSession())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

}





















//    @Test
//    public void testLoginUser_FailedLoginInvalidCredentials() throws Exception {
//        // Przygotowanie danych testowych
//        String username = "sampleUsername";
//        String password = "samplePassword";
//        User user = UserFixtures.someUser1();
//        user.setUserName(username);
//        user.setPassword(password);
//        Mockito.when(userService.findByUserName(username)).thenReturn(null);
//
//        // Wywołanie kontrolera
//        mockMvc.perform(MockMvcRequestBuilders.post("/loginUser")
//                        .param("username", username)
//                        .param("password", password)
//                        .session(new MockHttpSession()))
//                // Oczekiwania
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
//                .andExpect(MockMvcResultMatchers.view().name("login"));
//    }
//
//    @Test
//    public void testLoginUser_FailedLoginUserWithoutRequiredRole() throws Exception {
//        // Przygotowanie danych testowych
//        String username = "sampleUsername";
//        String password = "samplePassword";
//        User user = UserFixtures.someUser1();
//        user.setUserName(username);
//        user.setPassword(password);
//
//        Mockito.when(userService.findByUserName(username)).thenReturn(user);
//
//        // Wywołanie kontrolera
//        mockMvc.perform(MockMvcRequestBuilders.post("/loginUser")
//                        .param("username", username)
//                        .param("password", password)
//                        .session(new MockHttpSession()))
//                // Oczekiwania
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.model().attributeExists("error"))
//                .andExpect(MockMvcResultMatchers.view().name("login"));
//    }
//
//}


//@Test
//    void loginWorksCorrectly() throws Exception {
////        given/when/then
//        mockMvc.perform(MockMvcRequestBuilders.post("/loginUser"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("candidate_portal"));
//
//    }