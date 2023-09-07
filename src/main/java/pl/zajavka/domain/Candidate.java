package pl.zajavka.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "email")
@ToString(of = {"id", "name", "surname", "email","phoneNumber",

})
public class Candidate {

    Integer id;
    String name;
    String surname;
    String email;
    String phoneNumber;

}
