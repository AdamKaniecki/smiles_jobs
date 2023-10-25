package pl.zajavka.infrastructure.security;

import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SmilesJobsUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (userEntity == null) {
            throw new UsernameNotFoundException("Użytkownik o nazwie " + userName + " nie istnieje");
        }

        // Mapowanie ról z UserEntity na SimpleGrantedAuthority
        Set<GrantedAuthority> authorities = userEntity.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toSet());

        // Tworzenie obiektu UserDetails na podstawie UserEntity
        UserDetails userDetails = User.builder()
                .username(userEntity.getUserName())
                .password(userEntity.getPassword())
                .authorities(authorities)
                .build();

        return userDetails;
    }
//    private List<GrantedAuthority> getUserAuthority(Set<RoleEntity> userRoles) {
//        return userRoles.stream()
//            .map(role -> new SimpleGrantedAuthority(role.getRole()))
//            .distinct()
//            .collect(Collectors.toList());
//    }
//
//    private UserDetails buildUserForAuthentication(UserEntity user, List<GrantedAuthority> authorities) {
//        return new User(
//            user.getUserName(),
//            user.getPassword(),
//            user.getActive(),
//            true,
//            true,
//            true,
//            authorities
//        );
//    }

//    @Override
//    public void afterPropertiesSet() throws Exception {
//
//    }
}

