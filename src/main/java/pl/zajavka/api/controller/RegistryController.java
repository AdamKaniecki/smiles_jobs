package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.User;

@AllArgsConstructor
@Controller
public class RegistryController {


    public static final String CANDIDATE_REGISTRY = "/candidate_registry";
    public static final String COMPANY_REGISTRY = "/company_registry";
    private UserService userService;


    @GetMapping(COMPANY_REGISTRY)
    public String getCompanyRegistry(User user) {
        return "company_registry";
    }


    @GetMapping(CANDIDATE_REGISTRY)
    public String getCandidateRegistry(User user) {
        return "candidate_registry";
    }


    @PostMapping("/candidateRegistry")
    public String createCandidate(@ModelAttribute("user") User user, Model model, HttpSession session) {
        // Utwórz kandydata w bazie danych
        userService.createCandidate(user);

        // Zapisz użytkownika w sesji
        session.setAttribute("user", user);

        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
        model.addAttribute("user", user);

        return "redirect: /candidate_portal";
    }

    @PostMapping("/companyRegistry")
    public String createCompany(@ModelAttribute("user") User user, Model model, HttpSession session) {
        // Utwórz kandydata w bazie danych
        userService.createCompany(user);

        // Zapisz użytkownika w sesji
        session.setAttribute("user", user);

        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
        model.addAttribute("user", user);

        return "redirect: /company_portal";
    }


}
