////package pl.zajavka.api.controller;
////
////import jakarta.servlet.http.HttpSession;
////import lombok.AllArgsConstructor;
////import lombok.extern.slf4j.Slf4j;
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.web.bind.annotation.*;
////import pl.zajavka.api.dto.JobOfferDTO;
////import pl.zajavka.api.dto.UserDTO;
////import pl.zajavka.api.dto.mapper.CvMapperDTO;
////import pl.zajavka.api.dto.mapper.JobOfferMapperDTO;
////import pl.zajavka.api.dto.mapper.UserMapperDTO;
////import pl.zajavka.business.CvService;
////import pl.zajavka.business.JobOfferService;
////import pl.zajavka.business.UserService;
////import pl.zajavka.domain.CV;
////import pl.zajavka.domain.JobOffer;
////import pl.zajavka.domain.User;
////import pl.zajavka.infrastructure.database.entity.CvEntity;
////import pl.zajavka.infrastructure.security.mapper.UserMapper;
////
////import java.util.List;
////import java.util.Optional;
////
////@Slf4j
////@AllArgsConstructor
////@Controller
////public class CompanyPortalController {
////
////    public static final String COMPANY_PORTAL = "{user}/company_portal";
////
////    private UserMapperDTO userMapperDTO;
////    private CvService cvService;
////    private JobOfferMapperDTO jobOfferMapperDTO;
////    private CvMapperDTO cvMapperDTO;
////
////
////    @GetMapping(COMPANY_PORTAL)
////    public String getCompanyPortalPage(HttpSession session, Model model) {
////        User user = (User) session.getAttribute("user");
////        if (user != null) {
////            // Użytkownik jest zalogowany
////            model.addAttribute("user", user);
////
////            UserDTO userDTO = userMapperDTO.map(user);
////            model.addAttribute("userDTO", userDTO);
////
////            List<CV> cvList = cvService.findAll();
////            model.addAttribute("cvList", cvList);
////            return "company_portal";
////        } else {
////            // Użytkownik nie jest zalogowany, przekieruj na stronę logowania
////            return "redirect:/login";
////        }
////    }
////
////
////@GetMapping("/search")
////public String searchAdvertisements(
////        @RequestParam("keyword") String keyword,
////        @RequestParam("category") String category,
////        Model model) {
////    List<CV> searchResults = cvService.searchCvByKeywordAndCategory(keyword, category);
////    model.addAttribute("searchResults", searchResults);
////    model.addAttribute("keyword", keyword);
////    model.addAttribute("category", category);
////    return "search_results";
////}
////
////    @GetMapping("/search_results")
////    public String showSearchResults(@RequestParam String keyword, String category, Model model) {
////        List<CV> searchResults = cvService.searchCvByKeywordAndCategory(category,keyword);
////        model.addAttribute("searchResults", searchResults);
////        return "search_results";
////    }
////
////
////
////
////
////}
//package pl.zajavka.api.controller;
//
//import jakarta.servlet.http.HttpSession;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import pl.zajavka.api.dto.JobOfferDTO;
//import pl.zajavka.api.dto.UserDTO;
//import pl.zajavka.api.dto.mapper.CvMapperDTO;
//import pl.zajavka.api.dto.mapper.JobOfferMapperDTO;
//import pl.zajavka.api.dto.mapper.UserMapperDTO;
//import pl.zajavka.business.CvService;
//import pl.zajavka.business.JobOfferService;
//import pl.zajavka.business.UserService;
//import pl.zajavka.domain.CV;
//import pl.zajavka.domain.JobOffer;
//import pl.zajavka.domain.User;
//import pl.zajavka.infrastructure.database.entity.CvEntity;
//import pl.zajavka.infrastructure.security.mapper.UserMapper;
//
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@AllArgsConstructor
//@Controller
//public class CompanyPortalController {
//
//    public static final String COMPANY_PORTAL = "{user}/company_portal";
//    public static final String CREATE_JOB_OFFER = "/create_job_offer";
//    private HttpSession httpSession;
//    private JobOfferService jobOfferService;
//    private UserService userService;
//    private UserMapperDTO userMapperDTO;
//    private CvService cvService;
//    private JobOfferMapperDTO jobOfferMapperDTO;
//    private CvMapperDTO cvMapperDTO;
//
//
//    @GetMapping(COMPANY_PORTAL)
//    public String getCompanyPortalPage(HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        if (user != null) {
//            // Użytkownik jest zalogowany
//            model.addAttribute("user", user);
//
//            UserDTO userDTO = userMapperDTO.map(user);
//            model.addAttribute("userDTO", userDTO);
//
//            List<CV> cvList = cvService.findAll();
//            model.addAttribute("cvList", cvList);
//            return "company_portal";
//        } else {
//            // Użytkownik nie jest zalogowany, przekieruj na stronę logowania
//            return "redirect:/login";
//        }
//    }
//
//    @GetMapping(CREATE_JOB_OFFER)
//    public String createJobOfferForm(Model model) {
//        String username = (String) httpSession.getAttribute("username");
//        if (username != null) {
//            model.addAttribute("username", username);
//            return "create_job_offer";
//        } else {
//            // Obsłuż brak zalogowanego użytkownika
//            return "login";  // Przekieruj na stronę logowania
//        }
//    }
//
//
//    @PostMapping("/createJobOffer")
//    public String createdJobOffers(
//            @ModelAttribute("jobOfferDTO") JobOfferDTO jobOfferDTO,
//            Model model) {
////        log.info("Received job offer: {}", jobOffer);
//        String username = (String) httpSession.getAttribute("username");
//
//        if (username != null) {
//            User loggedInUser = userService.findByUserName(username);
////            jobOffer.setDateTime(OffsetDateTime.now());
//            JobOffer jobOffer = jobOfferMapperDTO.map(jobOfferDTO);
//
//            jobOffer.setUser(loggedInUser);
//            jobOfferService.create(jobOffer, loggedInUser);
////            jobOfferMapperDTO.map(jobOffer);
//
//            // Dodaj reklamę do modelu, aby przekazać ją do widoku
//            model.addAttribute("jobOfferDTO", jobOfferDTO);
//            model.addAttribute("user", loggedInUser);
//
//            return "job_offer_created_successfully";
//        } else {
//            // Obsłuż brak zalogowanego użytkownika
//            return "login";  // Przekieruj na stronę logowania
//        }
//
//    }
//
//
//    @GetMapping("/search")
//    public String searchAdvertisements(
//            @RequestParam("keyword") String keyword,
//            @RequestParam("category") String category,
//            Model model) {
//        List<CV> searchResults = cvService.searchCvByKeywordAndCategory(keyword, category);
//        model.addAttribute("searchResults", searchResults);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("category", category);
//        return "search_results";
//    }
//
//    @GetMapping("/search_results")
//    public String showSearchResults(@RequestParam String keyword, String category, Model model) {
//        List<CV> searchResults = cvService.searchCvByKeywordAndCategory(category,keyword);
//        model.addAttribute("searchResults", searchResults);
//        return "search_results";
//    }
//
////    @GetMapping("/cv/{cvId}")
////    public String showCvDetails(@PathVariable Integer cvId, Model model) {
////        Optional<CV> cv = cvService.findById(cvId);
////
////        if (cv.isPresent()) {
////            model.addAttribute("cv", cvMapperDTO.map(cv.get()));
////            return "cv_details";  // Stwórz odpowiedni widok dla szczegółów CV
////        } else {
////            return "cv_not_found";  // Stwórz odpowiedni widok dla przypadku, gdy CV nie zostanie znalezione
////        }
////    }
//
//    @GetMapping("/cv/{cvId}")
//    public String showCvDetails(@PathVariable Integer cvId, Model model) {
//        Optional<CV> cv = cvService.findById(cvId);
//
//        if (cv.isPresent()) {
//            model.addAttribute("cv", cvMapperDTO.map(cv.get()));
//            return "show_cv";  // Użyj istniejącego widoku show_cv
//        } else {
//            return "cv_not_found";  // Stwórz odpowiedni widok dla przypadku, gdy CV nie zostanie znalezione
//        }
//    }
//
//
//}
package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.zajavka.api.dto.NotificationDTO;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.mapper.*;
import pl.zajavka.business.*;
import pl.zajavka.domain.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Controller
public class CompanyPortalController {

