package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zajavka.domain.Candidate;

import java.util.List;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {




private String candidateName;
private String candidateSurname;
private String candidateEmail;


        }
