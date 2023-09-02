package pl.zajavka.api.dto.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.zajavka.api.dto.CandidateDTO;
import pl.zajavka.domain.Candidate;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-02T11:36:59+0200",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class CandidateMapperImpl implements CandidateMapper {

    @Override
    public CandidateDTO map(Candidate candidate) {
        if ( candidate == null ) {
            return null;
        }

        CandidateDTO.CandidateDTOBuilder candidateDTO = CandidateDTO.builder();

        candidateDTO.name( candidate.getName() );
        candidateDTO.surname( candidate.getSurname() );
        candidateDTO.phoneNumber( candidate.getPhoneNumber() );
        candidateDTO.email( candidate.getEmail() );

        return candidateDTO.build();
    }
}
