package pl.zajavka.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.User;

@AllArgsConstructor
@Controller
public class RegistryController {


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
//            @Valid
            @ModelAttribute("username") UserDTO userDTO, Model model, HttpSession session) {
        User user = userMapperDTO.map(userDTO);

        userService.createCandidate(user);
        session.setAttribute("userSession", userDTO);
        model.addAttribute("user", userDTO);

        return "candidate_created_successfully";
    }

    @PostMapping("/companyRegistry")
    public String createCompany(@ModelAttribute("username") UserDTO userDTO, Model model, HttpSession session) {
        User user = userMapperDTO.map(userDTO);
        userService.createCompany(user);
        session.setAttribute("userSession", userDTO);
        model.addAttribute("user", userDTO);

        return "company_created_successfully";
    }
}
