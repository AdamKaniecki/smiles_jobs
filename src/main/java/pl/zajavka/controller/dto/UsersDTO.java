package pl.zajavka.controller.dto;

import lombok.*;

import java.util.List;
@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class UsersDTO {
    private List<UserDTO> usersDTO;
}
