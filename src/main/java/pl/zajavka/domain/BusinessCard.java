package pl.zajavka.domain;

import lombok.*;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.security.UserEntity;

@Data
@With
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class BusinessCard {
    private Integer id;
    private String office;
    private String scopeOperations;
    private String recruitmentEmail;
    private String phoneNumber;
    private String companyDescription;
    private String technologiesAndTools;
    private String certificatesAndAwards;
    private User user;
    private Address address;
}
