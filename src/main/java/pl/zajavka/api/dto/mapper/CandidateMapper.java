package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.domain.Candidate;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    @Mapping(source = "name", target = "candidateName")
        CandidateDTO mapToCandidateDTO(final Candidate candidate);

//    @Mapping(source = "candidateName", ignore = true)
//    CandidateDTO mapToDto(Candidate candidate);

//   List<CandidateDTO> map(List<Candidate> candidates);

    @Mapping(source = "candidateName", target = "name")
    Candidate mapToDomain(CandidateDTO candidateDTO);

//    @Mapping(source = "candidateName", target = "candidateName")
//    Candidate mapToCandidate(final CandidateDTO candidateDTO);

}
