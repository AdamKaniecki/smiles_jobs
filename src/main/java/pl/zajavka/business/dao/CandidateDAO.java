package pl.zajavka.business.dao;

import pl.zajavka.domain.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateDAO {

     Candidate createCandidate(Candidate newCandidate);
     Optional<Candidate> findByEmail(String email);
     List<Candidate> findCandidatesList();
     Candidate updateCandidate(Integer candidateId, Candidate updatedCandidate);
     Candidate saveCandidate(Candidate candidate);
     boolean deleteCandidate(Integer candidateId);

}
