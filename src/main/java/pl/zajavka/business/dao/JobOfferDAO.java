package pl.zajavka.business.dao;

import pl.zajavka.domain.JobOffer;

import java.util.List;
import java.util.Optional;

public interface JobOfferDAO {
    Optional<JobOffer> findByNumber(String number);
    List<JobOffer> findJobOffers();
}
