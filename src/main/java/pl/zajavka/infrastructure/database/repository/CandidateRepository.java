package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.CandidateDAO;
import pl.zajavka.domain.Candidate;
import pl.zajavka.infrastructure.database.entity.CandidateEntity;
import pl.zajavka.infrastructure.database.repository.jpa.CandidateAdvertisementJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.CandidateJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CandidateAdvertisementMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CandidateEntityMapper;


import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CandidateRepository implements CandidateDAO {

    private final CandidateJpaRepository candidateJpaRepository;
    private final CandidateEntityMapper candidateEntityMapper;

    private final CandidateAdvertisementJpaRepository candidateAdvertisementJpaRepository;
    private final CandidateAdvertisementMapper candidateAdvertisementMapper;


    @Override
    public Optional<Candidate> findByEmail(String email) {
        return candidateJpaRepository.findByEmail(email)
                .map(candidateEntityMapper::mapFromEntity);
    }
    @Override
    public List<Candidate> findCandidates() {
        return candidateJpaRepository.findAll().stream()
                .map(candidateEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public Candidate saveCandidate(Candidate candidate) {
        CandidateEntity toSave = candidateEntityMapper.mapToEntity(candidate);
        CandidateEntity saved = candidateJpaRepository.save(toSave);
        return candidateEntityMapper.mapFromEntity(saved);
    }


    public Candidate updateCandidate(Integer candidateId, Candidate updatedCandidate) {
        CandidateEntity candidateEntity = candidateJpaRepository.findById(candidateId).orElse(null);
        if (candidateEntity != null) {
            // Aktualizuj pola encji na podstawie pól obiektu updatedCandidate
            candidateEntity.setName(updatedCandidate.getName());
            candidateEntity.setSurname(updatedCandidate.getSurname());
            candidateEntity.setEmail(updatedCandidate.getEmail());
            candidateEntity.setPhoneNumber(updatedCandidate.getPhoneNumber());
            candidateEntity.setAvailabilityStatus(updatedCandidate.getAvailabilityStatus());
            // itd.

            CandidateEntity savedEntity = candidateJpaRepository.save(candidateEntity);
            return candidateEntityMapper.mapFromEntity(savedEntity);
        }
        return null;
    }

    public boolean deleteCandidate(Integer candidateId) {
        CandidateEntity candidateEntity = candidateJpaRepository.findById(candidateId).orElse(null);
        if (candidateEntity != null) {
            candidateJpaRepository.delete(candidateEntity);
            return true;
        }
        return false;
    }

    public Candidate createCandidate(Candidate newCandidate) {
        // Mapowanie obiektu DTO (newCandidate) na encję (CandidateEntity)
        CandidateEntity candidateEntity = candidateEntityMapper.mapToEntity(newCandidate);

        // Zapisanie encji w bazie danych
        CandidateEntity savedEntity = candidateJpaRepository.save(candidateEntity);

        // Mapowanie z powrotem z encji na obiekt DTO i zwrócenie go
        return candidateEntityMapper.mapFromEntity(savedEntity);
    }



}




