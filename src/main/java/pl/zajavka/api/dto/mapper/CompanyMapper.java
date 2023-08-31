package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.api.dto.CompanyDTO;
import pl.zajavka.domain.Candidate;
import pl.zajavka.domain.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    Company map(final CompanyDTO companyDTO);
}
