package pl.zajavka.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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



//
//        @Email
//        private String existingCustomerEmail;

        private String candidateName;
        private String candidateSurname;
        @Size
        @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
        private String candidatePhone;
        @Email
        private String candidateEmail;
        private String candidateAddressCountry;
        private String candidateAddressCity;
        private String candidateAddressPostalCode;
        private String candidateStreetAndNumber;

        private String userName;

        private String password;

        public Map<String, String> asMap() {

                Map<String, String> result = new HashMap<>();
                ofNullable(candidateName).ifPresent(value -> result.put("candidateName", value));
                ofNullable(candidateSurname).ifPresent(value -> result.put("candidateSurname", value));
                ofNullable(candidatePhone).ifPresent(value -> result.put("candidatePhone", value));
                ofNullable(candidateEmail).ifPresent(value -> result.put("candidateEmail", value));
//                ofNullable(existingCustomerEmail).ifPresent(value -> result.put("existingCustomerEmail", value));
                ofNullable(candidateAddressCountry).ifPresent(value -> result.put("candidateAddressCountry", value));
                ofNullable(candidateAddressCity).ifPresent(value -> result.put("candidateAddressCity", value));
                ofNullable(candidateAddressPostalCode).ifPresent(value -> result.put("candidateAddressPostalCode", value));
                ofNullable(candidateStreetAndNumber).ifPresent(value -> result.put("candidateStreetAndNumber", value));
                ofNullable(userName).ifPresent(value -> result.put("userName", value));
                ofNullable(password).ifPresent(value -> result.put("password", value));
                return result;
        }

        }
