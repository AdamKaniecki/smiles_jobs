package pl.zajavka.domain;

import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

import java.time.OffsetDateTime;

@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
public class Advertisement {

    private Integer id;
    private String name;
    private String surname;
    private String workExperience;
    private String knowledgeOfTechnologies;
    private OffsetDateTime dateTime;
    private User user;
}
