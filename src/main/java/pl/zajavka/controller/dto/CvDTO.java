package pl.zajavka.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.User;

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
    @Email
    private String contactEmail;
    @Size(min = 7,max = 15)
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String phoneNumber;
    private String education;
    private String workExperience;
    private String socialMediaProfil;
    private String projects;
    private String aboutMe;
    private String certificatesOfCourses;
    private String programmingLanguage;
    private String skillsAndTools;
    private String language;
    private String languageLevel;
    private String hobby;
    private String followPosition;
    private Address address;
    @JsonIgnore
    private Boolean visible;
    @JsonIgnore
    private User user;


}
