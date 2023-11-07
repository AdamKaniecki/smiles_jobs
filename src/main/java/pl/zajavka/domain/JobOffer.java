package pl.zajavka.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

import java.math.BigDecimal;

@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
public class JobOffer {

    private Integer id;
    private String companyName;
    private String position;
    private String responsibilities;
    private String requiredTechnologies;
    private String benefits;
    private BigDecimal salaryRange;
    private User user;
}
