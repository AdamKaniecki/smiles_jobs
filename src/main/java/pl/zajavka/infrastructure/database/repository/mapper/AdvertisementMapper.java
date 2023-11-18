package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdvertisementMapper {
    @Mapping(target = "name", source = "name")
    Advertisement map(AdvertisementEntity advertisementEntity );
    @Mapping(target = "name", source = "name")
    AdvertisementEntity map(Advertisement advertisement);
}
