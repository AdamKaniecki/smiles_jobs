package pl.zajavka.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(of = {"id","name","surname", "email", "phoneNumber","availabilityStatus" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidate")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "availability_status")
    private Boolean availabilityStatus;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;


}
//    @OneToOne(fetch = FetchType.EAGER, mappedBy = "candidate")
//    private CandidateAdvertisementEntity candidateAdvertisement;