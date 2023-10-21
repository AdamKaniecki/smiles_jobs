package pl.zajavka.domain;

import lombok.*;
import pl.zajavka.infrastructure.security.Role;

import java.util.Set;

@Data
@With
@Builder
@EqualsAndHashCode(of = "id")
public class User {

    private Integer id;
    private String userName;
    private String email;
    private String password;
    private Boolean active;
    private Set<Role> roles;
    private Set<Advertisement>advertisements;


}
