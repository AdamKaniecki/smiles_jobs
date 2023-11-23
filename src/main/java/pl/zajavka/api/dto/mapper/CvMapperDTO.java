package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.api.dto.CvDTO;
import pl.zajavka.domain.CV;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CvMapperDTO {

    CV map(CvDTO cvDTO);

    CvDTO map(CV cv);
}
