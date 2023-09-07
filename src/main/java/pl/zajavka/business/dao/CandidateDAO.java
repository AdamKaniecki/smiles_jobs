package pl.zajavka.business.dao;

import pl.zajavka.domain.Candidate;

import java.util.List;
import java.util.Optional;

public interface CandidateDAO {

    Optional<Candidate> findById(Long id);

    Optional<Candidate> findByEmail(String email);

    Candidate createCandidate(Candidate newCandidate);

     Candidate updateCandidate(Integer candidateId, Candidate updatedCandidate);
     Candidate save(Candidate candidate);
     boolean deleteCandidate(Integer candidateId);



    List<Candidate> findAll();


}
