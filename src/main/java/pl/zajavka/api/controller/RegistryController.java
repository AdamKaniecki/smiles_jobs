package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.User;
//@SessionAttributes("username")
@AllArgsConstructor
@Controller
public class RegistryController {
//

    public static final String CANDIDATE_REGISTRY = "/candidate_registry";
    public static final String COMPANY_REGISTRY = "/company_registry";
    private final UserService userService;
    private final UserMapperDTO userMapperDTO;


    @GetMapping(COMPANY_REGISTRY)
    public String getCompanyRegistry(UserDTO userDTO) {
        return "company_registry";
    }


    @GetMapping(CANDIDATE_REGISTRY)
    public String getCandidateRegistry(UserDTO userDTO) {
        return "candidate_registry";
    }



    @PostMapping("/candidateRegistry")
    public String createCandidate(
            @Valid @ModelAttribute("username") UserDTO userDTO, Model model, HttpSession session) {
       User user = userMapperDTO.map(userDTO);
        // Utwórz kandydata w bazie danych
        userService.createCandidate(user);

        // Zapisz użytkownika w sesji
        session.setAttribute("userSession", userDTO);

        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
        model.addAttribute("user", userDTO);

        return "candidate_created_successfully";
    }

    @PostMapping("/companyRegistry")
    public String createCompany(@ModelAttribute("username") User user, Model model, HttpSession session) {
        // Utwórz kandydata w bazie danych
        userService.createCompany(user);

        // Zapisz użytkownika w sesji
        session.setAttribute("userSession", user);

        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
        model.addAttribute("user", user);

        return "company_created_successfully";
    }
}
