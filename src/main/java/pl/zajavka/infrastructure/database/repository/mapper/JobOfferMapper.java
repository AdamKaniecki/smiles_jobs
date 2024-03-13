package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobOfferMapper {

    JobOffer map(JobOfferEntity jobOfferEntity );

    JobOfferEntity map(JobOffer jobOffer);


    List<JobOffer> map(List<JobOfferEntity> jobOfferEntityList);
}
