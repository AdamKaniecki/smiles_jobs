
package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
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
import pl.zajavka.domain.Notification;
import pl.zajavka.domain.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Controller
public class CompanyPortalController {

    public static final String COMPANY_PORTAL = "/company_portal";
    private UserService userService;
    private UserMapperDTO userMapperDTO;
    private CvService cvService;
    private CvMapperDTO cvMapperDTO;;
    private NotificationService notificationService;
    private NotificationMapperDTO notificationMapperDTO;
    private PaginationService paginationService;

    @SneakyThrows
    @GetMapping(COMPANY_PORTAL)
    public String getCompanyPortalPage(Authentication authentication, Model model, HttpSession httpSession,
     @PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable)
    {

        User loggedInUser = userService.getLoggedInUser(authentication);
        httpSession.setAttribute("user", loggedInUser);
        UserDTO userDTO = userMapperDTO.map(loggedInUser);
        model.addAttribute("userDTO", userDTO);

        Page<CvDTO> cvDTOPage = cvService.findAll(pageable)
                .map(cvMapperDTO::map);

        model.addAttribute("cvDTOs", cvDTOPage.getContent());
        model.addAttribute("currentPage", cvDTOPage.getNumber()) ; // Page numbers start from 1
        model.addAttribute("totalPages", cvDTOPage.getTotalPages());
        model.addAttribute("totalItems", cvDTOPage.getTotalElements());

        List<NotificationDTO> notificationDTOs = notificationService.findByUser(loggedInUser).stream()
                .map(notificationMapperDTO::map).toList();
        Page<NotificationDTO> notificationDTOsPage = paginationService.createNotificationPage(notificationDTOs, pageable);

        model.addAttribute("notificationDTOs", notificationDTOsPage.getContent());
        model.addAttribute("currentNotificationPage", notificationDTOsPage.getNumber());
        model.addAttribute("totalNotificationPages", notificationDTOsPage.getTotalPages());
        model.addAttribute("totalNotificationItems", notificationDTOsPage.getTotalElements());

        return "company_portal";

    }

    @GetMapping("/search")
    public String searchAdvertisements(
            @RequestParam("keyword") String keyword,
            @RequestParam("category") String category,
            Model model) {
        List<CV> searchResults = cvService.searchCvByKeywordAndCategory(keyword, category);
        List<CvDTO> searchResultsDTO = searchResults.stream()
                .map(cvMapperDTO::map)
                .collect(Collectors.toList());

        model.addAttribute("searchResultsDTO", searchResultsDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "search_results";
    }

    @GetMapping("/cv/{cvId}")
    public String showCvDetails(@PathVariable Integer cvId, Model model) {
        Optional<CV> cv = cvService.findById(cvId);
        if (cv.isPresent()) {
            model.addAttribute("cvDTO", cvMapperDTO.map(cv.get()));
            return "show_cv";
        } else {
            return "cv_not_found";
        }
    }

    @PostMapping("/arrangeInterview")
    public String arrangeInterview(
            @RequestParam("cvId") Integer cvId,
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("proposedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime proposedDateTime,
            Authentication authentication
    ) {
        User loggedInUser = userService.getLoggedInUser((authentication));
        User cvUser = userService.getUserByCv(cvId);
        Notification notification = notificationService.findById(notificationId);
        notificationService.arrangeInterview(notification, loggedInUser, cvUser, proposedDateTime);

        return "job_offer_created_successfully";

    }


    @PostMapping("/decline")
    public String declineNotification(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("cvId") Integer cvId,
            Authentication authentication
    ) {

        User loggedInUser = userService.getLoggedInUser(authentication);
        User cvUser = userService.getUserByCv(cvId);
        Notification notification = notificationService.findById(notificationId);
        notificationService.declineCandidate(notification, loggedInUser, cvUser);

        return "job_offer_created_successfully";

    }

    @PostMapping("/hired")
    public String hiredCandidate(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("cvId") Integer cvId,
            Authentication authentication
    ) {

        User loggedInUser = userService.getLoggedInUser(authentication);
        User cvUser = userService.getUserByCv(cvId);
        Notification notification = notificationService.findById(notificationId);
        notificationService.hiredCandidate(notification, loggedInUser, cvUser);

        return "job_offer_created_successfully";
    }


//    private User getLoggedInUser(Authentication authentication) throws AccessDeniedException {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new AccessDeniedException("Użytkownik nie jest uwierzytelniony");
//        }
//
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        User loggedInUser = userService.findByUserName(userDetails.getUsername());
//
//        if (loggedInUser == null) {
//            throw new AccessDeniedException("Użytkownik nie znaleziony");
//        }
//
//        return loggedInUser;
//    }


//    @SneakyThrows
//    private User getUserByCv(Integer cvId) {
//        Optional<CV> myCV = cvService.findById(cvId);
//        if (myCV.isEmpty()) {
//            throw new AccessDeniedException("Użytkownik nie posiada oferty pracy");
//        }
//
//        return myCV.get().getUser();
//    }

}
