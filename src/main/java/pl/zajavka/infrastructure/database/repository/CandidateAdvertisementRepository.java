package pl.zajavka.infrastructure.database.repository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.CandidateAdvertisementDAO;
import pl.zajavka.domain.CandidateAdvertisement;
import pl.zajavka.infrastructure.database.repository.jpa.CandidateAdvertisementJpaRepository;
import pl.zajavka.infrastructure.database.repository.jpa.CandidateJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CandidateEntityMapper;

import java.util.List;
import java.util.Optional;
@Repository
@AllArgsConstructor
public class CandidateAdvertisementRepository implements CandidateAdvertisementDAO {

    private final CandidateAdvertisementJpaRepository candidateAdvertisementJpaRepository;
//    private final CandidateEntityMapper candidateEntityMapper;



    @Override
    public Optional<CandidateAdvertisement> findByNumber(String number) {
        return Optional.empty();
    }

    @Override
    public List<CandidateAdvertisement> findCandidateAdvertisements() {
        return null;
    }
}
