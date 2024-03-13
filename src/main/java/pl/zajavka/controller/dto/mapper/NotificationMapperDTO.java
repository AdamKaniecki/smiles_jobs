package pl.zajavka.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.infrastructure.domain.Notification;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NotificationMapperDTO {

    NotificationDTO map(Notification notification);

    Notification map(NotificationDTO notificationDTO);

    List<NotificationDTO> mapToDTOList(List<Notification> notifications);

    List<Notification> mapToList(List<NotificationDTO> notificationDTOs);

//   Page<NotificationDTO> mapToDTOPage(Page<Notification> notifications);
//
//   Page<Notification> mapToPage(Page<NotificationDTO> notificationDTOs);
}
