package pl.zajavka.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
public class Address {

    private  Integer id;
    private String country;
    private String city;
    private String streetAndNumber;
}
