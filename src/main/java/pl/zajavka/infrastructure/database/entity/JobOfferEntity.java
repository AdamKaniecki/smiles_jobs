package pl.zajavka.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zajavka.infrastructure.security.UserEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "job_offer_table")
public class JobOfferEntity {

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

    @Column(name = "benefits")
    private String benefits;

    @Column(name = "salary_min")
    private BigDecimal salaryMin;
//
//    @Column(name = "salary_max")
//    private BigDecimal salaryMax;

    @Column(name = "date_time_job_offer")
    private OffsetDateTime jobOfferDateTime;



    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

//    @ManyToMany
//    @JoinTable(
//            name = "job_offer_notification_table",
//            joinColumns = @JoinColumn(name = "job_offer_id"),
//            inverseJoinColumns = @JoinColumn(name = "notification_id"))
//    private Set<NotificationEntity> notifications;
}
