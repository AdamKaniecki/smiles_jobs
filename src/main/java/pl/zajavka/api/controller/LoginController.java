//package pl.zajavka.api.controller;
//
//import jakarta.servlet.http.HttpSession;
//import lombok.AllArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.SessionAttributes;
//import pl.zajavka.business.UserService;
//import pl.zajavka.domain.User;
//import pl.zajavka.infrastructure.security.Role;
//import pl.zajavka.infrastructure.security.UserRepository;
//import pl.zajavka.infrastructure.security.mapper.UserMapper;
//
//@AllArgsConstructor
//@Controller
//@SessionAttributes("username")
//public class LoginController {
//    private UserRepository userRepository;
//    private UserMapper userMapper;
//    private UserService userService;
//
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
//
//    @PostMapping("/loginUser")
//    public String loginUser(@RequestParam("username") String username, String password, Model model, HttpSession session) {
//
//        User user = userService.findByUserName(username);
//
//        if (user != null
//                && user.getPassword().equals(password)
//                && user.getUserName().equals(username)
//                && user.getRoles().contains(Role.CANDIDATE)) {
//            // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
//            session.setAttribute("user", user); // Przechowaj użytkownika w sesji
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect: /candidate_portal";
//        } else {
//            if (user != null
//                    && user.getPassword().equals(password)
//                    && user.getUserName().equals(username)
//                    && user.getRoles().contains(Role.COMPANY)) {
//                // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
//                session.setAttribute("user", user); // Przechowaj użytkownika w sesji
//                System.out.println("Zalogowano Firmę pomyślnie");
//                model.addAttribute("username", username);
//                return "redirect: /company_portal";
//            } else {
//                System.out.println("Nieprawidłowe dane logowania.");
//                return "login";
//            }
//
//        }
//    }
//
//}

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
import pl.zajavka.infrastructure.security.Role;
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
        return "login";
    }


    @PostMapping("/loginUser")
    public String loginUser(@RequestParam("username") String username, String password, Model model, HttpSession session) {

        User user = userService.findByUserName(username);

        if (user != null
                && user.getPassword().equals(password)
                && user.getUserName().equals(username)
                && user.getRoles().contains(Role.CANDIDATE)) {
            // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
            session.setAttribute("user", user); // Przechowaj użytkownika w sesji
            System.out.println("Zalogowano Kandydata pomyślnie");
            model.addAttribute("username", username);
            return "redirect: /candidate_portal";
        } else {
            if (user != null
                    && user.getPassword().equals(password)
                    && user.getUserName().equals(username)
                    && user.getRoles().contains(Role.COMPANY)) {
                // Jeśli użytkownik istnieje i hasło jest poprawne, zaloguj użytkownika
                session.setAttribute("user", user); // Przechowaj użytkownika w sesji
                System.out.println("Zalogowano Firmę pomyślnie");
                model.addAttribute("username", username);
                return "redirect: /company_portal";
            } else {
                System.out.println("Nieprawidłowe dane logowania.");
                return "login";
            }

        }
    }

}