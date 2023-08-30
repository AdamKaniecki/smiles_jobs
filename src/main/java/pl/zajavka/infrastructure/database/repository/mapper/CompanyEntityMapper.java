package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Company;
import pl.zajavka.infrastructure.database.entity.CompanyEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyEntityMapper {

    @Mapping(target = "address", ignore = true)
    Company mapFromEntity(CompanyEntity entity);
}
