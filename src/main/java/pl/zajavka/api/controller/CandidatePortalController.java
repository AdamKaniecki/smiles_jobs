package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.api.dto.mapper.MapperDTO;
import pl.zajavka.business.AdvertisementService;
import pl.zajavka.business.JobOfferService;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

import java.util.List;

@AllArgsConstructor
@Controller
@Slf4j
public class CandidatePortalController {
    public static final String CANDIDATE_PORTAL = "{user}/candidate_portal";
    public static final String CREATE_ADVERTISEMENT = "/create_advertisement";

    //    public static final String USER_ID = "/show/{userId}";
    public static final String USER_ID = "/show";
    private MapperDTO mapperDTO;
    private HttpSession httpSession;

    private UserService userService;

    private UserRepository userRepository;
    private UserMapper userMapper;
    private AdvertisementService advertisementService;
    private JobOfferService jobOfferService;
//    private NotificationService notificationService;


    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Użytkownik jest zalogowany
            model.addAttribute("user", user);
            List<JobOfferEntity> jobOffers = jobOfferService.getAllJobOffers();
            model.addAttribute("jobOffers", jobOffers);
            return "candidate_portal";
        } else {
            // Użytkownik nie jest zalogowany, przekieruj na stronę logowania
            return "redirect:/login";
        }
    }


    @GetMapping(CREATE_ADVERTISEMENT)
    public String createAdvertisementForm(Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            return "create_advertisement";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }


    @PostMapping("/createAdvertisement")
    public String createdAdvertisement(
            @ModelAttribute("advertisement") Advertisement advertisement,
            Model model) {
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            advertisement.setUser(loggedInUser);
            advertisementService.create(advertisement, loggedInUser);

            // Dodaj reklamę do modelu, aby przekazać ją do widoku
            model.addAttribute("advertisement", advertisement);
            model.addAttribute("user", loggedInUser);

            return "advertisement_created_successfully";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }
    @GetMapping("/searchJobOffers")
    public String searchJobOffers(
            @RequestParam("keyword") String keyword,
            @RequestParam("category") String category,
            Model model) {
        List<JobOfferEntity> searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "search_job_offers_results"; // Twój widok do wyświetlania wyników wyszukiwania ofert pracy
    }

    @GetMapping("/search_job_offers_results")
    public String showSearchResults(@RequestParam String keyword, String category, Model model) {
        List<JobOfferEntity> searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(category,keyword);
        model.addAttribute("searchResults", searchResults);
        return "search_job_offers_results";
    }
}