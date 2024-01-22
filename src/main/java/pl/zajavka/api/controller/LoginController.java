package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.Role;

@Controller
public class LoginController {
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }


//    @GetMapping("/loginUser")
//    public String loginUser(Model model, HttpSession session) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//
//            // Przypisz użytkownika do sesji
//            session.setAttribute("loggedInUser", userService.findByUserName(username));
//
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//
//            // Przypisz użytkownika do sesji
//            session.setAttribute("loggedInUser", userService.findByUserName(username));
//
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }


    @PostMapping("/loginUser")
    public String loginUser(Model model, HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
            // Zalogowano Kandydata pomyślnie
            System.out.println("Zalogowano Kandydata pomyślnie");
            model.addAttribute("username", username);

            // Przypisz użytkownika do sesji
//            session.setAttribute("loggedInUser", userService.findByUserName(username));

            return "redirect:/candidate_portal";
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            // Zalogowano Firmę pomyślnie
            System.out.println("Zalogowano Firmę pomyślnie");
            model.addAttribute("username", username);

            // Przypisz użytkownika do sesji
//            session.setAttribute("loggedInUser", userService.findByUserName(username));

            return "redirect:/company_portal";
        } else {
            System.out.println("Nieprawidłowe dane logowania.");
            return "login";
        }
    }
}






















//    @GetMapping("/loginUser")
//    public String loginUser(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }
//    @GetMapping("/loginUser")
//    public String loginUser(Model model) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String username = auth.getName();
//
//        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CANDIDATE"))) {
//            // Zalogowano Kandydata pomyślnie
//            System.out.println("Zalogowano Kandydata pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/candidate_portal";
//        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
//            // Zalogowano Firmę pomyślnie
//            System.out.println("Zalogowano Firmę pomyślnie");
//            model.addAttribute("username", username);
//            return "redirect:/company_portal";
//        } else {
//            System.out.println("Nieprawidłowe dane logowania.");
//            return "login";
//        }
//    }


