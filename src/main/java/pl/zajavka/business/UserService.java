package pl.zajavka.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.Role;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserEntity createCandidate(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(encodedPassword)
                .active(true)
                .roles(Set.of(Role.CANDIDATE))
                .build();
        return userRepository.save(userEntity);
    }

    @Transactional
    public UserEntity createCompany(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(encodedPassword)
                .active(true)
                .roles(Set.of(Role.COMPANY))
                .build();
        return userRepository.save(userEntity);
    }

    @Transactional
    public void addAdvertisementToUser(User user, Advertisement advertisement) {
        // Pobierz istniejące reklamy użytkownika

        Set<Advertisement> userAdvertisements = user.getAdvertisements();

        // Dodaj nową reklamę
        userAdvertisements.add(advertisement);

        // Zaktualizuj listę reklam użytkownika
        user.setAdvertisements(userAdvertisements);
        UserEntity userEntity = userMapper.map(user);

        // Zapisz użytkownika w bazie danych
        userRepository.save(userEntity);
    }





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

        // itd. Zaktualizuj pozostałe pola według potrzeb

        return userMapper.map(userRepository.save(userEntity));
    }

    @Transactional
    public void deleteUser(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Brak użytkownika o userId: " + userId));
        userRepository.delete(userEntity);
    }

    public User getUserById(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Brak użytkownika o userId: " + userId));

        return userMapper.map(userEntity);
    }

    public List<User> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream()
                .map(userMapper::map)
                .collect(Collectors.toList());
    }

}