    public static final String COMPANY_PORTAL = "{user}/company_portal";
    public static final String CREATE_JOB_OFFER = "/create_job_offer";

    private HttpSession httpSession;
    private JobOfferService jobOfferService;
    private UserService userService;
    private UserMapperDTO userMapperDTO;
    private CvService cvService;
    private JobOfferMapperDTO jobOfferMapperDTO;
    private CvMapperDTO cvMapperDTO;
    private BusinessCardService businessCardService;
    private BusinessCardMapperDTO businessCardMapperDTO;
    private NotificationService notificationService;
    private NotificationMapperDTO notificationMapperDTO;

    @GetMapping(COMPANY_PORTAL)
//    @PreAuthorize("hasAuthority('ROLE_COMPANY')")
    public String getCompanyPortalPage(Authentication authentication, Model model) {
        System.out.println("i co?");
        // Pobierz informacje o zalogowanym użytkowniku z obiektu Authentication
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByUserName(userDetails.getUsername());

            // Użytkownik jest zalogowany
            model.addAttribute("user", user);

            UserDTO userDTO = userMapperDTO.map(user);
            model.addAttribute("userDTO", userDTO);

            List<CV> cvList = cvService.findAll();
            model.addAttribute("cvList", cvList);

            List<NotificationDTO> notifications = notificationService.findByUser(user).stream()
                    .map(notificationMapperDTO::map)
                    .collect(Collectors.toList());
            model.addAttribute("notifications", notifications);

            return "company_portal";
        } else {
            // Użytkownik nie jest zalogowany, przekieruj na stronę logowania
            return "home";
        }
        }


        @GetMapping("/search")
        public String searchAdvertisements (
                @RequestParam("keyword") String keyword,
                @RequestParam("category") String category,
                Model model){
            List<CV> searchResults = cvService.searchCvByKeywordAndCategory(keyword, category);
            model.addAttribute("searchResults", searchResults);
            model.addAttribute("keyword", keyword);
            model.addAttribute("category", category);
            return "search_results";
        }

        @GetMapping("/search_results")
        public String showSearchResults (@RequestParam String keyword, String category, Model model){
            List<CV> searchResults = cvService.searchCvByKeywordAndCategory(category, keyword);
            model.addAttribute("searchResults", searchResults);
            return "search_results";
        }


        @GetMapping("/cv/{cvId}")
        public String showCvDetails (@PathVariable Integer cvId, Model model){
            Optional<CV> cv = cvService.findById(cvId);

            if (cv.isPresent()) {
                model.addAttribute("cvDTO", cvMapperDTO.map(cv.get()));
                return "show_cv";  // Użyj istniejącego widoku show_cv
            } else {
                return "cv_not_found";  // Stwórz odpowiedni widok dla przypadku, gdy CV nie zostanie znalezione
            }
        }


