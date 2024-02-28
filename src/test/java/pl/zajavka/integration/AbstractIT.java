package pl.zajavka.integration;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.zajavka.SpringBootSmilesJobs;

@ActiveProfiles("test")
@Import(PersistenceContainerTestConfiguration.class)
@SpringBootTest(
        classes = {SpringBootSmilesJobs.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public abstract class AbstractIT {
}
