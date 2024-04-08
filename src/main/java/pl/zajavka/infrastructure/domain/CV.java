package pl.zajavka.infrastructure.domain;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
public class CV {

    private Integer id;
    private String name;
    private String surname;
    private String dateOfBirth;
    private String sex;
    private String maritalStatus;
    private String phoneNumber;
    private String contactEmail;
    private String workExperience;
    private String education;
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
    private Boolean visible;
    private User user;
    private Address address;







}
