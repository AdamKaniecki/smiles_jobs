package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.domain.Candidate;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    CandidateDTO map(final Candidate candidate);
//    Candidate map(final CandidateDTO candidateDTO);


}
