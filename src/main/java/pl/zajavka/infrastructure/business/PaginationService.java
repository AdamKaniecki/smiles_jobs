package pl.zajavka.infrastructure.business;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import pl.zajavka.controller.dto.NotificationDTO;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.jpa.CvJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.JobOfferJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.NotificationJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CvMapper;
import pl.zajavka.infrastructure.database.repository.mapper.JobOfferMapper;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.JobOffer;

import java.util.List;

@Service
@AllArgsConstructor
public class PaginationService {

    private final JobOfferJpaRepository jobOfferRepository;
    private final CvJpaRepository cvRepository;
    private final JobOfferMapper jobOfferMapper;
    private final CvMapper cvMapper;

    public Page<NotificationDTO> createNotificationPage(List<NotificationDTO> notifications, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), notifications.size());

        if (start < 0 || start >= notifications.size() || end < start) {
            // Jeśli start jest poza zakresem listy lub end jest mniejszy niż start, zwróć pustą stronę
            return Page.empty(pageable);
        }

        return new PageImpl<>(notifications.subList(start, end), pageable, notifications.size());
    }


    public Page<CV> findAll(Pageable pageable) {
        Page<CvEntity> cvEntities = cvRepository.findAll(pageable);
        return cvEntities.map(cvMapper::map);
    }

    public Page<JobOffer> findAllJobOffersForPage(Pageable pageable) {
        Page<JobOfferEntity> jobOfferEntities = jobOfferRepository.findAll(pageable);
        return jobOfferEntities.map(jobOfferMapper::map);
    }


}


