package pl.zajavka.infrastructure.database.repository;
import pl.zajavka.business.dao.CandidateAdvertisementDAO;
import pl.zajavka.domain.CandidateAdvertisement;
import java.util.List;
import java.util.Optional;

public class CandidateAdvertisementRepository implements CandidateAdvertisementDAO {


    @Override
    public Optional<CandidateAdvertisement> findByNumber(String number) {
        return Optional.empty();
    }

    @Override
    public List<CandidateAdvertisement> findCandidateAdvertisements() {
        return null;
    }
}
