package pl.zajavka.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.With;

import java.util.List;
@Data
@With
@Builder

public class UsersDTO {
    private List<UserDTO> usersDTO;
}
