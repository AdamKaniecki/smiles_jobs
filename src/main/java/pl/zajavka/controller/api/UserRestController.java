package pl.zajavka.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.UsersDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.User;

import java.net.URI;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserRestController {

    public static final String USER = "/users";
    public static final String USER_ID = "/{userId}";
    private static final String USER_ID_RESULT = "/%s";

    private final UserService userService;
    private UserMapperDTO userMapperDTO;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public UsersDTO usersList() {
        return UsersDTO.of(userService.findUsers().stream()
                .map(a -> userMapperDTO.map(a))
                .toList()
        );

    }

    @GetMapping(value = USER_ID, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO candidateDetails(@PathVariable Integer userId) {
        User user = userService.findById(userId);
        return userMapperDTO.map(user);

    }

    @PostMapping("/createCandidate")
    public ResponseEntity<UserDTO> addCandidate(
            @RequestBody UserDTO userDTO
    ) {
        User user = userMapperDTO.map(userDTO);
        User createdUser = userService.createCandidate(user);

        return ResponseEntity
                .created(URI.create(USER + USER_ID_RESULT.formatted(createdUser.getId())))
                .build();
    }
//    curl dodający Kandydata:
//    curl -i -H "Content-Type: application/json" -X POST http://localhost:8900/api/createCandidate -d "{\"userName\": \"john_doe\",\"email\": \"john.doe@example.com\",\"password\": \"secretpassword123\",\"active\": true,\"roles\": [{\"roleId\": 1}]}"

    @PostMapping("/createCompany")
    public ResponseEntity<UserDTO> addCompany(
            @RequestBody UserDTO userDTO


    ) {

        User user = userMapperDTO.map(userDTO);
        User createdUser = userService.createCompany(user);

        return ResponseEntity
                .created(URI.create(USER + USER_ID_RESULT.formatted(createdUser.getId())))
                .build();
    }
//    curl dodający Kandydata:
//    curl -i -H "Content-Type: application/json" -X POST http://localhost:8900/api/createCompany -d "{\"userName\": \"adam20\",\"email\": \"adam2120@poczta.onet.pl\",\"password\": \"adam20\",\"active\": true,\"roles\": [{\"roleId\": 2}]}"


    @PutMapping("/updateUser")
    public ResponseEntity<?> updateCandidate(
            @Valid @RequestBody UserDTO userDTO,
            Authentication authentication
    ) {
        try {
            String username = authentication.getName();
            User loggedInUser = userService.findByUserName(username);

            // Aktualizacja danych zalogowanego użytkownika na podstawie danych z userDTO
            loggedInUser.setUserName(userDTO.getUserName());
            loggedInUser.setActive(userDTO.getActive());
            loggedInUser.setEmail(userDTO.getEmail());
            // Dodaj inne pola do zaktualizowania

            userService.updateUser(loggedInUser);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating user data");
        }
    }

//    curl który aktualizuje Kandydata:
//curl -i -H "Content-Type: application/json" -X PUT http://localhost:8800/users/11 -d "{\"userName\": \"john_doe\",\"email\": \"john.doe@example.com\",\"password\": \"secretpassword123\",\"active\": true,\"roles\": [{\"roleId\": 1}]}"

}
