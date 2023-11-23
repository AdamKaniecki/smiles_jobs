package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.api.dto.BusinessCardDTO;
import pl.zajavka.domain.BusinessCard;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BusinessCardMapperDTO {

    BusinessCard map(BusinessCardDTO businessCardDTO);

    BusinessCardDTO map(BusinessCard businessCard);
}
