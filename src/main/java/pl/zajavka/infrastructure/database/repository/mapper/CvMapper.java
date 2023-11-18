package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.CV;
import pl.zajavka.infrastructure.database.entity.CvEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CvMapper {

    CV map(CvEntity cvEntity);

    CvEntity map(CV cv);

}
