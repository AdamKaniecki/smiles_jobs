package pl.zajavka.api.dto;

import lombok.*;
import pl.zajavka.domain.User;

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

    //    @NumberFormat(style = NumberFormat.Style.NUMBER)
//    private BigDecimal salaryMin;
//    @NumberFormat(style = NumberFormat.Style.NUMBER)
//    private BigDecimal salaryMax;
    private OffsetDateTime jobOfferDateTime;
    private User user;
}
