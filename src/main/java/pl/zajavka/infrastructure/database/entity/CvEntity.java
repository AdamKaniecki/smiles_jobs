package pl.zajavka.infrastructure.database.entity;
//

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "education")
    private String education;

    @Column(name = "work_experience")
    private String workExperience;

    @Column(name = "skills")
    private String skills;

    @Column(name = "language")
    private String language;

    @Column(name = "language_level")
    private String languageLevel;

    @Column(name = "hobby")
    private String hobby;

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
