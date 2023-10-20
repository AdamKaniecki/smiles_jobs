package pl.zajavka.infrastructure.security.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.security.UserEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapperSecurity {


    User map(UserEntity userEntity);

    UserEntity map(User user);

//    Role map(RoleEntity roleEntity);
//
//    RoleEntity map(Role role);

//    Set<RoleEntity> mapSet()
}
