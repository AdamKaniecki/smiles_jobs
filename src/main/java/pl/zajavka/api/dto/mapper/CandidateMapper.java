package pl.zajavka.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.domain.Candidate;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CandidateMapper {
    @Mapping(source = "name", target = "candidateName")
    @Mapping(source = "surname", target = "candidateSurname")
    @Mapping(source = "phoneNumber", target = "candidatePhoneNumber")
    @Mapping(source = "email", target = "candidateEmail")
        CandidateDTO mapToCandidateDTO(final Candidate candidate);

//    @Mapping(source = "candidateName", ignore = true)
//    CandidateDTO mapToDto(Candidate candidate);

//   List<CandidateDTO> map(List<Candidate> candidates);

    @Mapping(source = "candidateName", target = "name")
    @Mapping(source = "candidateSurname", target = "surname")
    @Mapping(source = "candidatePhoneNumber", target = "phoneNumber")
    @Mapping(source = "candidateEmail", target = "email")
    Candidate mapToDomain(CandidateDTO candidateDTO);

//    @Mapping(source = "candidateName", target = "candidateName")
//    Candidate mapToCandidate(final CandidateDTO candidateDTO);

}
