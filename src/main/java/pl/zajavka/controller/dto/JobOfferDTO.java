package pl.zajavka.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String experience;
    private String jobLocation;
    private String typeOfContract;
    private String typeOfWork;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String requiredLanguage;
    private String requiredLanguageLevel;
    private String benefits;
    private String jobDescription;
    private OffsetDateTime jobOfferDateTime;
    @JsonIgnore
    private boolean active;
    @JsonIgnore
    private int neededStaff;
    @JsonIgnore
    private int hiredCount;;


}
