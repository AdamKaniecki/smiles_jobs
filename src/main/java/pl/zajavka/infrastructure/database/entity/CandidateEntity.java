package pl.zajavka.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "candidateId")
@ToString(of = {"candidateId", "pesel","name","surname", "email", "phoneNumber","availabilityStatus" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidate")
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_id")
    private Integer candidateId;

    @Column(name = "pesel")
    private String pesel;

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

//    @Column(name = "CV")
//    private String CV;



    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;
}
