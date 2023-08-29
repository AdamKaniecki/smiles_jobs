package pl.zajavka.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@EqualsAndHashCode(of = "companyId")
@ToString(of = {"companyId", "companyName", "companyDescription", "email","recruitmentCriteria","requestEmployment" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "company_id")
    private Long companyId;

    @Column(name= "company_name")
    private String companyName;

    @Column(name= "company_description")
    private String companyDescription;

    @Column(name = "email")
    private String email;

    @Column(name= "recruitment_criteria")
    private String recruitmentCriteria;


    @Column(name= "request_employment")
    private String requestEmployment;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;
}
