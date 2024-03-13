package pl.zajavka.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.infrastructure.domain.JobOffer;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobOfferMapperDTO {

    JobOffer map(JobOfferDTO jobOfferDTO);

    JobOfferDTO map(JobOffer jobOffer);
}
