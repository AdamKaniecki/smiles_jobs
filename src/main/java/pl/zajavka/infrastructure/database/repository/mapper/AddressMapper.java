package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Address;
import pl.zajavka.infrastructure.database.entity.AddressEntity;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    Address map(AddressEntity addressEntity);

    AddressEntity map (Address address);
}
