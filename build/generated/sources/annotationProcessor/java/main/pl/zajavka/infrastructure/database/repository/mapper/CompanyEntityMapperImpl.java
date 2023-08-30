package pl.zajavka.infrastructure.database.repository.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.zajavka.domain.Company;
import pl.zajavka.infrastructure.database.entity.CompanyEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-29T14:33:33+0200",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class CompanyEntityMapperImpl implements CompanyEntityMapper {

    @Override
    public Company mapFromEntity(CompanyEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Company.CompanyBuilder company = Company.builder();

        company.companyId( entity.getCompanyId() );
        company.companyName( entity.getCompanyName() );
        company.companyDescription( entity.getCompanyDescription() );
        company.email( entity.getEmail() );
        company.recruitmentCriteria( entity.getRecruitmentCriteria() );
        company.requestEmployment( entity.getRequestEmployment() );

        return company.build();
    }
}
