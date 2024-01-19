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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
import pl.zajavka.business.CvService;
import pl.zajavka.business.JobOfferService;
import pl.zajavka.business.NotificationService;
import pl.zajavka.business.UserService;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.Notification;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.Status;
import pl.zajavka.infrastructure.security.UserRepository;

import java.nio.file.AccessDeniedException;
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
    private CvService cvService;

    @SneakyThrows
    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage(Model model, Authentication authentication) {
        System.out.println("czy ty tu wchodzisz 3?");

        User loggedInUser = userService.getLoggedInUser((authentication));
        UserDTO userDTO = userMapperDTO.map(loggedInUser);
        model.addAttribute("userDTO", userDTO);

        List<JobOfferDTO> jobOfferDTOs = jobOfferService.findAll().stream()
                .map(jobOfferMapperDTO::map)
                .collect(Collectors.toList());
        model.addAttribute("jobOffersDTOs", jobOfferDTOs);

        List<NotificationDTO> notifications = notificationService.findByUser(loggedInUser).stream()
                .map(notificationMapperDTO::map)
                .collect(Collectors.toList());
        model.addAttribute("notifications", notifications);

        return "candidate_portal";
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


//    @PostMapping("/sendCV")
//    public String sendCV(@RequestParam("jobOfferId") Integer jobOfferId, Model model, Authentication authentication) {
//
//        try {
//            User loggedInUser = getLoggedInUser(authentication);
//            User adresat = getJobOfferByUser(jobOfferId);
//            JobOffer jobOffer =  getJobOfferByUser(adresat);
//            CV cv = getCvByUser(loggedInUser.getId());
//            Notification notification = notificationService.createNotification(jobOffer, cv, loggedInUser, adresat);
//
//            userService.save(loggedInUser);
//            userService.save(adresat);
//
//            return "cv_created_successfully";
//        } catch (AccessDeniedException e) {
//            log.error("Błąd dostępu: {}", e.getMessage());
//            return "redirect:/home";
//        }
//
//
//    }


//    @PostMapping("/sendCV")
//    public String sendCV(@RequestParam("jobOfferId") Integer jobOfferId, Model model, HttpSession httpSession) {
//        System.out.println("czy ty tu wchodzisz?");
//        String username = (String) httpSession.getAttribute("username");
//
//        if (username != null) {
//            User loggedInUser = userService.findByUserName(username);
//            System.out.println("czy ty tu wchodzisz?2");
//            if (loggedInUser != null) {
//                Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);
//                if (optionalJobOffer.isPresent()) {
//                    System.out.println("czy ty tu wchodzisz?3");
//                    JobOffer jobOffer = optionalJobOffer.get();
//
//                    System.out.println("czy ty tu wchodzisz?4");
//                    Optional<CV> myCV = cvService.findByUser(loggedInUser);
//                    if (myCV.isPresent()) {
//                        System.out.println("czy ty tu wchodzisz?5");
//                        CV cv = myCV.get();
//                        // Utwórz obiekt Notification
//                        System.out.println("czy ty tu wchodzisz?6");
//                        User adresat = jobOffer.getUser();
//                        Notification notification = notificationService.createNotification(jobOffer, cv, loggedInUser, adresat);
//
//                        userService.save(loggedInUser);
//                        userService.save(adresat);
//
//                    } else {
//
//                        return "cv_not_found";
//                    }
//                }
//            }
//        }
//        return "cv_created_successfully";
//
//    }

    @PostMapping("/sendCV")
    public String sendCV(@RequestParam("jobOfferId") Integer jobOfferId, Model model, Authentication authentication) {
        if (authentication != null) {
            User loggedInUser = userService.getLoggedInUser(authentication);

            if (loggedInUser != null) {
                Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);

                if (optionalJobOffer.isPresent()) {
                    JobOffer jobOffer = optionalJobOffer.get();

                    Optional<CV> myCV = cvService.findByUser(loggedInUser);

                    if (myCV.isPresent()) {
                        CV cv = myCV.get();

                        try {
                            // Utwórz obiekt Notification
                            Notification notification = notificationService.createNotification(jobOffer, cv, loggedInUser, jobOffer.getUser());

                            // Zapisz encje w odpowiedniej kolejności
//                            userService.save(loggedInUser);
//                            notificationService.save(notification);
                        } catch (Exception e) {
                            e.printStackTrace();
                            // Obsłuż błąd zapisu
                            return "error_while_saving";
                        }

                    } else {
                        return "cv_not_found";
                    }
                }
            }
        }
        return "cv_created_successfully";
    }






    @PostMapping("/changeMeetingDate")
    public String changeMeetingDate(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("jobOfferId") Integer jobOfferId,
            Authentication authentication
    ) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User loggedInUser = userService.findByUserName(userDetails.getUsername());

            if (loggedInUser != null) {
                Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);

                if (optionalJobOffer.isPresent()) {
                    JobOffer jobOffer = optionalJobOffer.get();
                    Notification notification = notificationService.findById(notificationId);
                    User adresat = jobOffer.getUser();

                    // Tutaj możesz dodać kod do zmiany pola companyMessage
                    notificationService.changeMeetingDate(notification, loggedInUser, adresat);

                    return "cv_created_successfully";
                }
            }
        }

        return "redirect:/home"; // Przekieruj w przypadku problemu
    }

    @PostMapping("/acceptMeetingDate")
    public String acceptNotification(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("jobOfferId") Integer jobOfferId,
            Authentication authentication
    ) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User loggedInUser = userService.findByUserName(userDetails.getUsername());

            if (loggedInUser != null) {
                Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);

                if (optionalJobOffer.isPresent()) {
                    JobOffer jobOffer = optionalJobOffer.get();
                    Notification notification = notificationService.findById(notificationId);
                    User adresat = jobOffer.getUser();

                    notificationService.acceptMeetingDateTime(notification, loggedInUser, adresat);

                    return "cv_created_successfully";
                }
            }
        }

        return "redirect:/home"; // Przekieruj w przypadku problemu
    }



}