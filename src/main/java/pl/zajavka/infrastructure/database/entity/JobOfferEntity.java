package pl.zajavka.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "jobOfferId")
@ToString(of =
        {"jobOfferId","number", "dateOfJobOffers"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "job_offer")
public class JobOfferEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "job_offer_id")
    private Integer jobOfferId;

    @Column(name = "number")
    private String number;

    @Column(name= "company_name")
    private String companyName;

    @Column(name = "position")
    private String position;

    @Column(name = "programming_language")
    private String programmingLanguage;

    @Column(name = "job_responsibilities")
    private String jobResponsibilities;

    @Column(name = "knowledge_technology")
    private String knowledgeTechnology;

    @Column(name = "benefits")
    private String benefits;

    @Column(name = "salary_range")
    private String salaryRange;

    @Column(name = "date_of_job_offers")
    private OffsetDateTime dateOfJobOffers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

}
