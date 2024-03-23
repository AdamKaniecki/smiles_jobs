package pl.zajavka.controller.dto;

import lombok.*;
import pl.zajavka.infrastructure.domain.User;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobOfferDTO {
    private Integer id;
    private String companyName;
    private String position;
    private String responsibilities;
    private String requiredTechnologies;
    private String benefits;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private OffsetDateTime jobOfferDateTime;
    private Boolean active;

}
