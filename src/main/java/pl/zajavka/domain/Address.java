package pl.zajavka.domain;

import lombok.*;
import pl.zajavka.infrastructure.database.entity.CandidateEntity;
import pl.zajavka.infrastructure.database.entity.CompanyEntity;

@With
@Value
@Builder
@EqualsAndHashCode(of = "addressId")
@ToString(of = {"addressId", "country", "city", "postalCode","StreetAndNumber"})
public class Address {

    Integer addressId;
    String country;
    String city;
    String postalCode;
    String StreetAndNumber;
    Company company;
    Candidate candidate;
}
