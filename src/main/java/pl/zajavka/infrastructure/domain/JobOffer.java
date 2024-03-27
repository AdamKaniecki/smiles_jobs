package pl.zajavka.infrastructure.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

import java.math.BigDecimal;
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
    private Boolean active;
    private Integer neededStaff;
    private Integer hiredCount;
    private User user;

    // Metoda sprawdzająca, czy osiągnięto docelową liczbę zatrudnionych pracowników
    public boolean isFullyStaffed() {
        return hiredCount >= neededStaff;
    }

}
