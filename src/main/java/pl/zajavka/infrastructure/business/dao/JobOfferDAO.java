package pl.zajavka.infrastructure.business.dao;

import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface JobOfferDAO {

    Optional<JobOffer> findByIdOpt(Integer id);
    JobOffer findById(Integer id);
    List<JobOffer> findListByUser(User user);
    Optional<JobOffer> findByUser(User loggedInUser);
    JobOffer saveJobOffer(JobOffer jobOffer);
    JobOffer create(JobOffer jobOffer, User user);
   JobOffer updateJobOffer(JobOffer jobOffer);

    void deleteById(Integer jobOfferId);

    List<JobOffer> searchJobOffersByKeywordAndCategory(String keyword, String category);

    List<JobOffer> searchJobOffersBySalary(String category, BigDecimal salary);

}
