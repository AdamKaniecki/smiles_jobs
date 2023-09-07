package pl.zajavka.api.dto.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.zajavka.api.dto.CompanyDTO;
import pl.zajavka.domain.Company;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-07T11:51:04+0200",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class CompanyMapperImpl implements CompanyMapper {

    @Override
    public CompanyDTO map(Company company) {
        if ( company == null ) {
            return null;
        }

        CompanyDTO.CompanyDTOBuilder companyDTO = CompanyDTO.builder();

        companyDTO.companyName( company.getCompanyName() );
        companyDTO.email( company.getEmail() );

        return companyDTO.build();
    }
}
