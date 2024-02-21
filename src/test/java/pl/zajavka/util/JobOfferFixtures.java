package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@UtilityClass
public class JobOfferFixtures {
    public static JobOfferEntity someJobOffer1(){

        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
        return JobOfferEntity.builder()
                .companyName("company1")
                .position("junior java developer")
                .responsibilities("utrzymywanie aplikacji")
                .requiredTechnologies("spring")
                .benefits("owocowe czwartki")
                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
                .build();
    }
    public static JobOfferEntity someJobOffer2(){

        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
        return JobOfferEntity.builder()
                .companyName("company2")
                .position("junior java developer")
                .responsibilities("utrzymywanie aplikacji")
                .requiredTechnologies("spring")
                .benefits("owocowe czwartki")
                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
                .build();
    }

    public static JobOfferEntity someJobOffer3(){

        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
        return JobOfferEntity.builder()
                .companyName("company3")
                .position("junior java developer")
                .responsibilities("utrzymywanie aplikacji")
                .requiredTechnologies("spring")
                .benefits("owocowe czwartki")
                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
                .build();
    }


}
