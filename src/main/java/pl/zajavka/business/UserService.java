package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.Role;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public UserEntity createCandidate(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(encodedPassword)
                .active(true)
                .roles(Set.of(Role.CANDIDATE))
                .build();

        return userRepository.save(userEntity);
    }

    }
//    static User initUser1() {
//        return User.builder()
//                .userName("AB")
//                .email("addf@frg")
////                .password
//                .active(true)
//                .build();
//    }


//@Transactional
//    public void createUser(){
//         User user = initUser1();
//           UserEntity userEntity = mapperSecurity.map(user);
//         userRepository.save(userEntity);
//    }


