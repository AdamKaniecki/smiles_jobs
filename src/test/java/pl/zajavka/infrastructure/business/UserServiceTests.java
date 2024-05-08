package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.repository.jpa.CvJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.RoleRepository;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
import pl.zajavka.integration.AbstractIT;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static wiremock.com.google.common.base.Verify.verify;

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

    @Test
    public void testCreateCompany() {
        // Given
        User user = UserFixtures.someUser2();
        UserEntity savedUserEntity = UserFixtures.someUserEntity2();

        when(userMapper.map(any(UserEntity.class))).thenReturn(user);

        // When
        User createdUser = userService.createCompany(user);
        // Sprawdzamy, czy userRepository.save zwraca null
        assertNotNull(savedUserEntity);
        assertNotNull(user);
        assertNotNull(createdUser);

    }

    @Test
    public void testFindUsers() {
        // Given
        UserEntity userEntity1 = UserFixtures.someUserEntity1();
        UserEntity userEntity2 = UserFixtures.someUserEntity2();

        List<UserEntity> userEntities = List.of(userEntity1, userEntity2);

        List<User> expectedUsers = userEntities.stream()
                .map(userMapper::map)
                .collect(Collectors.toList());

        when(userRepository.findAll()).thenReturn(userEntities);
        when(userMapper.map(userEntities.get(0))).thenReturn(expectedUsers.get(0));
        when(userMapper.map(userEntities.get(1))).thenReturn(expectedUsers.get(1));

        // When
        List<User> foundUsers = userService.findUsers();

        // Then
        assertEquals(expectedUsers.size(), foundUsers.size());
        for (int i = 0; i < expectedUsers.size(); i++) {
            assertEquals(expectedUsers.get(i), foundUsers.get(i));
        }
    }


    @Test
    public void testUpdateUser_Success() {
        // Given
        User userToUpdate = UserFixtures.someUser1();
        userToUpdate.setId(1);
        UserEntity userEntityInDb = UserFixtures.someUserEntity1();
        userEntityInDb.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(userEntityInDb));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.map(userEntityInDb)).thenReturn(userToUpdate);

        // When
        User updatedUser = userService.updateUser(userToUpdate);

        // Then
        assertEquals(userToUpdate.getId(), updatedUser.getId());
        assertEquals(userToUpdate.getUserName(), updatedUser.getUserName());
        assertEquals(userToUpdate.getEmail(), updatedUser.getEmail());
        assertEquals(userToUpdate.getPassword(), updatedUser.getPassword());
        assertEquals(userToUpdate.getActive(), updatedUser.getActive());
    }

    @Test
    public void testUpdateUser_EntityNotFoundException() {
        // Given
        User userToUpdate = UserFixtures.someUser1();
        userToUpdate.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userToUpdate));
    }


    @Test
    public void testDeleteUser_Success() {
        // Given

        UserEntity userEntityInDb = UserFixtures.someUserEntity1();
        userEntityInDb.setId(1);
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntityInDb));

        // When
        userService.deleteUser(1);

        // Then
        Mockito.verify(userRepository).delete(userEntityInDb);
    }

    @Test
    public void testFindById_UserExists() {
        // Given
        int userId = 1;
        UserEntity userEntity = UserFixtures.someUserEntity1();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        // Mapping userEntity to UserDTO might be required depending on your implementation
        when(userMapper.map(userEntity)).thenReturn(UserFixtures.someUser1());

        // When
        User foundUser = userService.findById(userId);

        // Then
        assertEquals(userEntity.getId(), foundUser.getId());
        assertEquals(userEntity.getUserName(), foundUser.getUserName());
        // Add assertions for other fields as needed
    }


    @Test
    public void testFindById_UserNotFound() {
        // Given
        int userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(EntityNotFoundException.class, () -> userService.findById(userId));
    }

    @Test
    public void testFindByUserName_UserExists() {
        // Given
        String userName = "adam12";
        UserEntity userEntity = UserFixtures.someUserEntity1();
        when(userRepository.findByUserName(userName)).thenReturn(userEntity);
        when(userMapper.map(userEntity)).thenReturn(UserFixtures.someUser1());

        // When
        User foundUser = userService.findByUserName(userName);

        // Then
        assertEquals(userEntity.getUserName(), foundUser.getUserName());
    }

    @Test
    public void testFindByUserName_UserNotExists() {
        // Given
        String userName = "nonExistingUserName";
        when(userRepository.findByUserName(userName)).thenReturn(null);

        // When
        User foundUser = userService.findByUserName(userName);

        // Then
        assertNull(foundUser);
    }

    @Test
    public void testExistsByEmail_EmailExists() {
        // Given
        String email = "adam2113@poczta.onet.pl";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean exists = userService.existsByEmail(email);

        // Then
        assertTrue(exists);
    }


    @Test
    public void testExistsByEmail_EmailDoesNotExist() {
        // Given
        String email = "nonExistingEmail@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // When
        boolean exists = userService.existsByEmail(email);

        // Then
        assertFalse(exists);
    }

    @Test
    public void testSave() {
        // Given
        User user = UserFixtures.someUser1();
        UserEntity userEntity = UserFixtures.someUserEntity1();
        when(userMapper.map(user)).thenReturn(userEntity);

        // When
        userService.save(user);

        // Then
        Mockito.verify(userMapper).map(user);
        Mockito.verify(userRepository).saveAndFlush(userEntity);
    }

    @Test
    public void testGetUserByJobOffer_Success() {
        // Given
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        jobOffer.setId(1);

        UserEntity userEntity = UserFixtures.someUserEntity1();
        when(userRepository.findById(1)).thenReturn(Optional.of(userEntity));

        User expectedUser = UserFixtures.someUser1();
        when(userMapper.map(userEntity)).thenReturn(expectedUser);

        // When
        User result = userService.getUserByJobOffer(jobOffer);

        // Then
        assertEquals(expectedUser, result);
    }

    @Test
    public void testGetUserByJobOffer_EntityNotFound() {
        // Given
        JobOffer jobOffer = JobOfferFixtures.someJobOffer3();
        jobOffer.setId(2);

        when(userRepository.findById(2)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUserByJobOffer(jobOffer)
        );

        assertEquals("User not found for JobOffer with ID: 2", exception.getMessage());
    }

    @Test
    public void testGetUserByCv_Success() {
        // Given
        Integer cvId = 1;
        CvEntity cvEntity = CvFixtures.someCvEntity1();
        when(cvRepository.findById(cvId)).thenReturn(Optional.of(cvEntity));

        CV cv = CvFixtures.someCv1();
        when(cvMapper.map(cvEntity)).thenReturn(cv);

        User expectedUser = cv.getUser();

        // When
        User result = userService.getUserByCv(cvId);

        // Then
        assertEquals(expectedUser, result);
    }

    @Test
    public void testGetUserByCv_CvNotFound() {
        // Given
        Integer cvId = 2;
        when(cvRepository.findById(cvId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUserByCv(cvId)
        );

        assertEquals("Not found for CV with ID: 2", exception.getMessage());
    }
}
