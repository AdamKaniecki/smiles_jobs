package pl.zajavka.infrastructure.database.repository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.api.dto.mapper.CandidateMapper;
import pl.zajavka.business.dao.CandidateAdvertisementDAO;
import pl.zajavka.domain.Candidate;
import pl.zajavka.domain.CandidateAdvertisement;
import pl.zajavka.infrastructure.database.entity.CandidateAdvertisementEntity;
import pl.zajavka.infrastructure.database.repository.jpa.CandidateAdvertisementJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.CandidateJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CandidateAdvertisementMapper;
import pl.zajavka.infrastructure.database.repository.mapper.CandidateEntityMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Repository
@AllArgsConstructor
public class CandidateAdvertisementRepository implements CandidateAdvertisementDAO {

    private final CandidateAdvertisementJpaRepository candidateAdvertisementJpaRepository;
    private final CandidateAdvertisementMapper candidateAdvertisementMapper;
    private final CandidateJpaRepository candidateJpaRepository;
    private final CandidateMapper candidateMapper;
//    private final



    @Override
    public Optional<CandidateAdvertisement> findByNumber(String number) {
        return Optional.empty();
    }

    @Override
    public List<CandidateAdvertisement> findCandidateAdvertisements() {
        return null;
    }




}
