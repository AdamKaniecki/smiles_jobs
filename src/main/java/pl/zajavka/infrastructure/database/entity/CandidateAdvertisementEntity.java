package pl.zajavka.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "candidateAdvertisementId")
@ToString(of = {"candidateAdvertisementId","number","workExperience", "knowledgeTechnology", "dateOfAdvertisement" })
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "candidate_advertisement")
public class CandidateAdvertisementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "candidate_advertisement_id")
    private Integer candidateAdvertisementId;

    @Column(name = "number")
    private String number;

    @Column(name = "work_experience")
    private String workExperience;

    @Column(name = "knowledge_technology")
    private String knowledgeTechnology;

    @Column(name = "date_of_advertisement")
    private OffsetDateTime dateOfAdvertisement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id")
    private CandidateEntity candidate;




}
