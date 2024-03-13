package pl.zajavka.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCvDTO {

    private Integer cvId;
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
