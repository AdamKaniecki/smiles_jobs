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


//    @PostMapping("/company_registry")
//    public String createCompany (@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
//       User user = mapperDTO.map(userDTO);
//        userService.createCompany(user);
//        model.addAttribute("userDTO", userDTO);
//        return "user_created_successfully";
//    }

//    @PostMapping("/candidate_registry")
//    public String createCandidate (@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
//        User user = mapperDTO.map(userDTO);
//        userService.createCandidate(user);
//        model.addAttribute("userDTO", userDTO);
//        return "user_created_successfully";
//    }
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

        return "company_created_successfully";
    }


//    @GetMapping("/companyRegistry")
//    public String createCandidate(@ModelAttribute("user") User user, Model model, Authentication authentication) {
//        // Możesz uzyskać dostęp do Principal w tym miejscu
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        String username = userDetails.getUsername();
//
//        // Teraz możesz użyć 'username' lub userDetails w zależności od swoich potrzeb
//        userService.createCandidate(user,username);
//        model.addAttribute("user", user);
//        return "user_created_successfully";
//    }


}
