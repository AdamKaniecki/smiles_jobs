package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.domain.Candidate;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    @Mapping(source = "candidateName", target = "candidateName")
        CandidateDTO mapToCandidateDTO(final Candidate candidate);

//    @Mapping(source = "candidateName", target = "candidateName")
//    Candidate mapToCandidate(final CandidateDTO candidateDTO);

}
