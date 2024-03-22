package pl.zajavka.infrastructure.business;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.zajavka.infrastructure.database.repository.jpa.CvJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.JobOfferJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.NotificationJpaRepository;

@Service
@AllArgsConstructor
public class SortingService {
    private final JobOfferJpaRepository jobOfferRepository;
    private final CvJpaRepository cvRepository;
    private final NotificationJpaRepository notificationJpaRepository;



    public void sortJobOfferById(){
        Sort sort = Sort.by("id").ascending();


        jobOfferRepository.findAll(sort)
                .forEach(jobOffer -> System.out.println("jobOffer: " + jobOffer));


    }

}
