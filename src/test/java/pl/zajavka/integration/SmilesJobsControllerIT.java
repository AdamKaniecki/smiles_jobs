package pl.zajavka.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

//@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SmilesJobsControllerIT extends AbstractIT{

    @LocalServerPort //ta adnotacja ustawia losowy port
    private int port; //dzięki temu możemy wyciągnąć numer portu na jakim jest testowana aplikacja

    private final TestRestTemplate testRestTemplate; //symulacja przeglądarki



//    @Test
//    void applicationWorksCorrectly(){
//    String url = "http://localhost:%s/candidate_portal".formatted(port);
//
//        String page = this.testRestTemplate.getForObject(url, String.class);// ten tesRestTemplate udaje przeglądarkę
////        i probuje wykonać geta do tego url
//
//        Assertions.assertThat(page).contains("<title>Logowanie</title>");// oczekuję że na tej stronie
//        // pod tym adresem będzie taki wpis
//
//    }


}
