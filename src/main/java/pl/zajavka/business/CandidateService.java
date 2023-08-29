package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.business.dao.CandidateDAO;
import pl.zajavka.domain.Candidate;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CandidateService {

    private final CandidateDAO candidateDAO;


    @Transactional
    public List<Candidate> findAvailable() {
        List<Candidate> availableCandidate = candidateDAO.findAvailable();
        log.info("Available candidate: [{}]", availableCandidate.size());
        return availableCandidate;
    }

    @Transactional
    public Candidate findCandidate(String pesel) {
        Optional<Candidate> candidate = candidateDAO.findByPesel(pesel);
        if (candidate.isEmpty()) {
            throw new NotFoundException("Could not find candidate by pesel: [%s]".formatted(pesel));
        }
        return candidate.get();
    }
}