package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.User;

@AllArgsConstructor
@Controller
public class RegistryController {


    public static final String CANDIDATE_REGISTRY = "/candidate_registry";
    public static final String COMPANY_REGISTRY = "/company_registry";
    private UserService userService;
    private UserMapperDTO userMapperDTO;


    @GetMapping(COMPANY_REGISTRY)
    public String getCompanyRegistry(UserDTO userDTO) {
        return "company_registry";
    }


    @GetMapping(CANDIDATE_REGISTRY)
    public String getCandidateRegistry(UserDTO userDTO) {
        return "candidate_registry";
    }


    @PostMapping("/candidateRegistry")
    public String createCandidate(@ModelAttribute("userDTO") UserDTO userDTO, Model model, HttpSession session) {
        // Utwórz kandydata w bazie danych
        User user = userMapperDTO.map(userDTO);
        userService.createCandidate(user);

        // Zapisz użytkownika w sesji
        session.setAttribute("userDTO", userDTO);

        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
        model.addAttribute("userDTO", userDTO);

        return "redirect: /candidate_portal";
    }

    @PostMapping("/companyRegistry")
    public String createCompany(@ModelAttribute("userDTO") UserDTO userDTO, Model model, HttpSession session) {
        // Utwórz kandydata w bazie danych
        User user = userMapperDTO.map(userDTO);
        userService.createCompany(user);


        // Zapisz użytkownika w sesji
        session.setAttribute("userDTO", userDTO);

        // Dodaj użytkownika do modelu, jeśli to jest potrzebne
        model.addAttribute("userDTO", userDTO);

        return "redirect: /company_portal";
    }


}
