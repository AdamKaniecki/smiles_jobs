package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.Role;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.MapperSecurity;

import java.util.Set;

@AllArgsConstructor
@Controller
public class RegistryController {


    public static final String CANDIDATE_REGISTRY = "/candidate_registry";
    public static final String COMPANY_REGISTRY = "/company_registry";
    private MapperSecurity mapperSecurity;

    private UserRepository userRepository;


    @GetMapping(COMPANY_REGISTRY)
    public String getCompanyRegistry() {
        return "company_registry";
    }



    @GetMapping(CANDIDATE_REGISTRY)
    public String showUserForm(User user) {
        return "candidate_registry";
    }


    @PostMapping("/candidate_registry")
    public String createUser(@ModelAttribute("user") User user, Model model) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        User newUser = User.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .password(encodedPassword)
                .active(true)
                .roles(Set.of(Role.CANDIDATE))
                .build();

        UserEntity userEntity = mapperSecurity.map(newUser);
        UserEntity created = userRepository.save(userEntity);
        model.addAttribute("user", newUser);
        return "user_created_successfully";
    }



}
