package pl.zajavka.infrastructure.domain;

import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
@With
@Builder
public class MeetingInterviewRequest {

    private Integer cvId;
    private Integer notificationId;
    private Integer jobOfferId;
    private LocalDateTime proposedDateTime;

}
