package pl.zajavka.infrastructure.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class  UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private RoleRepository roleRepository;
    private CvMapper cvMapper;
    private CvJpaRepository cvRepository;


    @Transactional
    public User createCandidate(User user) {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());


        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity candidateRole = roleRepository.findByRole("ROLE_CANDIDATE");
        roles.add(candidateRole);

        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(encodedPassword)
                .active(true)
                .roles(roles)
                .build();

            userRepository.save(userEntity);
        return userMapper.map(userEntity);
    }


    @Transactional
    public User createCompany(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity companyRole = roleRepository.findByRole("ROLE_COMPANY");
        roles.add(companyRole);

        UserEntity userEntity = UserEntity.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(encodedPassword)
                .active(true)
                .roles(roles)
                .build();

       userRepository.save(userEntity);
       return userMapper.map(userEntity);
    }

//

    public List<User> findUsers() {
       List<User> users = userRepository.findAll().stream()
               .map(userMapper::map)
               .toList();
        return users;
    }


    @Transactional
    public User updateUser(User user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Not found entity User with ID: " + user.getId()));

        // Zaktualizuj pola użytkownika
        userEntity.setUserName(user.getUserName());
        userEntity.setEmail(user.getEmail());
        userEntity.setPassword(user.getPassword());
        userEntity.setActive(user.getActive());

        return userMapper.map(userRepository.save(userEntity));
    }

    @Transactional
    public void deleteUser(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Not found entity User with ID: " + userId));
        userRepository.delete(userEntity);
    }

    @Transactional
    public User findById(Integer userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Not found entity User with ID: " + userId));

        return userMapper.map(userEntity);

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

    public boolean existsByEmail(String email) {
        // Sprawdź, czy użytkownik o danym adresie e-mail już istnieje w bazie danych
        return userRepository.existsByEmail(email);
    }

    public void save(User user) {
        UserEntity userEntity = userMapper.map(user);
        userRepository.saveAndFlush(userEntity);
    }


    public User getUserByJobOffer(JobOffer jobOffer) {
        Integer jobOfferId = jobOffer.getId();
        UserEntity userEntity = userRepository.findById(jobOfferId)
                .orElseThrow(() -> new EntityNotFoundException("User not found for JobOffer with ID: " + jobOfferId));
        return userMapper.map(userEntity);
    }


    public User getUserByCv(Integer cvId) {
        CvEntity cvEntity = cvRepository.findById(cvId)
                .orElseThrow(()-> new EntityNotFoundException("Not found for CV with ID: " + cvId));
        CV cv = cvMapper.map(cvEntity);
        return cv.getUser();
    }



}
