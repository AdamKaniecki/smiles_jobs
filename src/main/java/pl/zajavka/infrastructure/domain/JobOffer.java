package pl.zajavka.infrastructure.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

import java.time.OffsetDateTime;

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

//    @NumberFormat(style = NumberFormat.Style.NUMBER)
//    private BigDecimal salaryMin;
//    @NumberFormat(style = NumberFormat.Style.NUMBER)
//    private BigDecimal salaryMax;
    private OffsetDateTime jobOfferDateTime;
    private User user;

}
