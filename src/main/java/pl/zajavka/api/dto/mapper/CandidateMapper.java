package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.domain.Candidate;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    Candidate map(final CandidateDTO candidateDTO);
    CandidateDTO mapToDTO(final Candidate candidate);
}
