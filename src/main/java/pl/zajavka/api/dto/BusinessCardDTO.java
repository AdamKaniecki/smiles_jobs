package pl.zajavka.api.dto;

import lombok.*;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.User;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCardDTO {

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
