package pl.zajavka.business.dao;

import pl.zajavka.domain.Company;

import java.util.List;
import java.util.Optional;

public interface CompanyDAO {


    Company create(Company company);
    Optional<Company> findByEmail(String email);
    List<Company> findCompaniesList();
    Company updateCompany(Integer companyId, Company updatedCompany);
    Company saveCompany(Company company);

    boolean deleteCompany(Integer companyId);
}
