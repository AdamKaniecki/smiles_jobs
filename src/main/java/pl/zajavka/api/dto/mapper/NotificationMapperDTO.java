package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.api.dto.NotificationDTO;
import pl.zajavka.domain.Notification;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapperDTO {

    NotificationDTO map(Notification notification);

    Notification map(NotificationDTO notificationDTO);

    List<NotificationDTO> mapToDTOList(List<Notification> notifications);

    List<Notification> mapToList(List<NotificationDTO> notificationDTOs);
}
