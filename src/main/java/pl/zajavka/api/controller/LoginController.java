package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
@AllArgsConstructor
@Controller
public class LoginController {
    private UserRepository userRepository;
    private UserMapper userMapper;

    @GetMapping("/candidate_portal/login")
    public String login() {
        return "login"; // Zwraca widok "login.html"
    }

//    @PostMapping("/login")
//    public String loginUser(Model model, String username, String password) {
//        // Tutaj możesz zaimplementować logikę logowania użytkownika
//        // Sprawdź, czy wprowadzone dane logowania są poprawne i zaloguj użytkownika
//        if ("ABBBBBB".equals(username) && "totobak".equals(password)) {
//            model.addAttribute("message", "Zalogowano pomyślnie.");
//            return "candidate_portal";
//        } else {
//            model.addAttribute("error", "Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }
@PostMapping("/login")
public String loginUser(Model model, String username, String password) {
    // Sprawdź, czy użytkownik o podanej nazwie istnieje w bazie danych

    UserEntity user = userRepository.findByUserName(username);


    if (user != null && user.getPassword().equals(password)) {
        // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
        model.addAttribute("message", "Zalogowano pomyślnie.");
        return "candidate_portal";
    } else {
        model.addAttribute("error", "Nieprawidłowe dane logowania.");
        return "login";
    }
}
}