package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.business.dao.CandidateDAO;
import pl.zajavka.domain.Candidate;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CandidateService {

    private final CandidateDAO candidateDAO;


    @Transactional
    public List<Candidate> findCandidates() {
        List<Candidate> candidates = candidateDAO.findCandidates();
        log.info("Available candidates: [{}]", candidates.size());
        return candidates;
    }

    @Transactional
    public Candidate findCandidate(String email) {
        Optional<Candidate> candidate = candidateDAO.findByEmail(email);
        if (candidate.isEmpty()) {
            throw new NotFoundException("Could not find candidate by email: [%s]".formatted(email));
        }
        return candidate.get();
    }
}