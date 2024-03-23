
package pl.zajavka.controller;

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
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.controller.dto.mapper.*;
;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.database.repository.NotificationRepository;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.PaginationService;
import pl.zajavka.infrastructure.business.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Controller
public class CompanyPortalController {

    public static final String COMPANY_PORTAL = "/company_portal";
    private CvRepository cvRepository;
    private UserService userService;
    private CvService cvService;
    private CvMapperDTO cvMapperDTO;;
    private NotificationService notificationService;
    private NotificationMapperDTO notificationMapperDTO;
    private PaginationService paginationService;
    private NotificationRepository notificationRepository;
    private JobOfferRepository jobOfferRepository;
    private JobOfferMapperDTO jobOfferMapperDTO;

    @SneakyThrows
    @GetMapping(COMPANY_PORTAL)
    public String getCompanyPortalPage(Authentication authentication, Model model,
     @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable)
    {

        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);

        Page<CvDTO> cvDTOPage = paginationService.findAll(pageable)
                .map(cvMapperDTO::map);

        model.addAttribute("cvDTOs", cvDTOPage.getContent());
        model.addAttribute("currentPage", cvDTOPage.getNumber()) ;
        model.addAttribute("totalPages", cvDTOPage.getTotalPages());
        model.addAttribute("totalItems", cvDTOPage.getTotalElements());

        List<NotificationDTO> notificationDTOs = notificationRepository.findByUser(loggedInUser).stream()
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
        List<CV> searchResults = cvRepository.searchCvByKeywordAndCategory(keyword, category);
        List<CvDTO> searchResultsDTO = searchResults.stream()
                .map(cvMapperDTO::map)
                .collect(Collectors.toList());

        model.addAttribute("searchResultsDTO", searchResultsDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "search_results";
    }



    @PostMapping("/arrangeInterview")
    public String arrangeInterview(
            @RequestParam("cvId") Integer cvId,
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("proposedDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime proposedDateTime,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        User cvUser = userService.getUserByCv(cvId);
        Notification notification = notificationRepository.findById(notificationId);

        notificationService.arrangeInterview(notification, loggedInUser, cvUser, proposedDateTime);

        return "job_offer_created_successfully";

    }


    @PostMapping("/decline")
    public String declineNotification(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("cvId") Integer cvId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        User cvUser = userService.getUserByCv(cvId);
        Notification notification = notificationRepository.findById(notificationId);

        notificationService.declineCandidate(notification, loggedInUser, cvUser);

        return "job_offer_created_successfully";

    }

    @PostMapping("/hired")
    public String hiredCandidate(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("cvId") Integer cvId,
            Authentication authentication
    ) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        User cvUser = userService.getUserByCv(cvId);
        Notification notification = notificationRepository.findById(notificationId);
        // Pobierz ofertę pracy z powiadomienia

        JobOffer jobOffer = notification.getJobOffer();
        // Ustaw pole active na false
        jobOffer.setActive(false);
        // Zapisz zmienioną encję JobOffer
        jobOfferRepository.saveJobOffer(jobOffer);
        JobOfferDTO jobOfferDTO = jobOfferMapperDTO.map(jobOffer);
        jobOfferDTO.setActive(false);

        notificationService.hiredCandidate(notification, loggedInUser, cvUser);

        return "job_offer_created_successfully";
    }




}