//        @PostMapping("/arrangeInterview")
//        public String arrangeInterview (
//                @RequestParam("jobOfferId") Integer jobOfferId,
//                @RequestParam("cvId") Integer cvId,
//                @RequestParam("notificationId") Integer notificationId,
//                @RequestParam("proposedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime
//        proposedDateTime,
//                HttpSession httpSession
//    ){
//            String username = (String) httpSession.getAttribute("username");
//            if (username != null) {
//                User loggedInUser = userService.findByUserName(username);
//                if (loggedInUser != null) {
//                    Optional<JobOffer> optionalJobOffer = jobOfferService.findByUser(loggedInUser);
//
//                    if (optionalJobOffer.isPresent()) {
//                        JobOffer jobOffer = optionalJobOffer.get();
//                        Optional<CV> myCV = cvService.findById(cvId);
//                        if (myCV.isPresent()) {
//                            CV cv = myCV.get();
//
//                            Notification notification = notificationService.findById(notificationId);
//
//                            notificationService.arrangeInterview(notification, loggedInUser, cv.getUser(), proposedDateTime);
//
//                            return "job_offer_created_successfully";
//                        }
//                    }
//                }
//            }
//            return "home";
//        }
@PostMapping("/arrangeInterview")
public String arrangeInterview(
        @RequestParam("jobOfferId") Integer jobOfferId,
        @RequestParam("cvId") Integer cvId,
        @RequestParam("notificationId") Integer notificationId,
        @RequestParam("proposedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime proposedDateTime,
        Authentication authentication
) {
    if (authentication != null && authentication.isAuthenticated()) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User loggedInUser = userService.findByUserName(userDetails.getUsername());

        if (loggedInUser != null) {
            Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);

            if (optionalJobOffer.isPresent()) {
                JobOffer jobOffer = optionalJobOffer.get();
                Optional<CV> myCV = cvService.findById(cvId);

                if (myCV.isPresent()) {
                    CV cv = myCV.get();
                    Notification notification = notificationService.findById(notificationId);

                    notificationService.arrangeInterview(notification, loggedInUser, cv.getUser(), proposedDateTime);

                    return "job_offer_created_successfully";
                }
            }
        }
    }

    return "redirect:/home"; // Przekieruj w przypadku problemu
}



