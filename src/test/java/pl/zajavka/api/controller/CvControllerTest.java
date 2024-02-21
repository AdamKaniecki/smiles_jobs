package pl.zajavka.api.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import pl.zajavka.api.dto.mapper.CvMapperDTO;
import pl.zajavka.api.dto.mapper.UserMapperDTO;
import pl.zajavka.business.CvService;
import pl.zajavka.domain.CV;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.util.CvFixtures;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class CvControllerTest {

    @MockBean
    private CvService cvService;

    @MockBean
    private CvMapperDTO cvMapperDTO;

    @MockBean
    private UserMapperDTO userMapperDTO;

    @InjectMocks
    private CvController cvController;

//    ten test narazie nie działa
    @Test
    void testShowMyCV_WhenCvExists() {
        // Given
        Integer cvId = 1;
        CV cv = CvFixtures.createSampleCv(); // Utwórz przykładowy obiekt CV

        when(cvService.findById(cvId)).thenReturn(Optional.of(cv));
        Model model = new ExtendedModelMap();
//        when(cvMapperDTO.map(cv)).thenReturn(cv); // Mockowanie mapowania

        // When
        String viewName = cvController.showMyCV(cvId, model);

        // Then
        assertThat(viewName).isEqualTo("show_my_cv");
        assertThat(model.getAttribute("cvDTO")).isEqualTo(cv);
//        verifyZeroInteractions(userMapperDTO); // Upewnij się, że mapowanie użytkownika nie zostało wywołane
    }
}
