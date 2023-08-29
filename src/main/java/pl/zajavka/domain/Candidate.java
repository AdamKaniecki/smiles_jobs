package pl.zajavka.domain;

import lombok.*;
import pl.zajavka.infrastructure.database.entity.AddressEntity;

@With
@Value
@Builder
@EqualsAndHashCode(of = "pesel")
@ToString(of = {"candidateId","pesel", "name", "surname", "email","phoneNumber","availabilityStatus"})
public class Candidate {

    Integer candidateId;
    String pesel;
    String name;
    String surname;
    String email;
    String phoneNumber;
    Boolean availabilityStatus;
//    String CV;
    AddressEntity address;
}
