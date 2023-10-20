package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.MapperSecurity;

@Service
@AllArgsConstructor
public class UserService {
   private final UserRepository userRepository;
   private final MapperSecurity mapperSecurity;

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

}
