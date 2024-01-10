package pl.zajavka.api.dto;

import lombok.*;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
