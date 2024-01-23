package pl.zajavka.business;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.webjars.NotFoundException;
import pl.zajavka.api.dto.mapper.BusinessCardMapperDTO;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.security.*;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class  UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private RoleRepository roleRepository;
    private SmilesJobsUserDetailsService smilesJobsUserDetailsService;
    private CvMapper cvMapper;

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
        return userRepository.findAll().stream().map(userMapper::map).toList();
    }


    @Transactional
    public User updateUser(User user) {
        UserEntity userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Brak użytkownika o userId: " + user.getId()));

        // Zaktualizuj pola użytkownika
        userEntity.setUserName(user.getUserName());
        userEntity.setEmail(user.getEmail());
//        userEntity.setAdvertisements(Set.of());

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
    public User getLoggedInUser(Authentication authentication) {
        UserEntity userEntity = userRepository.findByUserName(authentication.getName());
        if (userEntity == null) {
            System.out.println("i co ?34");
            return null;

        }
       return userMapper.map(userEntity);

}

    @SneakyThrows
    public String loginUser(Model model,  Authentication auth) {
        Authentication userAuth = SecurityContextHolder.getContext().getAuthentication();
        String username = userAuth.getName();



        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
            // Zalogowano Kandydata pomyślnie
            System.out.println("Zalogowano Kandydata pomyślnie");
            model.addAttribute("username", username);
            return "redirect:/candidate_portal";
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            // Zalogowano Firmę pomyślnie
            System.out.println("Zalogowano Firmę pomyślnie");
            model.addAttribute("username", username);
            return "redirect:/company_portal";
        } else {
            // Nieprawidłowe dane logowania
            System.out.println("Nieprawidłowe dane logowania.");
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }

    public User getUserByJobOffer(JobOffer jobOffer) {
        Integer jobOfferId = jobOffer.getId();
        UserEntity userEntity = userRepository.findById(jobOfferId)
                .orElseThrow(() -> new NotFoundException("User not found for JobOffer with ID: " + jobOfferId));
        return userMapper.map(userEntity);
    }

//    public RoleEntity getUserRole(User user) {
//        // Pobierz rolę użytkownika na podstawie jego nazwy użytkownika
//        UserEntity userEntity = userRepository.findByUserName(user.getUserName());
//
//        if (userEntity != null) {
//            return roleRepository.findByUsersContaining(userEntity);
//        }
//
//        return null;
//    }
}
