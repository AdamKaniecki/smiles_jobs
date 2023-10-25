package pl.zajavka.api.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.zajavka.api.dto.AdvertisementDTO;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.mapper.MapperDTO;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;

import java.util.List;

@AllArgsConstructor
@Controller
@Slf4j
public class CandidatePortalController {
    public static final String CANDIDATE_PORTAL = "/candidate_portal";
    public static final String CREATE_ADVERTISEMENT = "/create_advertisement";

    public static final String USER_ID = "{userId}";
    private MapperDTO mapperDTO;

    private UserService userService;

    private UserRepository userRepository;

    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage() {
        return "candidate_portal";
    }

        @GetMapping(CREATE_ADVERTISEMENT)
    public String getCreateAdvertisement(User user) {
        return "create_advertisement";
    }
//    @GetMapping(CREATE_ADVERTISEMENT)
//    public String getCreateAdvertisement(
//            @PathVariable Integer userId, Model model
//    ) {
//        User user = userService.findById(userId);
//        model.addAttribute("user", user);
//        return "create_advertisement";
//
//    }

    @GetMapping(USER_ID)
    public String showUserDetails(
            @PathVariable Integer userId,
            Model model
    ) {
        User user = userService.findById(userId);
        model.addAttribute("user", user);
        return "user_details";

    }

    @GetMapping
    public String users(Model model){
        List<User> users = userService.findUsers();
        model.addAttribute("users", users);
//        model.addAttribute("updateEmployeeDTO", new UpdateEmployeeDTO());
        return "users";
    }


    @PostMapping("/createAdvertisement")

    public String createAdvertisement(
            @PathVariable Integer userId,
            @ModelAttribute("userDTO") UserDTO userDTO, AdvertisementDTO advertisementDTO, Model model) {

        User user = mapperDTO.map(userDTO);
        Advertisement advertisement = mapperDTO.map(advertisementDTO);

        userService.addAdvertisementToUser(user, advertisement);
        return "user_created_successfully";
    }
}






