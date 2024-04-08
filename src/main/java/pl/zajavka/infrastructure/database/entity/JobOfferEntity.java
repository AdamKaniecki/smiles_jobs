package pl.zajavka.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zajavka.infrastructure.security.UserEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_offer_table")
public class JobOfferEntity {
//
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_offer_id")
    private Integer id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "position")
    private String position;

    @Column(name = "responsibilities")
    private String responsibilities;

    @Column(name = "required_technologies")
    private String requiredTechnologies;

    @Column(name = "experience")
    private String experience;

    @Column(name = "job_location")
    private String jobLocation;

    @Column(name = "type_of_contract")
    private String typeOfContract;

    @Column(name = "type_of_work")
    private String typeOfWork;

    @Column(name = "salary_min")
    private BigDecimal salaryMin;

    @Column(name = "salary_max")
    private BigDecimal salaryMax;

    @Column(name = "required_language")
    private String requiredLanguage;

    @Column(name = "required_language_level")
    private String requiredLanguageLevel;

    @Column(name = "benefits")
    private String benefits;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "date_time_job_offer")
    private OffsetDateTime jobOfferDateTime;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "needed_staff")
    private Integer neededStaff; // Ilość potrzebnych pracowników

    @Column(name = "hired_count")
    private Integer hiredCount; // Liczba zatrudnionych pracowników




    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;


}
