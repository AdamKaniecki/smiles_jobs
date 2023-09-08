package pl.zajavka.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zajavka.domain.Candidate;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {


    Long id;
    String candidateName;
    String candidateSurname;
    String candidatePhoneNumber;
    String candidateEmail;


    public Map<String, String> asMap() {

        Map<String, String> result = new HashMap<>();
        ofNullable(candidateName).ifPresent(value -> result.put("candidateName", value));
                ofNullable(candidateSurname).ifPresent(value -> result.put("candidateSurname", value));
                ofNullable(candidatePhoneNumber).ifPresent(value -> result.put("candidatePhone", value));
                ofNullable(candidateEmail).ifPresent(value -> result.put("candidateEmail", value));

        return result;
    }

    private CandidateDTO buildCandidate(Candidate candidate) {
        return CandidateDTO.builder()
                .candidateName(candidate.getName())
                .candidateSurname(candidate.getSurname())
                .candidatePhoneNumber(candidate.getPhoneNumber())
                .candidateEmail(candidate.getEmail())
                .build();

    }
}




