
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
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.PaginationService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.User;

import java.util.List;

;

@Slf4j
@AllArgsConstructor
@Controller
public class CompanyPortalController {

    public static final String COMPANY_PORTAL = "/company_portal";
    private UserService userService;
    private CvService cvService;
    private CvMapperDTO cvMapperDTO;;
    private NotificationService notificationService;
    private PaginationService paginationService;


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


        List<NotificationDTO> notificationDTOs = notificationService.findLatestByUser(loggedInUser);
        model.addAttribute("notificationDTOs",notificationDTOs);

        return "company_portal";

    }

    @GetMapping("/companyNotifications")
    public String getAllNotifications(Authentication authentication, Model model,
                                      @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        String username = authentication.getName();
        User loggedInUser = userService.findByUserName(username);
        List<NotificationDTO> notificationDTOs = notificationService.findByUser(loggedInUser);

        Page<NotificationDTO> notificationDTOsPage = paginationService.createNotificationPage(notificationDTOs, pageable);
        model.addAttribute("notificationDTOs", notificationDTOsPage.getContent());
        model.addAttribute("currentNotificationPage", notificationDTOsPage.getNumber());
        model.addAttribute("totalNotificationPages", notificationDTOsPage.getTotalPages());
        model.addAttribute("totalNotificationItems", notificationDTOsPage.getTotalElements());

        return "company_notifications";
    }



    @GetMapping("/search")
    public String searchCV(
            @RequestParam("keyword") String keyword,
            @RequestParam("category") String category,
            Model model) {

        List<CvDTO> searchResultsDTO = cvService.searchCvByKeywordAndCategory(keyword, category);
        model.addAttribute("searchResultsDTO", searchResultsDTO);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);
        return "search_results";
    }






}
