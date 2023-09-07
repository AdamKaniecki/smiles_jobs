package pl.zajavka.api.dto.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.domain.Candidate;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-07T11:52:05+0200",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class CandidateMapperImpl implements CandidateMapper {

    @Override
    public CandidateDTO mapToCandidateDTO(Candidate candidate) {
        if ( candidate == null ) {
            return null;
        }

        CandidateDTO.CandidateDTOBuilder candidateDTO = CandidateDTO.builder();

        candidateDTO.candidateName( candidate.getName() );
        if ( candidate.getId() != null ) {
            candidateDTO.id( candidate.getId().longValue() );
        }
        candidateDTO.surname( candidate.getSurname() );
        candidateDTO.phoneNumber( candidate.getPhoneNumber() );
        candidateDTO.email( candidate.getEmail() );

        return candidateDTO.build();
    }

    @Override
    public Candidate mapToDomain(CandidateDTO candidateDTO) {
        if ( candidateDTO == null ) {
            return null;
        }

        Candidate.CandidateBuilder candidate = Candidate.builder();

        candidate.name( candidateDTO.getCandidateName() );
        if ( candidateDTO.getId() != null ) {
            candidate.id( candidateDTO.getId().intValue() );
        }
        candidate.surname( candidateDTO.getSurname() );
        candidate.email( candidateDTO.getEmail() );
        candidate.phoneNumber( candidateDTO.getPhoneNumber() );

        return candidate.build();
    }
}
