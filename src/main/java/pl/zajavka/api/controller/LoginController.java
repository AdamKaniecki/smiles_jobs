package pl.zajavka.api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/loginUser")
    public String loginUser(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

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
            System.out.println("Nieprawidłowe dane logowania.");
            return "login";
        }
    }

}
