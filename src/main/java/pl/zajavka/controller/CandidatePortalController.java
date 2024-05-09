

package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.controller.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.infrastructure.business.*;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Controller
@Slf4j
public class CandidatePortalController {
    public static final String CANDIDATE_PORTAL = "/candidate_portal";

    private UserService userService;
    private JobOfferService jobOfferService;
    private JobOfferMapperDTO jobOfferMapperDTO;
    private NotificationService notificationService;
    private PaginationService paginationService;


    @SneakyThrows
    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage(
            Authentication authentication,
            Model model,
            @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable

    ) {
        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        Page<JobOfferDTO> jobOfferDTOsPage = paginationService.findAllJobOffersForPage(pageable)
                .map(jobOfferMapperDTO::map);

        model.addAttribute("jobOfferDTOs", jobOfferDTOsPage.getContent());
        model.addAttribute("currentJobOfferPage", jobOfferDTOsPage.getNumber()); // Page numbers start from 1
        model.addAttribute("totalJobOfferPages", jobOfferDTOsPage.getTotalPages());
        model.addAttribute("totalJobOfferItems", jobOfferDTOsPage.getTotalElements());

        List<NotificationDTO> notificationDTOs = notificationService.findLatestByUser(loggedInUser);
        model.addAttribute("notificationDTOs", notificationDTOs);


        return "candidate_portal";
    }

//

//


    @GetMapping("/candidateNotifications")
    public String getAllNotifications(Authentication authentication, Model model,
                                      @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        List<NotificationDTO> notificationDTOs = notificationService.findByUser(loggedInUser);

        Page<NotificationDTO> notificationDTOsPage = paginationService.createNotificationPage(notificationDTOs, pageable);
        model.addAttribute("notificationDTOs", notificationDTOsPage.getContent());
        model.addAttribute("currentNotificationPage", notificationDTOsPage.getNumber());
        model.addAttribute("totalNotificationPages", notificationDTOsPage.getTotalPages());
        model.addAttribute("totalNotificationItems", notificationDTOsPage.getTotalElements());

        return "candidate_notifications";
    }


    @GetMapping("/searchJobOffers")
    public String searchJobOffers(
            @RequestParam("keyword") String keyword,
            @RequestParam("category") String category,
            Model model) {

        List<JobOffer> searchResults;
        if ("salaryMin".equals(category)) {
            try {
                BigDecimal salaryMinValue = BigDecimal.valueOf(Double.parseDouble(keyword)); // Parsowanie wartości salaryMin
                searchResults = jobOfferService.searchJobOffersBySalary(category, salaryMinValue);
            } catch (NumberFormatException e) {

                return "error"; // Zwróć widok strony błędu
            }
        } else {
            searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category);
        }

        List<JobOfferDTO> searchResultsDTO = searchResults.stream()
                .map(jobOfferMapperDTO::map)
                .collect(Collectors.toList());

        model.addAttribute("searchResultsDTO", searchResultsDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);

        return "search_job_offers_results";
    }





}
















