package pl.zajavka.domain;

import lombok.*;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

//    private CV cv;

}
