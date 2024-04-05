package pl.zajavka.controller.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCardDTO {

    private Integer id;
    private String office;
    private String scopeOperations;
    private String recruitmentEmail;
    @Size(min = 7,max = 15)
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String phoneNumber;
    private String companyDescription;
    private String technologiesAndTools;
    private String certificatesAndAwards;

    private Address address;


    public Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        Optional.ofNullable(id).ifPresent(value -> result.put("id", value.toString()));
        Optional.ofNullable(office).ifPresent(value -> result.put("office", value));
        Optional.ofNullable(scopeOperations).ifPresent(value -> result.put("scopeOperations", value));
        Optional.ofNullable(recruitmentEmail).ifPresent(value -> result.put("recruitmentEmail", value));
        Optional.ofNullable(phoneNumber).ifPresent(value -> result.put("phoneNumber", value));
        Optional.ofNullable(technologiesAndTools).ifPresent(value -> result.put("technologiesAndTools", value));
        Optional.ofNullable(certificatesAndAwards).ifPresent(value -> result.put("certificatesAndAwards", value));
        Optional.ofNullable(address).ifPresent(value -> result.put("address", value.toString()));



        return result;
    }
}
