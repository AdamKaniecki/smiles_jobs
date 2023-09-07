package pl.zajavka.infrastructure.database.repository.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import pl.zajavka.domain.Candidate;
import pl.zajavka.infrastructure.database.entity.CandidateEntity;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-09-07T11:51:04+0200",
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

        candidate.id( entity.getId() );
        candidate.name( entity.getName() );
        candidate.surname( entity.getSurname() );
        candidate.email( entity.getEmail() );
        candidate.phoneNumber( entity.getPhoneNumber() );

        return candidate.build();
    }

    @Override
    public CandidateEntity mapToEntity(Candidate candidate) {
        if ( candidate == null ) {
            return null;
        }

        CandidateEntity.CandidateEntityBuilder candidateEntity = CandidateEntity.builder();

        candidateEntity.id( candidate.getId() );
        candidateEntity.name( candidate.getName() );
        candidateEntity.surname( candidate.getSurname() );
        candidateEntity.email( candidate.getEmail() );
        candidateEntity.phoneNumber( candidate.getPhoneNumber() );

        return candidateEntity.build();
    }

    @Override
    public List<Candidate> mapToDomainList(List<CandidateEntity> candidateEntities) {
        if ( candidateEntities == null ) {
            return null;
        }

        List<Candidate> list = new ArrayList<Candidate>( candidateEntities.size() );
        for ( CandidateEntity candidateEntity : candidateEntities ) {
            list.add( mapFromEntity( candidateEntity ) );
        }

        return list;
    }
}