//    @PostMapping("/decline")
//        public String declineNotification (
//                @RequestParam("notificationId") Integer notificationId,
//                @RequestParam("cvId") Integer cvId,
//                HttpSession httpSession
//    ){
//            String username = (String) httpSession.getAttribute("username");
//            if (username != null) {
//                User loggedInUser = userService.findByUserName(username);
//                if (loggedInUser != null) {
//                    Optional<JobOffer> optionalJobOffer = jobOfferService.findByUser(loggedInUser);
//
//                    if (optionalJobOffer.isPresent()) {
//                        JobOffer jobOffer = optionalJobOffer.get();
//                        Optional<CV> myCV = cvService.findById(cvId);
//                        if (myCV.isPresent()) {
//                            CV cv = myCV.get();
//                            Notification notification = notificationService.findById(notificationId);
//
//                            notificationService.declineCandidate(notification, loggedInUser, cv.getUser());
//                            return "job_offer_created_successfully";
//                        }
//                    }
//                }
//            }
//            return "home";
//        }
//        @PostMapping("/hired")
//        public String hiredCandidate (
//                @RequestParam("notificationId") Integer notificationId,
//                @RequestParam("cvId") Integer cvId,
//                HttpSession httpSession
//    ){
//            String username = (String) httpSession.getAttribute("username");
//            if (username != null) {
//                User loggedInUser = userService.findByUserName(username);
//                if (loggedInUser != null) {
//                    Optional<JobOffer> optionalJobOffer = jobOfferService.findByUser(loggedInUser);
//
//                    if (optionalJobOffer.isPresent()) {
//                        JobOffer jobOffer = optionalJobOffer.get();
//                        Optional<CV> myCV = cvService.findById(cvId);
//                        if (myCV.isPresent()) {
//                            CV cv = myCV.get();
//                            Notification notification = notificationService.findById(notificationId);
//
//                            notificationService.hiredCandidate(notification, loggedInUser, cv.getUser());
//                            return "job_offer_created_successfully";
//                        }
//                    }
//                }
//            }
//            return "home";
//        }

    @PostMapping("/decline")
    public String declineNotification(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("cvId") Integer cvId,
            Authentication authentication
    ) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User loggedInUser = userService.findByUserName(userDetails.getUsername());

            if (loggedInUser != null) {
                Optional<JobOffer> optionalJobOffer = jobOfferService.findByUser(loggedInUser);

                if (optionalJobOffer.isPresent()) {
                    JobOffer jobOffer = optionalJobOffer.get();
                    Optional<CV> myCV = cvService.findById(cvId);

                    if (myCV.isPresent()) {
                        CV cv = myCV.get();
                        Notification notification = notificationService.findById(notificationId);

                        notificationService.declineCandidate(notification, loggedInUser, cv.getUser());
                        return "job_offer_created_successfully";
                    }
                }
            }
        }

        return "redirect:/home"; // Przekieruj w przypadku problemu
    }

    @PostMapping("/hired")
    public String hiredCandidate(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("cvId") Integer cvId,
            Authentication authentication
    ) {
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User loggedInUser = userService.findByUserName(userDetails.getUsername());

            if (loggedInUser != null) {
                Optional<JobOffer> optionalJobOffer = jobOfferService.findByUser(loggedInUser);

                if (optionalJobOffer.isPresent()) {
                    JobOffer jobOffer = optionalJobOffer.get();
                    Optional<CV> myCV = cvService.findById(cvId);

                    if (myCV.isPresent()) {
                        CV cv = myCV.get();
                        Notification notification = notificationService.findById(notificationId);

                        notificationService.hiredCandidate(notification, loggedInUser, cv.getUser());
                        return "job_offer_created_successfully";
                    }
                }
            }
        }

        return "redirect:/home"; // Przekieruj w przypadku problemu
    }


}
