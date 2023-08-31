package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.business.dao.CandidateDAO;
import pl.zajavka.domain.Address;
import pl.zajavka.domain.Candidate;
import pl.zajavka.domain.CandidateAdvertisement;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class CandidateService {

    private final CandidateDAO candidateDAO;


    @Transactional
    public List<Candidate> findCandidates() {
        List<Candidate> candidates = candidateDAO.findCandidates();
        log.info("Available candidates: [{}]", candidates.size());
        return candidates;
    }

    @Transactional
    public Candidate findCandidate(String email) {
        Optional<Candidate> candidate = candidateDAO.findByEmail(email);
        if (candidate.isEmpty()) {
            throw new NotFoundException("Could not find candidate by email: [%s]".formatted(email));
        }
        return candidate.get();
    }

    private Candidate buildCandidate(Candidate candidate, Address address, CandidateAdvertisement candidateAdvertisement) {
        return Candidate.builder()
                .name(candidate.getName())
                .surname(candidate.getSurname())
                .phoneNumber(candidate.getPhoneNumber())
                .email(candidate.getEmail())
                .address(Address.builder()
                .country(address.getCountry())
                .city(address.getCity())
                .postalCode(address.getPostalCode())
                .StreetAndNumber(address.getStreetAndNumber()) .build())
                .candidateAdvertisements(Set.of(candidateAdvertisement))
                .build();



//               .address(Address.builder()
//                        .country(inputData.getCandidateAddressCountry())
//                        .city(inputData.getCandidateAddressCity())
//                        .postalCode(inputData.getCandidateAddressPostalCode())
//                        .address(inputData.getCandidateAddressStreet())
//                        .build())
//                .invoices(Set.of(invoice))
//                .build();
    }
}