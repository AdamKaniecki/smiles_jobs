package pl.zajavka.business;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.api.dto.mapper.CandidateMapper;
import pl.zajavka.business.dao.CandidateDAO;
import pl.zajavka.domain.Candidate;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CandidateService {

    private final CandidateDAO candidateDAO;
    private final CandidateMapper candidateMapper;



    private Candidate buildCandidate(Candidate candidate) {
        return Candidate.builder()
                .name(candidate.getName())
                .surname(candidate.getSurname())
                .phoneNumber(candidate.getPhoneNumber())
                .email(candidate.getEmail())
                .build();

    }
@Transactional
    public Candidate save(Candidate candidate) {
        // Poniżej znajduje się przykład prostego sprawdzenia, czy pole "name" nie jest puste.
        if (candidate.getId() == null) {
            throw new IllegalArgumentException("Pole 'name' nie może być puste.");
        }

        // Tutaj można dodać inne reguły walidacji w zależności od potrzeb.

        return candidateDAO.save(candidate);
    }


    @Transactional
    public CandidateDTO getCandidateById(Long id) {
        Optional<Candidate> candidateOptional = candidateDAO.findById(id);

        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            return candidateMapper.mapToCandidateDTO(candidate);
        } else {

            throw new EntityNotFoundException("Kandydat o podanym ID nie istnieje");
        }
    }

@Transactional
public List<Candidate> findAvailableCandidates() {
    List<Candidate> availableCandidates = candidateDAO.findAll();

    log.info("Available candidates: [{}]", availableCandidates.size());
    return availableCandidates;
    }



//    public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
//        Candidate candidate = candidateMapper.mapToDomain(candidateDTO);
//
//        // Zapisz kandydata w DAO
//        Candidate savedCandidate = candidateDAO.save(candidate);
//
//        // Przemapuj zapisanego kandydata z powrotem na DTO
//        CandidateDTO savedCandidateDTO;
//        savedCandidateDTO = candidateMapper.mapToCandidateDTO(savedCandidate);
//
//        return savedCandidateDTO;
//    }

    public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
        // Pobierz identyfikator kandydata z DTO
        Long candidateId = candidateDTO.getId();

        // Sprawdź, czy kandydat o podanym identyfikatorze już istnieje
        Optional<Candidate> existingCandidate = candidateDAO.findById(candidateId);

        if (existingCandidate.isPresent()) {
            throw new IllegalArgumentException("Kandydat o identyfikatorze '" + candidateId + "' już istnieje.");
        }


        // Mapuj obiekt DTO na obiekt domeny
        Candidate candidate = candidateMapper.mapToDomain(candidateDTO);


        // Zapisz kandydata w DAO
        Candidate savedCandidate = candidateDAO.save(candidate);

        // Przemapuj zapisanego kandydata z powrotem na DTO
        return candidateMapper.mapToCandidateDTO(savedCandidate);

    }


    public boolean deleteCandidate(Long id) {
        return false;
    }

    public List<CandidateDTO> getAllCandidates() {
        return null;
    }
}
