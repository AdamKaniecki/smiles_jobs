package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateAdvertisementDTO {

    Integer candidateAdvertisementId;
    String number;
    String workExperience;
    String knowledgeTechnology;
    String programmingLanguage;
    OffsetDateTime dateOfAdvertisement;
}
