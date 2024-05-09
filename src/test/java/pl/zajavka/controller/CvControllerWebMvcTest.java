package pl.zajavka.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.controller.dto.mapper.CvMapperDTO;
import pl.zajavka.controller.dto.mapper.UserMapperDTO;
import pl.zajavka.infrastructure.business.AddressService;
import pl.zajavka.infrastructure.business.CvService;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.util.UserFixtures;

import static org.mockito.Mockito.when;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(controllers = CvController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CvControllerWebMvcTest {
    @MockBean
    private CvService cvService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private UserService userService;
    @MockBean
    private CvMapperDTO cvMapperDTO;
    @MockBean
    private UserMapperDTO userMapperDTO;

    MockMvc mockMvc;

    @Test
    public void testCvForm() throws Exception {
        // given
        User user = UserFixtures.someUser1();
        CvDTO cvDTO = new CvDTO();
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("adam12");
        when(userService.findByUserName("adam12")).thenReturn(user);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get("/CvForm").principal(authentication)
                        .flashAttr("cvDTO", cvDTO))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("create_cv"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("userDTO"))
                .andExpect(MockMvcResultMatchers.model().attribute("cvDTO", cvDTO))
                .andExpect(MockMvcResultMatchers.model().attribute("userDTO", user));
    }
}
