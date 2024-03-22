package pl.zajavka.infrastructure.domain;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import pl.zajavka.infrastructure.database.entity.ProgrammingLanguage;

import java.math.BigDecimal;
import java.util.Set;

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
    private BigDecimal minSalary;
    private BigDecimal maxSalary;
    private String maritalStatus;
    private String phoneNumber;
    private String contactEmail;
    private String workExperience;
    private String education;
    private String skills;
    private Set<ProgrammingLanguage> programmingLanguages;
//    private Set<IT_Specializations> it_specializations;
    private String tools;
    private Integer yearsOfExperience;
    private String language;
    private String languageLevel;
    private String hobby;
    private User user;
    private Address address;
//    private byte [] photo;






}
