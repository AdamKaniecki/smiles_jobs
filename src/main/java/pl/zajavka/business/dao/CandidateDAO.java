package pl.zajavka.business.dao;

import pl.zajavka.domain.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateDAO {
    public Optional<Candidate> findByPesel(String pesel);
    public List<Candidate> findAvailable();
}
