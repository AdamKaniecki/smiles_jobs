package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.CompanyDAO;
import pl.zajavka.domain.Candidate;
import pl.zajavka.domain.Company;
import pl.zajavka.infrastructure.database.repository.jpa.CompanyJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CompanyEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CompanyRepository implements CompanyDAO {


    private final CompanyJpaRepository companyJpaRepository;
    private final CompanyEntityMapper companyEntityMapper;


    @Override
    public Optional<Company> findByEmail(String email) {
        return companyJpaRepository.findByEmail(email)
                .map(companyEntityMapper::mapFromEntity);
    }

    @Override
        public List<Company> findCompanies() {
            return companyJpaRepository.findAll().stream()
                    .map(companyEntityMapper::mapFromEntity)
                    .toList();
    }
}
