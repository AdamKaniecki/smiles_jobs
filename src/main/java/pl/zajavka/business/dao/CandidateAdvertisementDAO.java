package pl.zajavka.business.dao;

import pl.zajavka.domain.CandidateAdvertisement;

import java.util.List;
import java.util.Optional;

public interface CandidateAdvertisementDAO {

     Optional<CandidateAdvertisement> findByNumber(String number);
    List<CandidateAdvertisement> findCandidateAdvertisements();

}
