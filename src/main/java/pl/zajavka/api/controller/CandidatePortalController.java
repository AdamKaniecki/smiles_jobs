

package pl.zajavka.api.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.zajavka.api.dto.JobOfferDTO;
import pl.zajavka.api.dto.NotificationDTO;
import pl.zajavka.api.dto.UserDTO;
import pl.zajavka.api.dto.mapper.JobOfferMapperDTO;
import pl.zajavka.api.dto.mapper.NotificationMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.*;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.Notification;
import pl.zajavka.domain.User;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Controller
@Slf4j
public class CandidatePortalController {
    public static final String CANDIDATE_PORTAL = "/candidate_portal";
    private HttpSession httpSession;
    private UserService userService;
    private UserMapperDTO userMapperDTO;
    private JobOfferService jobOfferService;
    private JobOfferMapperDTO jobOfferMapperDTO;
    private NotificationService notificationService;
    private NotificationMapperDTO notificationMapperDTO;
    private CvService cvService;
    private PaginationService paginationService;

    @SneakyThrows
    @GetMapping(CANDIDATE_PORTAL)
    public String getCandidatePortalPage(
            Model model,
            Authentication authentication,
            HttpSession httpSession,
            @PageableDefault(size = 2, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        User loggedInUser = userService.getLoggedInUser(authentication);
        httpSession.setAttribute("user", loggedInUser);

        UserDTO userDTO = userMapperDTO.map(loggedInUser);
        model.addAttribute("userDTO", userDTO);

        Page<JobOfferDTO> jobOfferDTOsPage = jobOfferService.findAllJobOffersForPage(pageable)
                .map(jobOfferMapperDTO::map);

        model.addAttribute("jobOfferDTOs", jobOfferDTOsPage.getContent());
        model.addAttribute("currentJobOfferPage", jobOfferDTOsPage.getNumber()); // Page numbers start from 1
        model.addAttribute("totalJobOfferPages", jobOfferDTOsPage.getTotalPages());
        model.addAttribute("totalJobOfferItems", jobOfferDTOsPage.getTotalElements());



        List<NotificationDTO> notificationDTOs = notificationService.findByUser(loggedInUser).stream()
                .map(notificationMapperDTO::map).toList();

        Page<NotificationDTO> notificationDTOsPage = paginationService.createNotificationPage(notificationDTOs, pageable);
        model.addAttribute("notificationDTOs", notificationDTOsPage.getContent());
        model.addAttribute("currentNotificationPage", notificationDTOsPage.getNumber());
        model.addAttribute("totalNotificationPages", notificationDTOsPage.getTotalPages());
        model.addAttribute("totalNotificationItems", notificationDTOsPage.getTotalElements());
        return "candidate_portal";




    }


    @PostMapping("/searchJobOffers")
    public String searchJobOffers(
            @RequestParam("keyword") String keyword,
            @RequestParam("category") String category,
            Model model) {
        List<JobOffer> searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(keyword, category);
        log.info("co tu w kodzie:::::: jobOffers " + searchResults);
        List<JobOfferDTO> searchResultsDTO = searchResults.stream()
                .map(jobOfferMapperDTO::map)
                .collect(Collectors.toList());


        log.info("co tu w kodzie:::::: searchResultsDTO " + searchResultsDTO);
        model.addAttribute("searchResultsDTO", searchResultsDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "search_job_offers_results";
    }

    @GetMapping("/search_job_offers_results")
    public String showSearchResults(@RequestParam String keyword, String category, Model model ) {
        List<JobOffer> searchResults = jobOfferService.searchJobOffersByKeywordAndCategory(category, keyword);
        List<JobOfferDTO> searchResultsDTO = searchResults.stream()
                .map(jobOfferMapperDTO::map)
                .collect(Collectors.toList());
        model.addAttribute("searchResultsDTO", searchResultsDTO);
        return "search_job_offers_results";
    }


    @PostMapping("/changeMeetingDate")
    public String changeMeetingDate(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("jobOfferId") Integer jobOfferId,
            Authentication authentication
    ) {
        User loggedInUser = userService.getLoggedInUser((authentication));
        httpSession.setAttribute("user", loggedInUser);

        JobOffer jobOffer = jobOfferService.findById(jobOfferId);
        Notification notification = notificationService.findById(notificationId);
        User adresat = jobOffer.getUser();
        notificationService.changeMeetingDate(notification, loggedInUser, adresat);

        return "cv_created_successfully";
    }


    @PostMapping("/acceptMeetingDate")
    public String acceptNotification(
            @RequestParam("notificationId") Integer notificationId,
            @RequestParam("jobOfferId") Integer jobOfferId,
            Authentication authentication
    ) {
        User loggedInUser = userService.getLoggedInUser((authentication));
        httpSession.setAttribute("user", loggedInUser);

        JobOffer jobOffer = jobOfferService.findById(jobOfferId);
        Notification notification = notificationService.findById(notificationId);
        User adresat = jobOffer.getUser();
        notificationService.acceptMeetingDateTime(notification, loggedInUser, adresat);

        return "cv_created_successfully";
    }


    @PostMapping("/sendCV")
    @Transactional
    public String sendCV(@RequestParam("jobOfferId") Integer jobOfferId, Model model, Authentication authentication, HttpSession httpSession) {
        User loggedInUser = userService.getLoggedInUser((authentication));
        httpSession.setAttribute("user", loggedInUser);

        JobOffer jobOffer = jobOfferService.findById(jobOfferId);
        CV cv = cvService.findByUser2(loggedInUser);
        User adresat = jobOffer.getUser();

        if (notificationService.hasUserSentCVToJobOffer(loggedInUser, jobOffer)) {
            return "cv_already_sent";
        }
        if(cv == null){
        }
        Notification notification = notificationService.createNotification(jobOffer, cv, loggedInUser, adresat);
        userService.save(loggedInUser);
        userService.save(adresat);

        return "cv_send_successfully";
    }


}
















