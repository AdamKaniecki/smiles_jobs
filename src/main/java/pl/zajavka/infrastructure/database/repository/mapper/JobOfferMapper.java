package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Advertisement;
import pl.zajavka.domain.JobOffer;
import pl.zajavka.infrastructure.database.entity.AdvertisementEntity;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobOfferMapper {

    JobOffer map(JobOfferEntity jobOfferEntity );

    JobOfferEntity map(JobOffer jobOffer);

//    default BigDecimal map(String value) {
//        return value != null ? new BigDecimal(value) : null;
//    }
//
//    default String map(BigDecimal value) {
//        return value != null ? value.toString() : null;
//    }
}