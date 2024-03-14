package pl.zajavka.controller.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.UsersDTO;
import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.controller.dto.mapper.NotificationMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.JobOfferService;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.User;

import java.net.URI;

@RestController
@RequestMapping(CandidatePortalRestController.USER)
@AllArgsConstructor
public class CandidatePortalRestController {

    public static final String USER = "/users";
    public static final String USER_ID = "/{userId}";
    private static final String USER_ID_RESULT = "/%s";

    private final UserService userService;
    private UserMapperDTO userMapperDTO;
    private JobOfferService jobOfferService;
    private JobOfferMapperDTO jobOfferMapperDTO;
    private NotificationService notificationService;
    private NotificationMapperDTO notificationMapperDTO;
    private CvService cvService;

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

    @PostMapping
//    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
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
//    curl -i -H "Content-Type: application/json" -X POST http://localhost:8800/users -d "{\"userName\": \"john_doe\",\"email\": \"john.doe@example.com\",\"password\": \"secretpassword123\",\"active\": true,\"roles\": [{\"roleId\": 1}]}"


    @PutMapping(USER_ID)
//    @PreAuthorize("hasAuthority('ROLE_CANDIDATE')")
    public ResponseEntity<?> updateCandidate(
            @PathVariable Integer userId,
           @Valid @RequestBody UserDTO userDTO
    ) {
        User user = userMapperDTO.map(userDTO);
        user.setId(userId); // Ustawiamy ID użytkownika na podstawie ścieżki
        userService.updateUser(user);

        return ResponseEntity.ok().build();
    }
//    curl który aktualizuje Kandydata:
//curl -i -H "Content-Type: application/json" -X PUT http://localhost:8800/users/11 -d "{\"userName\": \"john_doe\",\"email\": \"john.doe@example.com\",\"password\": \"secretpassword123\",\"active\": true,\"roles\": [{\"roleId\": 1}]}"
}
