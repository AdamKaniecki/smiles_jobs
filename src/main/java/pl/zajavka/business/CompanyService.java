package pl.zajavka.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.business.dao.CompanyDAO;
import pl.zajavka.domain.Candidate;
import pl.zajavka.domain.CandidateAdvertisement;
import pl.zajavka.domain.Company;
import pl.zajavka.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class CompanyService {
    private final CompanyDAO companyDAO;

    @Transactional
    public List<Company> findCompanies() {
        List<Company> companies = companyDAO.findCompanies();
        log.info("Available companies: [{}]", companies.size());
        return companies;
    }

    @Transactional
    public Company findCompany(String email) {
        Optional<Company> company = companyDAO.findByEmail(email);
        if (company.isEmpty()) {
            throw new NotFoundException("Could not find company by email: [%s]".formatted(email));
        }
        return company.get();
    }


}

