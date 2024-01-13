package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.api.dto.NotificationDTO;
import pl.zajavka.domain.Notification;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {

    NotificationEntity map(Notification notification);

    Notification map(NotificationEntity notificationEntity);

//    @Mapping(target = "cv.user.roles", source = "notificationEntity.cv.user.roles")
//    @Mapping(target = "jobOffer.user.roles", source = "notificationEntity.jobOffer.user.roles")
//    Notification mapWithRoles(NotificationEntity notificationEntity);

    List<NotificationEntity> mapToEntityList(List<Notification> notifications);

    List<Notification> mapToList(List<NotificationEntity> notificationEntities);
}
