package pl.zajavka.infrastructure.domain;

import lombok.*;
import pl.zajavka.infrastructure.security.RoleEntity;

import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
public class User  {

    private Integer id;
    private String userName;
    private String email;
    private String password;
    private Boolean active;
    private Set<RoleEntity> roles;
    private Set<JobOffer> jobOffers;
    private CV cv;

    private Set<Notification> notificationsSend;
    private Set<Notification> notificationsReceive;

    //nie wiem czy to jest konieczne
//    private Address address;
}
