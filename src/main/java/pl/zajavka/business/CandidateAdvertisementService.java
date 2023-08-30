package pl.zajavka.business;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.zajavka.business.dao.CandidateAdvertisementDAO;
import pl.zajavka.domain.Candidate;
import pl.zajavka.domain.CandidateAdvertisement;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CandidateAdvertisementService {
    private final CandidateAdvertisementDAO candidateAdvertisementDAO;

    @Transactional
    public List<CandidateAdvertisement> findCandidateAdvertisements(){
        List<CandidateAdvertisement> candidateAdvertisements = candidateAdvertisementDAO.findCandidateAdvertisements();
        log.info("Available candidateAdvertisements: [{}]", candidateAdvertisements.size());
        return candidateAdvertisements;
    }

    @Transactional
    public CandidateAdvertisement findByNumber(String number){
        Optional<CandidateAdvertisement> candidateAdvertisement = candidateAdvertisementDAO.findByNumber(number);
        if (candidateAdvertisement.isEmpty()) {
            throw new NotFoundException("Could not find candidateAdvertisement by email: [%s]".formatted(number));
        }
        return candidateAdvertisement.get();
    }


}
