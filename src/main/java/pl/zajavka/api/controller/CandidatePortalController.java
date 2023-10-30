package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.mapper.MapperDTO;
import pl.zajavka.business.AdvertisementService;
import pl.zajavka.business.UserService;
import pl.zajavka.business.UserSessionManager;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Controller
@Slf4j
public class CandidatePortalController {
    public static final String CANDIDATE_PORTAL = "/candidate_portal";
    public static final String CREATE_ADVERTISEMENT = "/create_advertisement";

//    public static final String USER_ID = "/show/{userId}";
    public static final String USER_ID = "/show";
    private MapperDTO mapperDTO;

    private UserService userService;

    private UserRepository userRepository;
    private UserMapper userMapper;
    private AdvertisementService advertisementService;
//    private final UserSessionManager userSessionManager;

//    @GetMapping("/candidate_portal")
//    public String getCandidatePortalPage(HttpServletRequest request, Model model) {
//        // Pobierz identyfikator sesji użytkownika
//        String sessionId = request.getSession().getId();
//
//        // Pobierz informacje o zalogowanym użytkowniku z UserSessionManager
//        User user = userSessionManager.getLoggedInUser(sessionId);
//
//        if (user != null) {
//            model.addAttribute("user", user);
//            return "candidate_portal";
//        } else {
//            // Obsłuż sytuację, gdy użytkownik nie jest zalogowany
//            return "error";
//        }
//    }

    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage() {
        return "candidate_portal";
    }

//        @GetMapping(CREATE_ADVERTISEMENT)
//    public String getCreateAdvertisement(User user) {
//        return "create_advertisement";
//    }


//    @GetMapping(USER_ID)
//    public String showUserDetails(
//            @PathVariable Integer userId,
//            Model model
//    ) {
//        System.out.println("test get by id - start");
//
//        User user = userService.findById(userId);
//        model.addAttribute("user", user);
//        System.out.println("test get by id");
//        return "user_details";
//    }

    @GetMapping(CREATE_ADVERTISEMENT)
    public String users(Model model){
        List<User> users = userService.findUsers();
        model.addAttribute("users", users);
//        model.addAttribute("updateEmployeeDTO", new UpdateEmployeeDTO());
        return "create_advertisement";
    }


    @PostMapping("/createAdvertisement")
    public String createdAdvertisement(
            @ModelAttribute("advertisementEntity") AdvertisementEntity advertisementEntity, UserEntity userEntity, String username,
            Model model) {

        UserEntity user1 = userRepository.findByUserName(username);
        advertisementEntity.setUser(user1);
        advertisementService.create(advertisementEntity);



        // Dodaj reklamę do modelu, aby przekazać ją do widoku
        model.addAttribute("advertisementEntity", advertisementEntity);
        model.addAttribute("user", userEntity);

        return "user_created_successfully";
    }


//    @PostMapping("/candidate_registry")
//    public String createCandidate (@ModelAttribute("userDTO") UserDTO userDTO, Model model) {
//        User user = mapperDTO.map(userDTO);
//        userService.createCandidate(user);
//        model.addAttribute("userDTO", userDTO);
//        return "user_created_successfully";
//    }
}






