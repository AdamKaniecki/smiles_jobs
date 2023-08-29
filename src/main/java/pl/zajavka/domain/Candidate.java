package pl.zajavka.domain;

import lombok.*;
import pl.zajavka.infrastructure.database.entity.AddressEntity;

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
//    String CV;
    AddressEntity address;
}
