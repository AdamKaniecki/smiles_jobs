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
import pl.zajavka.business.AdvertisementService;
import pl.zajavka.business.JobOfferService;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
@Slf4j
@AllArgsConstructor
@Controller
public class CompanyPortalController {

    public static final String COMPANY_PORTAL = "{user}/company_portal";
    public static final String CREATE_JOB_OFFER = "/create_job_offer";
    private AdvertisementService advertisementService;
    private HttpSession httpSession;
    private JobOfferService jobOfferService;
    private UserService userService;


//    @GetMapping(COMPANY_PORTAL)
//    public String getCandidatePortalPage() {
//        return "company_portal";
//    }


    @GetMapping(COMPANY_PORTAL)
    public String getCompanyPortalPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Użytkownik jest zalogowany
            model.addAttribute("user", user);
            List<AdvertisementEntity> advertisements = advertisementService.getAllAdvertisements();
            model.addAttribute("advertisements", advertisements);
            return "company_portal";
        } else {
            // Użytkownik nie jest zalogowany, przekieruj na stronę logowania
            return "redirect:/login";
        }
    }

    @GetMapping(CREATE_JOB_OFFER)
    public String createJobOfferForm(Model model) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            return "create_job_offer";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }
    }

    //    @GetMapping("/company_portal/showAdvertisements")
//    public String showAdvertisements(Model model) {
//
//        return "company_portal";
//    }
    @PostMapping("/createJobOffer")
    public String createdJobOffers(
            @ModelAttribute("jobOffer") JobOffer jobOffer,
            Model model) {
        log.info("Received job offer: {}", jobOffer);
        String username = (String) httpSession.getAttribute("username");

        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
//            jobOffer.setDateTime(OffsetDateTime.now());
            jobOffer.setUser(loggedInUser);
            jobOfferService.create(jobOffer, loggedInUser);

            // Dodaj reklamę do modelu, aby przekazać ją do widoku
            model.addAttribute("jobOffer", jobOffer);
            model.addAttribute("user", loggedInUser);

            return "user_created_successfully";
        } else {
            // Obsłuż brak zalogowanego użytkownika
            return "login";  // Przekieruj na stronę logowania
        }

    }

//    @GetMapping("/search")
//    public String searchAdvertisements(@RequestParam("keyword") String keyword, Model model) {
//        List<AdvertisementEntity> searchResults = advertisementService.searchAdvertisementsByKeyword(keyword);
//        model.addAttribute("searchResults", searchResults);
//        model.addAttribute("keyword", keyword);
//        return "search_results"; // Twój widok do wyświetlania wyników wyszukiwania
//    }
@GetMapping("/search")
public String searchAdvertisements(
        @RequestParam("keyword") String keyword,
        @RequestParam("category") String category,
        Model model) {
    List<AdvertisementEntity> searchResults = advertisementService.searchAdvertisementsByKeywordAndCategory(keyword, category);
    model.addAttribute("searchResults", searchResults);
    model.addAttribute("keyword", keyword);
    model.addAttribute("category", category);
    return "search_results";
}

    @GetMapping("/search_results")
    public String showSearchResults(@RequestParam String keyword, String category, Model model) {
        List<AdvertisementEntity> searchResults = advertisementService.searchAdvertisementsByKeywordAndCategory(category,keyword);
        model.addAttribute("searchResults", searchResults);
        return "search_results";
    }
}
