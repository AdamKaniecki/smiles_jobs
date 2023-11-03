package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;
@AllArgsConstructor
@Controller
@SessionAttributes("username")
public class LoginController {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserService userService;

    @GetMapping("/login")
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
    @PostMapping("/loginUser")
    public String loginUser(@RequestParam("username") String username, String password, Model model, HttpSession session) {
        // Sprawdź, czy użytkownik o podanej nazwie istnieje w bazie danych

        User user = userService.findByUserName(username);

        if (user != null && user.getPassword().equals(password) && user.getUserName().equals(username)) {
            // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
            session.setAttribute("user", user); // Przechowaj użytkownika w sesji
            System.out.println("Zalogowano pomyślnie");
            model.addAttribute("username", username);
            return "candidate_portal";
        } else {
            model.addAttribute("error", "Nieprawidłowe dane logowania.");
            return "login";
        }
    }



//}
//
//    @PostMapping("/login")
//    public String login(@RequestParam("username") String username, Model model) {
//        // Przykład: autentykacja użytkownika, jeśli poprawna, to ustawmy nazwę użytkownika w sesji
//        model.addAttribute("username", username);
//
//        // Przekierowanie użytkownika na stronę główną
//        return "redirect:/home";
//    }


}