package pl.zajavka.infrastructure.database.entity;
//
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zajavka.infrastructure.security.UserEntity;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "business_card_table")
public class BusinessCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_card_id")
    private Integer id;

    @Column(name = "office")
    private String office;

    @Column(name = "scope_operations")
    private String scopeOperations;

    @Column(name = "recruitment_email")
    private String recruitmentEmail;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "company_description")
    private String companyDescription;

    @Column(name = "technologies_and_tools")
    private String technologiesAndTools;

    @Column(name = "certificates_and_awards")
    private String certificatesAndAwards;

    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;
}
