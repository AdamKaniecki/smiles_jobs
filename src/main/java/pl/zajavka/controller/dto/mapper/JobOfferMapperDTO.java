package pl.zajavka.controller.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.domain.JobOffer;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobOfferMapperDTO {

    JobOffer map(JobOfferDTO jobOfferDTO);

    JobOfferDTO map(JobOffer jobOffer);

    List<JobOffer> mapToListJobOffers(List<JobOfferDTO> jobOffersDTOList);
    List<JobOfferDTO> mapToListJobOffersDTO(List<JobOffer> jobOffersDList);
}
