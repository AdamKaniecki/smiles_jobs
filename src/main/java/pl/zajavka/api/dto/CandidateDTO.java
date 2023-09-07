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
    private String surname;
    private String phoneNumber;

    private String email;


    public Map<String, String> asMap() {

        Map<String, String> result = new HashMap<>();
        ofNullable(candidateName).ifPresent(value -> result.put("candidateName", value));
                ofNullable(surname).ifPresent(value -> result.put("candidateSurname", value));
                ofNullable(phoneNumber).ifPresent(value -> result.put("candidatePhone", value));
                ofNullable(email).ifPresent(value -> result.put("candidateEmail", value));

        return result;
    }

    private CandidateDTO buildCandidate(Candidate candidate) {
        return CandidateDTO.builder()
                .candidateName(candidate.getName())
                .surname(candidate.getSurname())
                .phoneNumber(candidate.getPhoneNumber())
                .email(candidate.getEmail())
                .build();

    }
}




