package pl.zajavka.business.dao;

import pl.zajavka.domain.Candidate;
import pl.zajavka.domain.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDAO {

    Optional<Company> findByEmail(String email);
    List<Company> findCompanies();

    Company saveCompany(Company company);
}
