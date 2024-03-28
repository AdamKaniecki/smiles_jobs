//package pl.zajavka.infrastructure.database;
//
//import lombok.AllArgsConstructor;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
//import pl.zajavka.infrastructure.security.RoleEntity;
//import pl.zajavka.infrastructure.security.UserEntity;
//import pl.zajavka.infrastructure.security.UserRepository;
//import pl.zajavka.util.JobOfferFixtures;
//import pl.zajavka.util.UserFixtures;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//import static pl.zajavka.util.UserFixtures.*;
//
////@AutoConfigureMockMvc(addFilters = false)
//@AllArgsConstructor(onConstructor = @__(@Autowired))
//public class UserRepositoryDataJpaTest extends AbstractJpaIT{
//
//    private UserRepository userRepository;
//
//    @Test
//    void thatUserCanBeSavedCorrectly(){
////        given
//        var users = List.of(someUserEntity1(), someUserEntity2(), someUserEntity3());
//        userRepository.saveAll(users);
////        when
//        List<UserEntity> usersFound = userRepository.findAll();
////        then
//        assertThat(usersFound.size()).isEqualTo(3);
//    }
//
//
//    @Test
//    void thatUserCanBeFindByUsernameCorrectly(){
////        given
//        UserEntity user = UserFixtures.someUserEntity1();
//        userRepository.save(user);
////        when
//        UserEntity userFound = userRepository.findByUserName("adam12");
//        UserEntity nonExistentUser = userRepository.findByUserName("nonexistent");
////        then
//        assertThat(userFound.getUserName()).isEqualTo(user.getUserName());
//        assertThat(nonExistentUser).isNull();
//
//    }
//
//    @Test
//    void thatUserExistByEmailCorrectly(){
////        given
//        UserEntity user = UserFixtures.someUserEntity1();
//        userRepository.save(user);
//
////        when
//        boolean existsByEmail = userRepository.existsByEmail("adam2113@poczta.onet.pl");
//        boolean exists = userRepository.existsByEmail("nonexistent@example.com");
////        then
//        assertThat(existsByEmail).isTrue();
//        assertThat(exists).isFalse();
//
//    }
//
//    @Test
//    void thatUserCanBeFindByIdCorrectly(){
////        given
//        UserEntity user = UserFixtures.someUserEntity1();
//        userRepository.save(user);
////        when
//        Optional<UserEntity> userFound = userRepository.findById(user.getId());
//        Optional<UserEntity> userNotFound = userRepository.findById(user.getId() + 1); // Szukamy ID, które nie istnieje.
////        then
//        assertThat(userFound).isPresent();
//        assertThat(userFound.get().getId()).isEqualTo(user.getId());
//        assertThat(userNotFound).isNotPresent(); // Oczekujemy, że użytkownik nie zostanie znaleziony.
//    }
//    @Test
//    void thatUserCanBeDeletedCorrectly() {
//        // given
//        UserEntity user = UserFixtures.someUserEntity1();
//        userRepository.save(user);
//
//        // when
//        userRepository.delete(user);
//
//        // then
//        Optional<UserEntity> userFound = userRepository.findById(user.getId());
//        assertThat(userFound).isNotPresent(); // Oczekujemy, że użytkownik nie zostanie znaleziony po usunięciu.
//    }
//
//    @Test
//    void thatUserCanBeUpdatedCorrectly() {
//        // given
//        UserEntity user = UserFixtures.someUserEntity1();
//        String newEmail = "newemail@example.com";
//        user.setEmail(newEmail);
//
//        // when
//        UserEntity updatedUser = userRepository.save(user);
//
//        // then
//        assertThat(updatedUser.getEmail()).isEqualTo(newEmail); // Oczekujemy, że email użytkownika zostanie zaktualizowany.
//    }
//
//    @Test
//    void thatUserPasswordIsSetCorrectly() {
//        // given
//        UserEntity user = UserFixtures.someUserEntity1();
//        String password = "password123";
//        user.setPassword(password);
//
//        // when
//        userRepository.save(user);
//
//        // then
//        UserEntity savedUser = userRepository.findByUserName(user.getUserName());
//        assertThat(savedUser.getPassword()).isEqualTo(password);
//    }
//
//    @Test
//    void thatUserActiveIsSetCorrectly() {
//        // given
//        UserEntity user = UserFixtures.someUserEntity1();
//        boolean active = true;
//        user.setActive(active);
//
//        // when
//        userRepository.save(user);
//
//        // then
//        UserEntity savedUser = userRepository.findByUserName(user.getUserName());
//        assertThat(savedUser.getActive()).isEqualTo(active);
//    }
//
//
//
//
//
//
//}
//
//
//
//
//
//
//
////        given
////        when
////        then