package pl.zajavka.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.Role;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class  UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public User createCandidate(User user) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(user.getPassword());

        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(user.getPassword())
//                .password(encodedPassword)
                .active(true)
                .roles(Set.of(Role.CANDIDATE))
                .build();

        userRepository.save(userEntity);
        return  userMapper.map(userEntity);

    }

    @Transactional
    public UserEntity createCompany(User user) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(user.getPassword());

        UserEntity userEntity = UserEntity.builder()
//                .id(userId)
                .userName(user.getUserName())
                .email(user.getEmail())
//                .password(encodedPassword)
                .password(user.getPassword())
                .active(true)
                .roles(Set.of(Role.COMPANY))
                .build();
        return userRepository.save(userEntity);
    }

//

    public List<User> findUsers() {
        return userRepository.findAll().stream().map(userMapper::map).toList();
    }


    @Transactional
    public User updateUser(User user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Brak użytkownika o userId: " + user.getId()));

        // Zaktualizuj pola użytkownika
        userEntity.setUserName(user.getUserName());
        userEntity.setEmail(user.getEmail());
        userEntity.setAdvertisements(Set.of());

        // itd. Zaktualizuj pozostałe pola według potrzeb

        return userMapper.map(userRepository.save(userEntity));
    }

    @Transactional
    public void deleteUser(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Brak użytkownika o userId: " + userId));
        userRepository.delete(userEntity);
    }

    @Transactional
    public User findById(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Brak użytkownika o userId: " + userId));
        return userMapper.map(userEntity);
    }


    //    public List<User> getAllUsers() {
//        List<UserEntity> userEntities = userRepository.findAll();
//        return userEntities.stream()
//                .map(userMapper::map)
//                .collect(Collectors.toList());
//    }
    @Transactional
    public User createUser(User user) {

        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
//                .password(encodedPassword)
                .password(user.getPassword())
                .active(true)
                .roles(Set.of(Role.COMPANY))
                .build();
//    UserEntity userEntity = userMapper.map(user);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return userMapper.map(savedUserEntity);
    }
    @Transactional
    public User findByUserName(String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (userEntity == null) {
            // Jeśli użytkownik o podanej nazwie nie istnieje, możesz zwrócić null lub obsłużyć to inaczej
            return null;
        }
        return userMapper.map(userEntity);
    }
}