package pl.zajavka.domain;

import lombok.*;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.entity.CandidateAdvertisementEntity;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"candidateId", "name", "surname", "email","phoneNumber","availabilityStatus"})
public class Candidate {

    Integer candidateId;
    String name;
    String surname;
    String email;
    String phoneNumber;
    Boolean availabilityStatus;
    Address address;
    Set<CandidateAdvertisement> candidateAdvertisements;
}
