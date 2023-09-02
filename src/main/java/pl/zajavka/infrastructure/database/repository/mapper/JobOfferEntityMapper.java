package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.CandidateAdvertisement;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.infrastructure.database.entity.CandidateAdvertisementEntity;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface JobOfferEntityMapper {

    JobOffer mapFromEntity (JobOfferEntity entity);

}
