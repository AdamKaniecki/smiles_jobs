package pl.zajavka.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;
import pl.zajavka.domain.Notification;
import pl.zajavka.infrastructure.security.Role;
import pl.zajavka.infrastructure.security.RoleEntity;

import java.util.List;
import java.util.Set;
@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
public class UserDTO {
    private Integer id;
    private String userName;
    private String email;
    private String password;
    private Boolean active;
    private Set<RoleEntity> roles;
    private Set<Notification> notificationsSend;
    private Set<Notification> notificationsReceive;
}
