package pl.zajavka.infrastructure.business;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.database.repository.NotificationRepository;

@Service
@AllArgsConstructor
public class SortingService {
    private final JobOfferRepository jobOfferRepository;
    private final CvRepository cvRepository;
    private final NotificationRepository notificationRepository;



    public void sortJobOfferById(){
        Sort sort = Sort.by("id").ascending();


        jobOfferRepository.findAll(sort)
                .forEach(jobOffer -> System.out.println("jobOffer: " + jobOffer));


    }

}
