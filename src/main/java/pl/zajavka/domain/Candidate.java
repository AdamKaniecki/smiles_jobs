package pl.zajavka.domain;

import lombok.*;
import org.springframework.stereotype.Component;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.entity.CandidateAdvertisementEntity;

import java.util.Set;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"candidateId", "candidateName", "surname", "email","phoneNumber","availabilityStatus"})
public class Candidate {

    Integer candidateId;
    String candidateName;
    String surname;
    String email;
    String phoneNumber;
    Boolean availabilityStatus;
    Address address;
    Set<CandidateAdvertisement> candidateAdvertisements;
}
