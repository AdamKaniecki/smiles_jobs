package pl.zajavka.api.controller.rest;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.zajavka.controller.api.BusinessCardRestController;
import pl.zajavka.controller.api.UserRestController;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.UserFixtures;
import wiremock.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.http.RequestEntity.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = UserRestController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserRestControllerWebMvcTest {

    @MockBean
    private UserService userService;
    @MockBean
    private UserMapperDTO userMapperDTO;
    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();  // Inicjalizacja ObjectMapper przed użyciem
    }



//    @Test
//    public void shouldReturnUsersList() throws Exception {
//        // Przygotowanie danych testowych
//        User user1 = UserFixtures.someUser1();
//        User user2 = UserFixtures.someUser1();
//
//        UserDTO userDTO1 = new UserDTO();
//        UserDTO userDTO2 = new UserDTO();
//
//        // Mockowanie serwisu i mappera
//        when(userService.findUsers()).thenReturn(List.of(user1, user2));
//        when(userMapperDTO.map(user1)).thenReturn(userDTO1);
//        when(userMapperDTO.map(user2)).thenReturn(userDTO2);
//
//        // Wykonanie żądania GET na endpoint
//        mockMvc.perform(get("/api/users") // Zastąp "/your-endpoint" rzeczywistą ścieżką
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk()) // Sprawdzenie, czy status odpowiedzi to 200 OK
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
//    }
//

    @Test
    public void testAddCandidate() throws Exception {

        // Przykładowe dane wejściowe
        UserDTO userDTO = new UserDTO();
        User createdUser = new User();
        // Mockowanie
        when(userMapperDTO.map(any(UserDTO.class))).thenReturn(createdUser);
        when(userService.createCandidate(any(User.class))).thenReturn(createdUser);

        // Wykonanie żądania POST i weryfikacja wyniku
        mockMvc.perform(MockMvcRequestBuilders.post("/api/createCandidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }


    @Test
    public void testAddCompany() throws Exception {

        // Przykładowe dane wejściowe
        UserDTO userDTO = new UserDTO();
        User createdUser = new User();
        // Mockowanie
        when(userMapperDTO.map(any(UserDTO.class))).thenReturn(createdUser);
        when(userService.createCompany(any(User.class))).thenReturn(createdUser);

        // Wykonanie żądania POST i weryfikacja wyniku
        mockMvc.perform(MockMvcRequestBuilders.post("/api/createCompany")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }


    @Test
    public void testUpdateCandidateSuccess() throws Exception {
        // Mockowanie danych wejściowych
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("newUsername");
        userDTO.setActive(true);
        userDTO.setEmail("newemail@example.com");
        User loggedInUser = new User();


        when(userService.findByUserName("testUser")).thenReturn(loggedInUser);
        // Wykonanie żądania PUT i weryfikacja odpowiedzi
        mockMvc.perform(MockMvcRequestBuilders.put("/api/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .principal(authentication))
                .andExpect(status().isOk());

        // Weryfikacja, czy dane użytkownika zostały zaktualizowane
        verify(userService).updateUser(any(User.class));
    }

    @Test
    public void testUpdateCandidateFailure() throws Exception {
        // Mockowanie danych wejściowych
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn(username);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("newUsername");
        userDTO.setActive(true);
        userDTO.setEmail("newemail@example.com");

//        when(authentication.getName()).thenReturn("currentUsername");
        when(userService.findByUserName("testUser")).thenReturn(new User());

        // Symulacja wyjątku podczas aktualizacji użytkownika
        doThrow(new RuntimeException("Update failed")).when(userService).updateUser(any(User.class));

        // Wykonanie żądania PUT i weryfikacja odpowiedzi
        mockMvc.perform(MockMvcRequestBuilders.put("/api/updateUser")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO))
                        .principal(authentication))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("An error occurred while updating user data"));
    }

    @Test
    public void testGetNotifications() throws Exception {
        // Mockowanie danych wejściowych
        String username = "testUser";
        Authentication authentication = Mockito.mock(Authentication.class);
        User loggedInUser = new User();
        loggedInUser.setUserName(username);

        NotificationDTO notification1 = new NotificationDTO();
        NotificationDTO notification2 = new NotificationDTO();
        List<NotificationDTO> notifications = List.of(notification1, notification2);

        when(authentication.getName()).thenReturn(username);
        when(userService.findByUserName(username)).thenReturn(loggedInUser);
        when(notificationService.findByUser(loggedInUser)).thenReturn(notifications);

        // Wykonanie żądania GET i weryfikacja odpowiedzi
        mockMvc.perform(get("/api/myNotifications")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(notifications)));
    }

}
