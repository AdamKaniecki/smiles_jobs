package pl.zajavka.business.dao;

import pl.zajavka.domain.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateDAO {
    public Optional<Candidate> findByEmail(String email);
    public List<Candidate> findCandidates();
}
