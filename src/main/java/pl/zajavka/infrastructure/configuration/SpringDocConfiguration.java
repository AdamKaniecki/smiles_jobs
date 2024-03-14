package pl.zajavka.infrastructure.configuration;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.zajavka.SpringBootSmilesJobs;

@Configuration
public class SpringDocConfiguration {

//    trzeba zarejestrować 3 Beany:

@Bean
    public GroupedOpenApi groupedOpenApi(){ // na tej podstawie zostanie wystawiona dokumentacja

    return GroupedOpenApi.builder()
            .group("default") //nazwa dokumentacji
            .pathsToMatch("/**") //wystawiona dokumentacja dla takich ścieżek
            .packagesToScan(SpringBootSmilesJobs.class.getPackageName()) //scanning paczek
            .build();
}
@Bean
    public OpenAPI springDocOpenApi() {
    return new OpenAPI()
            .components(new Components())
            .info(new Info()
                    .title("Smiles Jobs applications")
                    .contact(contact())
                    .version("1.0"));
}
//

           private Contact contact(){
        return new Contact()
                .name("Smiles Jobs")
                .url("https://zajavka.pl")// tu zmiana póżniej
                .email("adam2113@poczta.onet.pl");
    }
}


