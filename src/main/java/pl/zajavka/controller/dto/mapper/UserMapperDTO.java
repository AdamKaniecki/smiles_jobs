package pl.zajavka.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.controller.dto.UserDTO;
import pl.zajavka.infrastructure.domain.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapperDTO {
    User map(UserDTO userDTO);

    UserDTO map(User user);
}
