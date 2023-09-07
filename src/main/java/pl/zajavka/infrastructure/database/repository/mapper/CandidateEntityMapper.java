package pl.zajavka.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.zajavka.domain.Candidate;
import pl.zajavka.infrastructure.database.entity.CandidateEntity;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface CandidateEntityMapper {

//    @Mapping(target = "address", ignore = true)
    Candidate mapFromEntity(CandidateEntity entity);

//    @Mapping(target = "candidateAdvertisement", ignore = true)
    CandidateEntity mapToEntity(Candidate candidate);

    List<Candidate> mapToDomainList(List<CandidateEntity> candidateEntities);
}
