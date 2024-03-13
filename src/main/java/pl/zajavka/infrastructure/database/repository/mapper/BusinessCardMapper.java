package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.infrastructure.domain.BusinessCard;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BusinessCardMapper {
    BusinessCard map (BusinessCardEntity businessCardEntity);

    BusinessCardEntity map (BusinessCard businessCard);
}
