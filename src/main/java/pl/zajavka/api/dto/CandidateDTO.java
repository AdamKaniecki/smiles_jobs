package pl.zajavka.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.Candidate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {




//        @Email
//        private String existingCustomerEmail;
//
        private String name;
        private String surname;
//        @Size
//        @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
        private String phoneNumber;
//        @Email
        private String email;


//        public Map<String, String> asMap() {
//
//                Map<String, String> result = new HashMap<>();
//                ofNullable(name).ifPresent(value -> result.put("candidateName", value));
//                ofNullable(surname).ifPresent(value -> result.put("candidateSurname", value));
//                ofNullable(phoneNumber).ifPresent(value -> result.put("candidatePhone", value));
//                ofNullable(email).ifPresent(value -> result.put("candidateEmail", value));
//
//                return result;
        }




