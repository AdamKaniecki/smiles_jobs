package pl.zajavka.infrastructure.business;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.database.repository.NotificationRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class PaginationService {

    private final JobOfferRepository jobOfferRepository;
    private final CvRepository cvRepository;
    private final NotificationRepository notificationRepository;


    public Page<JobOfferEntity> paginate(int pageNumber, int pageSize){
        System.out.printf("pagination. pn: %s, ps: %s ####%n", pageNumber, pageSize);

        Sort sort = Sort.by("id").ascending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        return jobOfferRepository.findAll(pageable);
    }

    public Page<NotificationDTO> createNotificationPage(List<NotificationDTO> notifications, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), notifications.size());

        if (start < 0 || start >= notifications.size() || end < start) {
            // Jeśli start jest poza zakresem listy lub end jest mniejszy niż start, zwróć pustą stronę
            return Page.empty(pageable);
        }

        return new PageImpl<>(notifications.subList(start, end), pageable, notifications.size());
    }
}
