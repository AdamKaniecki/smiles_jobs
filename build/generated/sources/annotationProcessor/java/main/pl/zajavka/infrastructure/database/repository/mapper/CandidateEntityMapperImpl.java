package pl.zajavka.infrastructure.database.repository.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.zajavka.domain.Candidate;
import pl.zajavka.infrastructure.database.entity.CandidateEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-08-29T14:33:33+0200",
    comments = "version: 1.5.3.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-7.4.jar, environment: Java 17.0.6 (Amazon.com Inc.)"
)
@Component
public class CandidateEntityMapperImpl implements CandidateEntityMapper {

    @Override
    public Candidate mapFromEntity(CandidateEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Candidate.CandidateBuilder candidate = Candidate.builder();

        candidate.candidateId( entity.getCandidateId() );
        candidate.name( entity.getName() );
        candidate.surname( entity.getSurname() );
        candidate.email( entity.getEmail() );
        candidate.phoneNumber( entity.getPhoneNumber() );
        candidate.availabilityStatus( entity.getAvailabilityStatus() );

        return candidate.build();
    }
}
