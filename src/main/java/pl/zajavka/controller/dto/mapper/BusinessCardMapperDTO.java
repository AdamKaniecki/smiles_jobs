package pl.zajavka.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.controller.dto.BusinessCardDTO;
import pl.zajavka.infrastructure.domain.BusinessCard;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BusinessCardMapperDTO {

    BusinessCard map(BusinessCardDTO businessCardDTO);

    BusinessCardDTO map(BusinessCard businessCard);
}
