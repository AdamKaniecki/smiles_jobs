package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdvertisementMapper {

    Advertisement map(AdvertisementEntity advertisementEntity );

    AdvertisementEntity map(Advertisement advertisement);
}
