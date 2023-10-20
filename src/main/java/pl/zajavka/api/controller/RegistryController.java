package pl.zajavka.api.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.zajavka.api.dto.mapper.MapperDTO;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.User;

@AllArgsConstructor
@Controller
public class RegistryController {


    public static final String CANDIDATE_REGISTRY = "/candidate_registry";
    public static final String COMPANY_REGISTRY = "/company_registry";
//    private UserMapper userMapper;
    private UserService userService;
    private MapperDTO mapperDTO;


    @GetMapping(COMPANY_REGISTRY)
    public String getCompanyRegistry() {
        return "company_registry";
    }

    
    @GetMapping(CANDIDATE_REGISTRY)
    public String showUserForm(User user) {
        return "candidate_registry";
    }


    @PostMapping("/candidate_registry")
    public String createCandidate (@ModelAttribute("user") User user, Model model) {

        userService.createCandidate(user);
        model.addAttribute("user", user);
        return "user_created_successfully";
    }


}
