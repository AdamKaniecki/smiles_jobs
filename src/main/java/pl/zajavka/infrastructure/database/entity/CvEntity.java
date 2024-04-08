package pl.zajavka.infrastructure.database.entity;
//

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zajavka.infrastructure.security.UserEntity;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CV")
public class CvEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cv_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "date_of_birth")
    private String dateOfBirth;

    @Column(name = "sex")
    private String sex;

    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "social_media_profil")
    private String socialMediaProfil;

    @Column(name = "projects")
    private String projects;

    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "certificates_of_courses")
    private String certificatesOfCourses;

    @Column(name = "programming_language")
    private String programmingLanguage;

    @Column(name = "skills_and_tools")
    private String skillsAndTools;

    @Column(name = "work_experience")
    private String workExperience;

    @Column(name = "education")
    private String education;

    @Column(name = "language")
    private String language;

    @Column(name = "language_level")
    private String languageLevel;

    @Column(name = "hobby")
    private String hobby;

    @Column(name = "follow_position")
    private String followPosition;

    @Column(name = "visible")
    private Boolean visible;

    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;


}
