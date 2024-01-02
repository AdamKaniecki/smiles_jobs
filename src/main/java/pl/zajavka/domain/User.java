package pl.zajavka.domain;

import lombok.*;
import pl.zajavka.infrastructure.security.Role ;
import pl.zajavka.infrastructure.security.UserEntity;

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
    private Set<Role> roles;
    private Set<JobOffer> jobOffers;
    private CV cv;


}
