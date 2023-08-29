package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.CandidateDAO;
import pl.zajavka.domain.Candidate;
import pl.zajavka.infrastructure.database.repository.jpa.CandidateJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CandidateEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CandidateRepository implements CandidateDAO {

    private final CandidateJpaRepository candidateJpaRepository;
    private final CandidateEntityMapper candidateEntityMapper;


    @Override
    public List<Candidate> findAvailable() {
        return candidateJpaRepository.findAll().stream()
                .map(candidateEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public Optional<Candidate> findByPesel(String pesel) {
        return candidateJpaRepository.findByPesel(pesel)
                .map(candidateEntityMapper::mapFromEntity);
    }
}
