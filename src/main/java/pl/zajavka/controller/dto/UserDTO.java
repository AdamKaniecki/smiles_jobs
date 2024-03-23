package pl.zajavka.controller.dto;

import jakarta.validation.constraints.Email;
import lombok.*;
import pl.zajavka.infrastructure.domain.Notification;
import pl.zajavka.infrastructure.security.RoleEntity;

import java.util.Set;
@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Integer id;
    private String userName;
    @Email
    private String email;
    private String password;
    private Boolean visible;
//    private Set<RoleEntity> roles;
    private Set<Notification> notificationsSend;
    private Set<Notification> notificationsReceive;
}
