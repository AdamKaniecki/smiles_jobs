package pl.zajavka.infrastructure.configuration;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;
import pl.zajavka.infrastructure.database.repository.AdvertisementRepository;
import pl.zajavka.infrastructure.security.Role;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class BootstrapApplicationComponent implements ApplicationListener<ContextRefreshedEvent> {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserService userService;
    private AdvertisementRepository advertisementRepository;


    @Override
    @Transactional
    public void onApplicationEvent(final @NonNull ContextRefreshedEvent event) {

        advertisementRepository.deleteAll();
        userRepository.deleteAll();

//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode("totombak");
        String encodedPassword = "totobak";

        User user = User.builder()

                .userName("ABBBBBB")
                .email("addf@frg")
                .password(encodedPassword)
                .active(true)
                .roles(Set.of(Role.CANDIDATE))
                .build();
        UserEntity userEntity = userMapper.map(user);
        userRepository.save(userEntity);

//     jak to jest odkomentowane to w wyniku działania tej metody przypisuje advertisement do usera,
//     a przez controllery nie może namierzyć usera.tworzy obiekt ale nie wiąże z userem

//        AdvertisementEntity advertisementEntity = AdvertisementEntity.builder()
//                .name("tyured")
//                .user(userRepository.findByUserName(user.getUserName()))
//                .build();
//        advertisementRepository.save(advertisementEntity);
    }



// to działą tylko tworzy encję
//        UserEntity userEntity = userRepository.save(UserEntity.builder()
//                .userName("ABBBBBB")
//                .email("addf@frg")
//                .password(encodedPassword)
//                .active(true)
//                .roles(Set.of(Role.CANDIDATE))
//                .build());
//        userMapper.map(userEntity);
//


//        BCryptPasswordEncoder passwordEncoder2 = new BCryptPasswordEncoder();
//        String encodedPassword2 = passwordEncoder2.encode("totombak2");
//
//        userRepository.save(UserEntity.builder()
//                .userName("BBBBBBB")
//                .email("add2f@frg")
//                .password(encodedPassword2)
//                .active(true)
//                .roles(Set.of(Role.COMPANY))
//                .build());
//
//    }
    }


