package pl.zajavka.infrastructure.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import pl.zajavka.infrastructure.database.entity.Status;

import java.time.LocalDateTime;

@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
public class Notification {

    private Integer id;
    private String candidateMessage;
    private String companyMessage;
    private User sender;
    private User receiver;
    private Status status;
    private LocalDateTime dateTime;
    private CV cv;
    private JobOffer jobOffer;
}
