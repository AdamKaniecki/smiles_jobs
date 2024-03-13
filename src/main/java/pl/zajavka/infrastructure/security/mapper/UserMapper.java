package pl.zajavka.infrastructure.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {


    User map(UserEntity userEntity);

    UserEntity map(User user);

    @Mapping(source = "username", target = "userName")
    @Mapping(source = "password", target = "password")
//    @Mapping(source = "active", target = "active")
//    @Mapping(source = "roles", target = "roles")
    User mapSecurity(org.springframework.security.core.userdetails.User userSecurity);

//    Role map(RoleEntity roleEntity);
//
//    RoleEntity map(Role role);

//    Set<RoleEntity> mapSet()
}
