package pl.zajavka.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserJpaRepository;
import pl.zajavka.integration.AbstractJpaIT;
import pl.zajavka.util.UserFixtures;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.zajavka.util.UserFixtures.*;


@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UserRepositoryDataJpaTest extends AbstractJpaIT {



    @Autowired
    private UserJpaRepository userRepository;

    @Test
    void findByUserName_ReturnsUser_WhenUserExists() {
        // Given
        UserEntity userEntity = UserFixtures.someUserEntity1();
        userRepository.save(userEntity);

        // When
        UserEntity foundUser = userRepository.findByUserName(userEntity.getUserName());

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserName()).isEqualTo(userEntity.getUserName());
    }



    @Test
    void thatUserCanBeSavedCorrectly(){
//        given
        var users = List.of(someUserEntity1(), someUserEntity2(), someUserEntity3());
        userRepository.saveAll(users);
//        when
        List<UserEntity> usersFound = userRepository.findAll();
//        then
        assertThat(usersFound.size()).isEqualTo(3);
    }


    @Test
    void thatUserCanBeFindByUsernameCorrectly(){
//        given
        UserEntity user = UserFixtures.someUserEntity1();
        userRepository.save(user);
//        when
        UserEntity userFound = userRepository.findByUserName("adam12");
        UserEntity nonExistentUser = userRepository.findByUserName("nonexistent");
//        then
        assertThat(userFound.getUserName()).isEqualTo(user.getUserName());
        assertThat(nonExistentUser).isNull();

    }

    @Test
    void thatUserExistByEmailCorrectly(){
//        given
        UserEntity user = UserFixtures.someUserEntity1();
        userRepository.save(user);

//        when
        boolean existsByEmail = userRepository.existsByEmail("adam2113@poczta.onet.pl");
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
//        then
        assertThat(existsByEmail).isTrue();
        assertThat(exists).isFalse();

    }

    @Test
    void thatUserCanBeFindByIdCorrectly(){
//        given
        UserEntity user = UserFixtures.someUserEntity1();
        userRepository.save(user);
//        when
        Optional<UserEntity> userFound = userRepository.findById(user.getId());
        Optional<UserEntity> userNotFound = userRepository.findById(user.getId() + 1); // Szukamy ID, które nie istnieje.
//        then
        assertThat(userFound).isPresent();
        assertThat(userFound.get().getId()).isEqualTo(user.getId());
        assertThat(userNotFound).isNotPresent(); // Oczekujemy, że użytkownik nie zostanie znaleziony.
    }
    @Test
    void thatUserCanBeDeletedCorrectly() {
        // given
        UserEntity user = UserFixtures.someUserEntity1();
        userRepository.save(user);

        // when
        userRepository.delete(user);

        // then
        Optional<UserEntity> userFound = userRepository.findById(user.getId());
        assertThat(userFound).isNotPresent(); // Oczekujemy, że użytkownik nie zostanie znaleziony po usunięciu.
    }

    @Test
    void thatUserCanBeUpdatedCorrectly() {
        // given
        UserEntity user = UserFixtures.someUserEntity1();
        String newEmail = "newemail@example.com";
        user.setEmail(newEmail);

        // when
        UserEntity updatedUser = userRepository.save(user);

        // then
        assertThat(updatedUser.getEmail()).isEqualTo(newEmail); // Oczekujemy, że email użytkownika zostanie zaktualizowany.
    }

    @Test
    void thatUserPasswordIsSetCorrectly() {
        // given
        UserEntity user = UserFixtures.someUserEntity1();
        String password = "password123";
        user.setPassword(password);

        // when
        userRepository.save(user);

        // then
        UserEntity savedUser = userRepository.findByUserName(user.getUserName());
        assertThat(savedUser.getPassword()).isEqualTo(password);
    }

    @Test
    void thatUserActiveIsSetCorrectly() {
        // given
        UserEntity user = UserFixtures.someUserEntity1();
        boolean active = true;
        user.setActive(active);

        // when
        userRepository.save(user);

        // then
        UserEntity savedUser = userRepository.findByUserName(user.getUserName());
        assertThat(savedUser.getActive()).isEqualTo(active);
    }






}







//        given
//        when
//        then