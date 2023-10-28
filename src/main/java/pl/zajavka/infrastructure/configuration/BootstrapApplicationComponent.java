package pl.zajavka.infrastructure.configuration;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.infrastructure.security.Role;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;

import java.util.Set;

@Slf4j
@Component
@AllArgsConstructor
public class BootstrapApplicationComponent implements ApplicationListener<ContextRefreshedEvent> {

    private UserRepository userRepository;


    @Override
    @Transactional
    public void onApplicationEvent(final @NonNull ContextRefreshedEvent event) {

        userRepository.deleteAll();
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode("totombak");
        String encodedPassword = "totobak";

        UserEntity user1 = userRepository.save(UserEntity.builder()
                .userName("ABBBBBB")
                .email("addf@frg")
                .password(encodedPassword)
                .active(true)
                .roles(Set.of(Role.CANDIDATE))
                .build());


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
}

