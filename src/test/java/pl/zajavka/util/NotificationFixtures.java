package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
import pl.zajavka.infrastructure.database.entity.Status;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@UtilityClass
public class NotificationFixtures {

public static NotificationEntity sampleNotification1(){
    LocalDateTime jobOfferDateTime = LocalDateTime.now();
    return NotificationEntity.builder()
            .companyMessage("randomMessage1")
            .candidateMessage("randomMessage2")
            .status(Status.WAITING_FOR_INTERVIEW)
            .dateTime(jobOfferDateTime)
            .build();

}

    public static NotificationEntity sampleNotification2(){
        LocalDateTime jobOfferDateTime = LocalDateTime.now();
        return NotificationEntity.builder()
                .companyMessage("randomMessage3")
                .candidateMessage("randomMessage4")
                .status(Status.WAITING_FOR_INTERVIEW)
                .dateTime(jobOfferDateTime)
                .build();

    }
}
