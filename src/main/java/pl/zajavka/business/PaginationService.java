package pl.zajavka.business;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.entity.NotificationEntity;
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
}
