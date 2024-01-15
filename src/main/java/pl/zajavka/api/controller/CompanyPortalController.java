
package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.api.dto.CvDTO;
import pl.zajavka.api.dto.NotificationDTO;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.mapper.*;
import pl.zajavka.business.*;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.Notification;
import pl.zajavka.domain.User;

import java.nio.file.AccessDeniedException;
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

    @SneakyThrows
    @GetMapping(COMPANY_PORTAL)
    public String getCompanyPortalPage(Authentication authentication, Model model) {

        User loggedInUser = getLoggedInUser((authentication));
        UserDTO userDTO = userMapperDTO.map(loggedInUser);
        model.addAttribute("userDTO", userDTO);

        List<CvDTO> cvList = cvService.findAll().stream()
                        .map(cvMapperDTO::map)
                                .collect(Collectors.toList());
        model.addAttribute("cvList", cvList);

        List<NotificationDTO> notifications = notificationService.findByUser(loggedInUser).stream()
                .map(notificationMapperDTO::map)
                .collect(Collectors.toList());
        model.addAttribute("notifications", notifications);

        return "company_portal";

    }


    @GetMapping("/search")
    public String searchAdvertisements(
            @RequestParam("keyword") String keyword,
            @RequestParam("category") String category,
            Model model) {
        List<CV> searchResults = cvService.searchCvByKeywordAndCategory(keyword, category);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "search_results";
    }

    @GetMapping("/search_results")
    public String showSearchResults(@RequestParam String keyword, String category, Model model) {
        List<CV> searchResults = cvService.searchCvByKeywordAndCategory(category, keyword);
        model.addAttribute("searchResults", searchResults);
        return "search_results";
    }


    @GetMapping("/cv/{cvId}")
    public String showCvDetails(@PathVariable Integer cvId, Model model) {
        Optional<CV> cv = cvService.findById(cvId);
        if (cv.isPresent()) {
            model.addAttribute("cvDTO", cvMapperDTO.map(cv.get()));
            return "show_cv";  // Użyj istniejącego widoku show_cv
        } else {
            return "cv_not_found";  // Stwórz odpowiedni widok dla przypadku, gdy CV nie zostanie znalezione
        }
    }

    @PostMapping("/arrangeInterview")
    public String arrangeInterview(
            @RequestParam("cvId") Integer cvId,
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("proposedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime proposedDateTime,
            Authentication authentication
    ) {

        try {
            User loggedInUser = getLoggedInUser(authentication);
            User cvUser = getUserByCv(cvId);
            Notification notification = notificationService.findById(notificationId);

            notificationService.arrangeInterview(notification, loggedInUser, cvUser, proposedDateTime);

            return "job_offer_created_successfully";
        } catch (AccessDeniedException e) {
            log.error("Błąd dostępu: {}", e.getMessage());
            return "redirect:/home";
        }
    }


    @PostMapping("/decline")
    public String declineNotification(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("cvId") Integer cvId,
            Authentication authentication
    ) {
        try {
            User loggedInUser = getLoggedInUser(authentication);
            User cvUser = getUserByCv(cvId);
            Notification notification = notificationService.findById(notificationId);

            notificationService.declineCandidate(notification, loggedInUser, cvUser);

            return "job_offer_created_successfully";
        } catch (AccessDeniedException e) {
            log.error("Błąd dostępu: {}", e.getMessage());
            return "redirect:/home";
        }
    }

    @PostMapping("/hired")
    public String hiredCandidate(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("cvId") Integer cvId,
            Authentication authentication
    ) {
        try {
            User loggedInUser = getLoggedInUser(authentication);
            User cvUser = getUserByCv(cvId);
            Notification notification = notificationService.findById(notificationId);

            notificationService.hiredCandidate(notification, loggedInUser, cvUser);

            return "job_offer_created_successfully";
        } catch (AccessDeniedException e) {
            log.error("Błąd dostępu: {}", e.getMessage());
            return "redirect:/home";
        }
    }




    private User getLoggedInUser(Authentication authentication) throws AccessDeniedException {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("Użytkownik nie jest uwierzytelniony");
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User loggedInUser = userService.findByUserName(userDetails.getUsername());

        if (loggedInUser == null) {
            throw new AccessDeniedException("Użytkownik nie znaleziony");
        }

        return loggedInUser;
    }


    @SneakyThrows
    private User getUserByCv(Integer cvId) {
        Optional<CV> myCV = cvService.findById(cvId);
        if (myCV.isEmpty()) {
            throw new AccessDeniedException("Użytkownik nie posiada oferty pracy");
        }

        return myCV.get().getUser();
    }

}
