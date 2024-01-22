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

import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private EntityManager entityManager;

//    @SneakyThrows
//    @GetMapping(CANDIDATE_PORTAL)
//    public String getCandidatePortalPage(Model model, Authentication authentication, HttpSession session) {
//
//        User user = (User) session.getAttribute("username");
//
//
//        User loggedInUser = userService.getLoggedInUser((authentication));
//        UserDTO userDTO = userMapperDTO.map(loggedInUser);
//        model.addAttribute("userDTO", userDTO);
//
//        List<JobOfferDTO> jobOfferDTOs = jobOfferService.findAll().stream()
//                .map(jobOfferMapperDTO::map)
//                .collect(Collectors.toList());
//        model.addAttribute("jobOffersDTOs", jobOfferDTOs);
//
//        List<NotificationDTO> notifications = notificationService.findByUser(loggedInUser).stream()
//                .map(notificationMapperDTO::map)
//                .collect(Collectors.toList());
//        model.addAttribute("notifications", notifications);
//
//        return "candidate_portal";
//    }


//    @GetMapping(CANDIDATE_PORTAL)
//    public String getCandidatePortalPage(HttpSession session, Model model) {
//        System.out.println("czy ty tu wchodzisz 3?");
//        User user = (User) session.getAttribute("user");
//        if (user != null) {
//            // Użytkownik jest zalogowany
//            model.addAttribute("user", user);
//            UserDTO userDTO = userMapperDTO.map(user);
//            model.addAttribute("userDTO", userDTO);
//            model.addAttribute("cvDTO", new CvDTO());  // Dodaj obiekt cvDTO do modelu, może być pusty, jeśli nie ma jeszcze CV
//
//            List<JobOffer> jobOffers = jobOfferService.findAll();
//            List<JobOfferDTO> jobOfferDTOs = jobOffers.stream()
//                    .map(jobOfferMapperDTO::map)
//                    .toList();
//            model.addAttribute("jobOffersDTOs", jobOfferDTOs);
//
//            List<Notification> userNotifications = notificationService.findByUser(user);
//            List<NotificationDTO> notificationDTOs = userNotifications.stream()
//                    .map(notificationMapperDTO::map)
//                    .collect(Collectors.toList());
//            model.addAttribute("notifications", notificationDTOs);
//
//            return "candidate_portal";
//        } else {
//            // Użytkownik nie jest zalogowany, przekieruj na stronę logowania
//            return "home";
//        }
//    }
//@GetMapping(CANDIDATE_PORTAL)
//public String getCandidatePortalPage(HttpSession session, Model model) {
//    System.out.println("czy ty tu wchodzisz 3?");
//    User user = (User) session.getAttribute("username"); // Używamy tego samego klucza "loggedInUser"
//    if (user != null) {
//        // Użytkownik jest zalogowany
//        model.addAttribute("username", user);
//        UserDTO userDTO = userMapperDTO.map(user);
//        model.addAttribute("userDTO", userDTO);
//        model.addAttribute("cvDTO", new CvDTO());  // Dodaj obiekt cvDTO do modelu, może być pusty, jeśli nie ma jeszcze CV
//
//        List<JobOffer> jobOffers = jobOfferService.findAll();
//        List<JobOfferDTO> jobOfferDTOs = jobOffers.stream()
//                .map(jobOfferMapperDTO::map)
//                .toList();
//        model.addAttribute("jobOffersDTOs", jobOfferDTOs);
//
//        List<Notification> userNotifications = notificationService.findByUser(user);
//        List<NotificationDTO> notificationDTOs = userNotifications.stream()
//                .map(notificationMapperDTO::map)
//                .collect(Collectors.toList());
//        model.addAttribute("notifications", notificationDTOs);
//
//        return "candidate_portal";
//    } else {
//        // Użytkownik nie jest zalogowany, przekieruj na stronę logowania
//        return "home";
//    }
//}



    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage(Model model, Authentication authentication) {
        System.out.println("czy ty tu wchodzisz 3?");
        if (authentication != null && authentication.isAuthenticated()) {
            // Użytkownik jest zalogowany
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findByUserName(userDetails.getUsername());
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
            return "home";
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
//    public String sendCV(@RequestParam("jobOfferId") Integer jobOfferId) {
//
//        JobOffer jobOffer = jobOfferService.findById(jobOfferId);
//
//        if (jobOffer != null) {
//
//
//            Optional<CV> myCV = cvService.findByUser(loggedInUser);
//
//            if (myCV.isPresent()) {
//                CV cv = myCV.get();
//
//                try {
//                    // Utwórz obiekt Notification
//                    Notification notification = notificationService.createNotification(jobOffer, cv, loggedInUser, jobOffer.getUser());
//
//                    // Zapisz encje w odpowiedniej kolejności
////                            userService.save(loggedInUser);
////                            notificationService.save(notification);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    // Obsłuż błąd zapisu
//                    return "error_while_saving";
//                }
//
//            } else {
//                return "cv_not_found";
//            }
//
//        }
//        return "cv_created_successfully";
//    }
//}


//    @PostMapping("/sendCV")
//    public String sendCV(@RequestParam("jobOfferId") Integer jobOfferId, Model model, Authentication authentication) {
//        Authentication userAuth = SecurityContextHolder.getContext().getAuthentication();
//        String username = userAuth.getName();
//
//                    JobOffer jobOffer = jobOfferService.findById(jobOfferId);
//
//                    System.out.println("czy ty tu wchodzisz?3");
//
//                    System.out.println("czy ty tu wchodzisz?4");
//                    Optional<CV> myCV = cvService.);
//                    if (myCV.isPresent()) {
//                        System.out.println("czy ty tu wchodzisz?5");
//                        CV cv = myCV.get();
////                        cv = entityManager.merge(cv);
//                        // Utwórz obiekt Notification
//                        System.out.println("czy ty tu wchodzisz?6");
//                        User adresat = jobOffer.getUser();
//                        Notification notification = notificationService.createNotification(jobOffer, cv,loggedInUser, adresat);
//
//                        // Zapisz zmiany w użytkowniku
////                        userService.save(loggedInUser);
////                        userService.save(adresat);
//
//                        return "candidate_created_successfully";
//                    } else {
//                        // Obsłuż sytuację, gdy użytkownik nie ma przypisanego CV
//                        return "cv_not_found"; // Przekieruj na stronę główną lub obsłuż inaczej
//                    }
//                }
//
//       // Przekieruj w przypadku problemu
//    }

//    @PostMapping("/sendCV")
//    public String sendCV(@RequestParam("jobOfferId") Integer jobOfferId, Model model, Authentication authentication) {
//        if (authentication != null) {
//            User sender = userService.getLoggedInUser(authentication);
//            JobOffer jobOffer = jobOfferService.findById(jobOfferId);
//            CV cvToSend = cvService.getCvByUser(sender);
//            User recipientUser = userService.getUserByJobOffer(jobOffer);
//
//            Notification notification = notificationService.createNotification(jobOffer, cvToSend, sender, recipientUser);
//
//        }
//        return "cv_created_successfully";
//    }


//    @PostMapping("/changeMeetingDate")
//    public String changeMeetingDate(
//            @RequestParam("notificationId") Integer notificationId,
//            @RequestParam("jobOfferId") Integer jobOfferId,
//            Authentication authentication
//    ) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            User loggedInUser = userService.findByUserName(userDetails.getUsername());
//
//            if (loggedInUser != null) {
//                Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);
//
//                if (optionalJobOffer.isPresent()) {
//                    JobOffer jobOffer = optionalJobOffer.get();
//                    Notification notification = notificationService.findById(notificationId);
//                    User adresat = jobOffer.getUser();
//
//                    // Tutaj możesz dodać kod do zmiany pola companyMessage
//                    notificationService.changeMeetingDate(notification, loggedInUser, adresat);
//
//                    return "cv_created_successfully";
//                }
//            }
//        }
//
//        return "redirect:/home"; // Przekieruj w przypadku problemu
//    }

//    @PostMapping("/acceptMeetingDate")
//    public String acceptNotification(
//            @RequestParam("notificationId") Integer notificationId,
//            @RequestParam("jobOfferId") Integer jobOfferId,
//            Authentication authentication
//    ) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            User loggedInUser = userService.findByUserName(userDetails.getUsername());
//
//            if (loggedInUser != null) {
////                Optional<JobOffer> optionalJobOffer = jobOfferService.findById(jobOfferId);
//
//                if (optionalJobOffer.isPresent()) {
//                    JobOffer jobOffer = optionalJobOffer.get();
//                    Notification notification = notificationService.findById(notificationId);
//                    User adresat = jobOffer.getUser();
//
//                    notificationService.acceptMeetingDateTime(notification, loggedInUser, adresat);
//
//                    return "cv_created_successfully";
//                }
//            }
//        }
//
//        return "redirect:/home"; // Przekieruj w przypadku problemu
//    }


    @PostMapping("/sendCV")
    public String sendCv(@RequestParam Integer jobOfferId, Model model) {
        // Pobierz informacje o użytkowniku z HttpSession
        //        User loggedInUser = (User) httpSession.getAttribute("username");
        String username = (String) httpSession.getAttribute("username");
        if (username != null) {
            // Pobierz zalogowanego użytkownika
            User loggedInUser = userService.findByUserName(username);

//             Sprawdź, czy użytkownik ma przypisane CV
            Optional<CV> userCV = cvService.findByUser(loggedInUser);

            // Sprawdź, czy oferta pracy istnieje
            JobOffer jobOffer = jobOfferService.findById(jobOfferId);

            if (userCV.isPresent() && jobOffer != null) {
                CV cv = userCV.get();
                User adresat = jobOffer.getUser();


                notificationService.createNotification(jobOffer, cv, loggedInUser, adresat);

            }
        }
        return "candidate_portal";
    }






















}





