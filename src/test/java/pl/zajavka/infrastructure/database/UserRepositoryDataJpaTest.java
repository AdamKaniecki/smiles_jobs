package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.zajavka.util.UserFixtures.*;

//@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryDataJpaTest extends AbstractJpaIT{

    private UserRepository userRepository;

    @Test
    void thatUserCanBeSavedCorrectly(){
        var users = List.of(someUser1(), someUser2()
                , someUser3()
        );
        userRepository.saveAll(users);
//        when
        List<UserEntity> usersFound = userRepository.findAll();
//        then
        assertThat(usersFound.size()).isEqualTo(3);
    }





}


