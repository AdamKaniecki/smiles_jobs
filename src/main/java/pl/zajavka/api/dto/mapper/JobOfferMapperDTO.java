package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.api.dto.JobOfferDTO;
import pl.zajavka.domain.JobOffer;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobOfferMapperDTO {

    JobOffer map(JobOfferDTO jobOfferDTO);

    JobOfferDTO map(JobOffer jobOffer);
}
