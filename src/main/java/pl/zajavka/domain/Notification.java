package pl.zajavka.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import pl.zajavka.infrastructure.security.UserEntity;

@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
public class Notification {

    private Integer id;
    private String message;
    private User user;
    private CV cv;
    private JobOffer jobOffer;
}
