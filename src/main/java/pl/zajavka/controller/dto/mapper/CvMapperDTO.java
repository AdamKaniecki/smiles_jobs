package pl.zajavka.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.infrastructure.domain.CV;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CvMapperDTO {

    CV map(CvDTO cvDTO);

    CvDTO map(CV cv);
}
