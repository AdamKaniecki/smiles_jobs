package pl.zajavka.api.dto;

import lombok.*;
import pl.zajavka.domain.CV;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.User;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Integer id;
    private String message;
    private CV cv;
    private JobOffer jobOffer;
    private User user;
}
