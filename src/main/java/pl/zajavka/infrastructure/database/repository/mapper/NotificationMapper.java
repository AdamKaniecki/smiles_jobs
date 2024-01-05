package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.api.dto.NotificationDTO;
import pl.zajavka.domain.Notification;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapper {
    NotificationEntity map(Notification notification);

    Notification map(NotificationEntity notificationEntity);

    List<NotificationEntity> mapToEntityList(List<Notification> notifications);

    List<Notification> mapToList(List<NotificationEntity> notificationEntities);
}
