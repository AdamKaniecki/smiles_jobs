package pl.zajavka.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.JobOfferDAO;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class JobOfferService {
    private final JobOfferDAO jobOfferDAO;

    @Transactional
    public List<JobOffer> findJobOffers(){
        List<JobOffer> jobOffersList = jobOfferDAO.findJobOffers();
        log.info("Available jobOffersList: [{}]", jobOffersList.size());
        return jobOffersList;
    }

    @Transactional
    public JobOffer findByNumber(String number){
        Optional<JobOffer> jobOffer = jobOfferDAO.findByNumber(number);
        if (jobOffer.isEmpty()) {
            throw new NotFoundException("Could not find jobOffer by email: [%s]".formatted(number));
        }
        return jobOffer.get();
    }


}
