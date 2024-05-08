package pl.zajavka.infrastructure.business;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pl.zajavka.infrastructure.database.repository.jpa.CvJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.RoleRepository;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.UserFixtures;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserServiceTests extends AbstractIT {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private CvMapper cvMapper;
    @Mock
    private CvJpaRepository cvRepository;
    @InjectMocks
    private UserService userService;


    @Test
    public void testCreateCandidate() {
        // Given
        User user = UserFixtures.someUser1();
        UserEntity savedUserEntity = UserFixtures.someUserEntity1();

        when(userMapper.map(any(UserEntity.class))).thenReturn(user);

        // When
        User createdUser = userService.createCandidate(user);
        // Sprawdzamy, czy userRepository.save zwraca null
      assertNotNull(savedUserEntity);
      assertNotNull(user);
      assertNotNull(createdUser);

    }

}
