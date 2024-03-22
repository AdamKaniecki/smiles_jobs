package pl.zajavka.infrastructure.database.entity;
//

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import pl.zajavka.infrastructure.security.UserEntity;

import java.math.BigDecimal;
import java.util.Set;

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

//    @Enumerated(EnumType.STRING)
    @Column(name = "sex")
    private String sex;

//    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private String maritalStatus;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "contact_email")
    private String contactEmail;



    @Column(name = "work_experience")
    private String workExperience;

    @Column(name = "education")
    private String education;

    @Column(name = "skills")
    private String skills;


    @ElementCollection(targetClass = ProgrammingLanguage.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "cv_programming_languages", joinColumns = @JoinColumn(name = "cv_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "programming_language")
    private Set<ProgrammingLanguage> programmingLanguages;


//    @ElementCollection(targetClass = ProgrammingLanguage.class, fetch = FetchType.EAGER)
//    @CollectionTable(name = "cv_IT_specializations", joinColumns = @JoinColumn(name = "cv_id"))
//    @Enumerated(EnumType.STRING)
//    @Column(name = "IT_specializations")
//    private Set<IT_Specializations> it_specializations;

    @Column(name = "tools")
    private String tools;


    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "language")
    private String language;

//    @Enumerated(EnumType.STRING)
    @Column(name = "language_level")
    private String languageLevel;

    @Column(name = "hobby")
    private String hobby;

//    @Transient
//    private byte[] photo;

    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;


//    @ManyToMany
//    @JoinTable(
//            name = "notifications",
//            joinColumns = @JoinColumn(name = "cv_id"),
//            inverseJoinColumns = @JoinColumn(name = "job_offer_id"))
//    private List<JobOffer> jobOffers;



}
