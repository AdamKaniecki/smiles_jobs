package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
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

// to działa
//    @PostMapping("/candidateRegistry")
//    public String createCandidate(@ModelAttribute("userDTO") UserDTO userDTO, Model model, HttpSession session) {
//        // Utwórz kandydata w bazie danych
//        User user = userMapperDTO.map(userDTO);
//        userService.createCandidate(user);
//        // Zapisz użytkownika w sesji
//        session.setAttribute("userDTO", userDTO);
//        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
//        model.addAttribute("userDTO", userDTO);
//
//        return "candidate_created_successfully";
//    }

//    @PostMapping("/companyRegistry")
//    public String createCompany(@ModelAttribute("userDTO") UserDTO userDTO, Model model, HttpSession session) {
//        // Utwórz kandydata w bazie danych
//        User user = userMapperDTO.map(userDTO);
//        userService.createCompany(user);
//
//
////         Zapisz użytkownika w sesji
//        session.setAttribute("userDTO", userDTO);
//
//        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
//        model.addAttribute("userDTO", userDTO);
//
//        return "company_created_successfully";
//    }

//@PostMapping("/candidateRegistry")
//    public String createCandidate(@ModelAttribute("userDTO") UserDTO userDTO, Model model, HttpSession session) {
//        // Utwórz kandydata w bazie danych
//        User user = userMapperDTO.map(userDTO);
//        userService.createCandidate(user);
//
//        // Zapisz użytkownika w sesji
//        session.setAttribute("userDTO", userDTO);
//
//        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
//        model.addAttribute("userDTO", userDTO);
//
//        return "candidate_created_successfully";
//    }
//
//    @PostMapping("/companyRegistry")
//    public String createCompany(@ModelAttribute("userDTO") UserDTO userDTO, Model model, HttpSession session) {
//        // Utwórz kandydata w bazie danych
//        User user = userMapperDTO.map(userDTO);
//        userService.createCompany(user);
//
//
//        // Zapisz użytkownika w sesji
//        session.setAttribute("userDTO", userDTO);
//
//        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
//        model.addAttribute("userDTO", userDTO);
//
//        return "company_created_successfully";
//    }

//@PostMapping("/candidate_registry")
//public String createCandidate(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
//    User user = userMapperDTO.map(userDTO);
//    userService.createCandidate(user);
//    model.addAttribute("userDTO", userDTO);
//    System.out.println("czy tu 1?");
//    return "candidate_created_successfully";
//}

//    @PostMapping("/company_registry")
//    public String createCompany(@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
//        User user = userMapperDTO.map(userDTO);
//        userService.createCompany(user);
//        model.addAttribute("userDTO", userDTO);
//        return "company_created_successfully";
//    }

//    @PostMapping("/candidateRegistry")
//    public String createCandidate(@ModelAttribute("userDTO") UserDTO userDTO, Model model, HttpSession session) {
//        // Utwórz kandydata w bazie danych
//        User user = userMapperDTO.map(userDTO);
//        // Przypisz rolę "ROLE_CANDIDATE"
//        userService.createCandidate(user);
//
//        // Ustaw atrybut sesji do automatycznego logowania po przekierowaniu do strony logowania
////        session.setAttribute("loggedInUser", user.getUserName());
//
//        // Zapisz użytkownika w sesji
//        session.setAttribute("userDTO", userDTO);
//        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
//        model.addAttribute("userDTO", userDTO);
//
//        return "candidate_created_successfully";
//
//    }


    @PostMapping("/candidateRegistry")
    public String createCandidate(@ModelAttribute("username") User user, Model model, HttpSession session) {
        // Utwórz kandydata w bazie danych
        userService.createCandidate(user);

        // Zapisz użytkownika w sesji
        session.setAttribute("userSession", user);

        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
        model.addAttribute("user", user);

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
