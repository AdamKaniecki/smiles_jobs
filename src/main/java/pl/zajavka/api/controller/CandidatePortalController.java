//package pl.zajavka.api.controller;
//
//import jakarta.servlet.http.HttpSession;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import pl.zajavka.api.dto.JobOfferDTO;
//import pl.zajavka.api.dto.UserDTO;
//import pl.zajavka.api.dto.mapper.JobOfferMapperDTO;
//import pl.zajavka.api.dto.mapper.UserMapperDTO;
//import pl.zajavka.business.JobOfferService;
//import pl.zajavka.business.UserService;
//import pl.zajavka.domain.JobOffer;
//import pl.zajavka.domain.User;
//import pl.zajavka.infrastructure.security.UserRepository;
//
//import java.util.List;
//
//@AllArgsConstructor
//@Controller
//@Slf4j
//public class CandidatePortalController {
//    public static final String CANDIDATE_PORTAL = "{user}/candidate_portal";
//    private UserMapperDTO userMapperDTO;
//    private JobOfferService jobOfferService;
//    private JobOfferMapperDTO jobOfferMapperDTO;
////    private
//
//
//    @GetMapping(CANDIDATE_PORTAL)
//    public String getCandidatePortalPage(HttpSession session, Model model) {
//
//        User user = (User) session.getAttribute("user");
//        if (user != null) {
//
//            model.addAttribute("user", user);
//            UserDTO userDTO = userMapperDTO.map(user);
//            model.addAttribute("userDTO", userDTO);
//
//            List<JobOffer> jobOffers = jobOfferService.findAll();
//            List<JobOfferDTO> jobOfferDTOs = jobOffers.stream()
//                    .map(jobOfferMapperDTO::map)
//                    .toList();
//            model.addAttribute("jobOffersDTOs", jobOfferDTOs);
//
//            return "candidate_portal";
//        } else {
//            // Użytkownik nie jest zalogowany, przekieruj na stronę logowania
//            return "redirect:/login";
//        }
//    }
//
//
//
//    @GetMapping("/searchJobOffers")
//    public String searchJobOffers(
//            @RequestParam("keyword") String keyword,
//            @RequestParam("category") String category,
//            Model model) {
//        List<JobOffer> searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category);
//        model.addAttribute("searchResults", searchResults);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("category", category);
//        return "search_job_offers_results"; // Twój widok do wyświetlania wyników wyszukiwania ofert pracy
//    }
//
//    @GetMapping("/search_job_offers_results")
//    public String showSearchResults(@RequestParam String keyword, String category, Model model) {
//        List<JobOffer> searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(category,keyword);
//        model.addAttribute("searchResults", searchResults);
//        return "search_job_offers_results";
//    }
//}
package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.api.dto.CvDTO;
import pl.zajavka.api.dto.JobOfferDTO;
import pl.zajavka.api.dto.NotificationDTO;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.api.dto.mapper.NotificationMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.JobOfferService;
import pl.zajavka.business.NotificationService;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.Notification;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.security.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Controller
@Slf4j
public class CandidatePortalController {
    public static final String CANDIDATE_PORTAL = "{user}/candidate_portal";
    public static final String USER_ID = "/show";

    private HttpSession httpSession;
    private UserService userService;
    private UserRepository userRepository;
    private UserMapperDTO userMapperDTO;
    private JobOfferService jobOfferService;
    private JobOfferMapperDTO jobOfferMapperDTO;
    private NotificationService notificationService;
    private NotificationMapperDTO notificationMapperDTO;
//    private


    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage(HttpSession session, Model model) {
//        log.info("No co tam:",session, model);
//        User user = userMapperDTO.map(userDTO);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            // Użytkownik jest zalogowany
            model.addAttribute("user", user);
            UserDTO userDTO = userMapperDTO.map(user);
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("cvDTO", new CvDTO());  // Dodaj obiekt cvDTO do modelu, może być pusty, jeśli nie ma jeszcze CV

            List<JobOffer> jobOffers = jobOfferService.findAll();
            List<JobOfferDTO> jobOfferDTOs = jobOffers.stream()
                    .map(jobOfferMapperDTO::map)
                    .toList();
            model.addAttribute("jobOffersDTOs", jobOfferDTOs);

            List<Notification> userNotifications = notificationService.findByUser(user);
            List<NotificationDTO> notificationDTOs = userNotifications.stream()
                    .map(notificationMapperDTO::map)
                    .collect(Collectors.toList());
            model.addAttribute("notifications", notificationDTOs);

            return "candidate_portal";
        } else {
            // Użytkownik nie jest zalogowany, przekieruj na stronę logowania
            return "redirect:/login";
        }
    }


    @GetMapping("/searchJobOffers")
    public String searchJobOffers(
            @RequestParam("keyword") String keyword,
            @RequestParam("category") String category,
            Model model) {
        List<JobOffer> searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "search_job_offers_results"; // Twój widok do wyświetlania wyników wyszukiwania ofert pracy
    }

    @GetMapping("/search_job_offers_results")
    public String showSearchResults(@RequestParam String keyword, String category, Model model) {
        List<JobOffer> searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(category, keyword);
        model.addAttribute("searchResults", searchResults);
        return "search_job_offers_results";
    }

    @PostMapping("/acceptMeetingDate")
    public String acceptNotification(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("jobOfferId") Integer jobOfferId,
            HttpSession httpSession
    ) {
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            User loggedInUser = userService.findByUserName(username);
            System.out.println("czy ty tu wchodzisz?2");
            if (loggedInUser != null) {
                Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);
                if (optionalJobOffer.isPresent()) {
                    System.out.println("czy ty tu wchodzisz?3");
                    JobOffer jobOffer = optionalJobOffer.get();
                    Notification notification = notificationService.findById(notificationId);
                    User adresat = jobOffer.getUser();
                    notificationService.acceptMeetingDateTime(notification, loggedInUser, adresat);
                    return "cv_created_successfully";
                }
            }
        }
        return "home";
    }

}