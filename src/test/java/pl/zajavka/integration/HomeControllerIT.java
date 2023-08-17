package pl.zajavka.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pl.zajavka.integration.configuration.AbstractIT;
@RequiredArgsConstructor(onConstructor = @__(@Autowired))

public class HomeControllerIT extends AbstractIT {

    @LocalServerPort
    private int port;

    @Value("${server.servlet.context-path}")
    private String basePath;

    private final TestRestTemplate testRestTemplate;

    @Test
    void homePageWorksCorrectly(){
        String url = String.format("http://localhost:%s%s", port, basePath);
        String page = this.testRestTemplate.getForObject(url, String.class);
        Assertions.assertThat(page).contains("Smiles Jobs!");
    }
}
