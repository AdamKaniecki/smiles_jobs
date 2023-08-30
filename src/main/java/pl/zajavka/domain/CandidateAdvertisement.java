package pl.zajavka.domain;

import lombok.*;
import pl.zajavka.infrastructure.database.entity.CandidateEntity;

import java.time.OffsetDateTime;
@With
@Value
@Builder
@EqualsAndHashCode(of = "number")
@ToString(of = {"candidateAdvertisementId", "number", "workExperience", "knowledgeTechnology","dateOfAdvertisement","candidate"})
public class CandidateAdvertisement {

     Integer candidateAdvertisementId;
     String number;
     String workExperience;
     String knowledgeTechnology;
     OffsetDateTime dateOfAdvertisement;
     Candidate candidate;
}
