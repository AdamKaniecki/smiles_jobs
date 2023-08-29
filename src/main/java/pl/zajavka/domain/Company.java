package pl.zajavka.domain;

import jakarta.persistence.Column;
import lombok.*;
import pl.zajavka.infrastructure.database.entity.AddressEntity;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"companyId", "companyName", "companyDescription", "email", "recruitmentCriteria", "requestEmployment"})
public class Company {

    Long companyId;

    String companyName;

    String companyDescription;

    String email;

    String recruitmentCriteria;

    String requestEmployment;
    AddressEntity address;
}
