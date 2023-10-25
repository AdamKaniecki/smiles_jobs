package pl.zajavka.api.controller.restController;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.domain.User;
import pl.zajavka.domain.Users;
import pl.zajavka.infrastructure.security.Role;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping(UserController.USER)
@AllArgsConstructor
public class UserController {

    public static final String USER = "/user";
    public static final String USER_ID = "/{userId}";
    public static final String USER_ID_RESULT = "/%s";


    private UserRepository userRepository;
    private UserMapper userMapper;

    @GetMapping
    public Users usersList() {
        return Users.of(userRepository.findAll().stream()
                .map(userMapper::map)
                .toList());
    }

    @GetMapping(value = USER_ID, produces = {
//            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_JSON_VALUE})
    public User userDetails(@PathVariable Integer userId) {
        return userRepository.findById(userId)
                .map(userMapper::map)
                .orElseThrow(() -> new EntityNotFoundException(
                        "nima user takiego userId: [%s]".formatted(userId)
                ));
    }

//    @PostMapping
//    @Transactional
//    public ResponseEntity<User> addCandidate(
//            @RequestBody User user
//    ) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(user.getPassword());
//
//        User newUser = User.builder()
//                .userName(user.getUserName())
//                .email(user.getEmail())
//                .password(encodedPassword)
//                .active(true)
//                .roles(Set.of(Role.CANDIDATE))
//                .build();
//
//        UserEntity userEntity = userMapper.map(newUser);
//        UserEntity created = userRepository.save(userEntity);
//   return ResponseEntity
//           .created(URI.create(USER + USER_ID_RESULT.formatted(created.getId())))
//           .build();
//    }


}
