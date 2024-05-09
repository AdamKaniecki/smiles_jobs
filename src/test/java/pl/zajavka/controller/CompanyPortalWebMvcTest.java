package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.NotificationService;
import pl.zajavka.infrastructure.business.PaginationService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = CompanyPortalController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CompanyPortalWebMvcTest {


    @MockBean
    private UserService userService;
    @MockBean
    private CvService cvService;
    @MockBean
    private CvMapperDTO cvMapperDTO;;
    @MockBean
    private NotificationService notificationService;
    @MockBean
    private PaginationService paginationService;

    MockMvc mockMvc;

    @Test
    public void testGetCompanyPortalPage() throws Exception {

        // given
        User user = UserFixtures.someUser2();
        // Mock authentication
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("adam122222");
        // Mock service behavior
        when(userService.findByUserName("adam122222")).thenReturn(user);


        List<CV> cvList = new ArrayList<>();
        cvList.add(CvFixtures.someCv1());
        Page<CV> cvDTOPage = new PageImpl<>(cvList);
        List<NotificationDTO> notificationDTOs = new ArrayList<>();
        notificationDTOs.add(new NotificationDTO());
//

//        // Mock paginationService behavior
        when(paginationService.findAll(any(Pageable.class))).thenReturn(cvDTOPage);
        // Mock jobOfferMapperDTO behavior
        when(cvMapperDTO.map(any(CV.class))).thenReturn(CvFixtures.someCvDTO());
//        // Mock notificationService behavior
        when(notificationService.findLatestByUser(user)).thenReturn(notificationDTOs);
//
        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/company_portal").principal(authentication))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("company_portal"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("cvDTOs"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("currentPage"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("totalPages"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("totalItems"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("notificationDTOs"));

        // Verify that userService.findByUserName() was called with correct username
        Mockito.verify(userService).findByUserName("adam122222");
    }
}
