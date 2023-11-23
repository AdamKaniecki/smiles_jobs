package pl.zajavka.api.dto;

import lombok.*;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.User;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CvDTO {
    private Integer id;
    private String name;
    private String surname;
    private String dateOfBirth;
    private String sex;
    private String maritalStatus;
    private String contactEmail;
    private String phoneNumber;
    private String education;
    private String workExperience;
    private String skills;
    private String language;
    private String languageLevel;
    private String hobby;
    private User user;
    private Address address;
}
