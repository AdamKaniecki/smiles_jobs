package pl.zajavka.infrastructure.domain;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@With
public class Users {

private List<User> users;

}
