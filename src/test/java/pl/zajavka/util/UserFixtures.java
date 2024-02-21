package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;

import java.util.Set;

@UtilityClass
public class UserFixtures {



    public static UserEntity someUser1() {
        RoleEntity candidateRole = RoleEntity.builder().role("ROLE_CANDIDATE").build();
        UserEntity userEntity = UserEntity.builder()
                .userName("adam12")
                .roles(Set.of(candidateRole))
                .password("adam112")
                .email("adam2113@poczta.onet.pl")
                .active(true)
                .build();
        return userEntity;
    }

    public static UserEntity someUser2() {
        RoleEntity companyRole = RoleEntity.builder().role("ROLE_COMPANY").build();
        return UserEntity.builder()
                .userName("john34")
                .roles(Set.of(companyRole))
                .password("john34")
                .email("john34@example.com")
                .active(true)
                .build();
    }

    public static UserEntity someUser3() {
        RoleEntity candidateRole = RoleEntity.builder().role("ROLE_CANDIDATE").build();
        return UserEntity.builder()
                .userName("john35")
                .roles(Set.of(candidateRole))
                .password("john35")
                .email("john35@example.com")
                .active(true)
                .build();
    }
}