package pl.zajavka.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.zajavka.business.dao.CompanyDAO;
import pl.zajavka.domain.Company;
import pl.zajavka.domain.Company;
import pl.zajavka.infrastructure.database.entity.CompanyEntity;
import pl.zajavka.infrastructure.database.entity.CompanyEntity;
import pl.zajavka.infrastructure.database.entity.CompanyEntity;
import pl.zajavka.infrastructure.database.repository.jpa.CompanyJpaRepository;
import pl.zajavka.infrastructure.database.repository.mapper.CompanyEntityMapper;


import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class CompanyRepository implements CompanyDAO {


    private final CompanyJpaRepository companyJpaRepository;
    private final CompanyEntityMapper companyEntityMapper;

    public Company create(Company company) {
        if (company != null) {
            // Mapowanie obiektu Company na CompanyEntity
            CompanyEntity companyEntity = companyEntityMapper.mapToEntity(company);

            // Zapisanie firmy do bazy danych
            CompanyEntity createdCompanyEntity = companyJpaRepository.save(companyEntity);

            // Mapowanie z powrotem z CompanyEntity na Company i zwrócenie nowej firmy
            return companyEntityMapper.mapFromEntity(createdCompanyEntity);
        }
        return null;
    }


    @Override
    public Optional<Company> findByEmail(String email) {
        return companyJpaRepository.findByEmail(email).map(companyEntityMapper::mapFromEntity);
    }

    @Override
    public List<Company> findCompaniesList() {
        return companyJpaRepository.findAll().stream().map(companyEntityMapper::mapFromEntity).toList();
    }


    public Company updateCompany(Integer companyId, Company updatedCompany) {
        CompanyEntity companyEntity = companyJpaRepository.findById(companyId).orElse(null);
        if (companyEntity != null) {

            // Aktualizuj pola encji na podstawie pól obiektu updatedCompany
            companyEntity.setCompanyName(updatedCompany.getCompanyName());
            companyEntity.setCompanyDescription(updatedCompany.getCompanyDescription());
            companyEntity.setEmail(updatedCompany.getEmail());
            companyEntity.setRecruitmentCriteria(updatedCompany.getRecruitmentCriteria());
            companyEntity.setRequestEmployment(updatedCompany.getRequestEmployment());

            if (updatedCompany.getAddress() != null) {
                companyEntity.getAddress().setCountry(updatedCompany.getAddress().getCountry());
                companyEntity.getAddress().setCity(updatedCompany.getAddress().getCity());
                companyEntity.getAddress().setPostalCode(updatedCompany.getAddress().getPostalCode());
                companyEntity.getAddress().setStreetAndNumber(updatedCompany.getAddress().getStreetAndNumber());
            }
            // itd.

            CompanyEntity savedEntity = companyJpaRepository.save(companyEntity);
            return companyEntityMapper.mapFromEntity(savedEntity);
        }
        return null;
    }

    @Override
    public Company saveCompany(Company company) {
        CompanyEntity toSave = companyEntityMapper.mapToEntity(company);
        CompanyEntity saved = companyJpaRepository.save(toSave);
        return companyEntityMapper.mapFromEntity(saved);
    }

    @Override
    public boolean deleteCompany(Integer companyId) {
        CompanyEntity companyEntity = companyJpaRepository.findById(companyId).orElse(null);
        if (companyEntity != null) {
            companyJpaRepository.delete(companyEntity);
            return true;
        }
        return false;
    }
}
