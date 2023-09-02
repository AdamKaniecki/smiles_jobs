package pl.zajavka.domain;

import lombok.*;
import pl.zajavka.infrastructure.database.entity.CompanyEntity;

import java.time.OffsetDateTime;
@With
@Value
@Builder
@EqualsAndHashCode(of = "number")
@ToString(of = {"jobOffersId", "number", "dateOfJobOffers"})
public class JobOffer {
    Integer jobOffersId;
    String number;
    String companyName;
    String position;
    String programmingLanguage;
    String jobResponsibilities;
    String knowledgeTechnology;
    String benefits;
    String salaryRange;
    OffsetDateTime dateOfJobOffers;
    CompanyEntity company;
}
