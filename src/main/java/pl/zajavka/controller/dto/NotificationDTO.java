package pl.zajavka.controller.dto;

import lombok.*;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.database.entity.Status;

import java.time.LocalDateTime;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
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
