package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.domain.User;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.database.repository.JobOfferRepository;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.util.JobOfferFixtures;
import pl.zajavka.util.UserFixtures;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.zajavka.util.JobOfferFixtures.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JobOfferRepositoryDataJpaTest extends AbstractJpaIT {

    private JobOfferRepository jobOfferRepository;
    private UserRepository userRepository;

    @Test
    public void testSaveJobOffer() {
        // Given
        UserEntity user = UserFixtures.someUser1();
        userRepository.save(user);
        JobOfferEntity jobOfferEntity = someJobOffer1();
        jobOfferEntity.setUser(user);

        // When
        JobOfferEntity savedJobOfferEntity = jobOfferRepository.save(jobOfferEntity);

        // Then
        assertThat(savedJobOfferEntity.getId()).isNotNull();
    }


    @Test
    public void testFindJobOfferById() {
        // Given
        UserEntity user = UserFixtures.someUser1();
        userRepository.save(user);
        JobOfferEntity jobOfferEntity = someJobOffer1();
        jobOfferEntity.setUser(user);
        jobOfferRepository.save(jobOfferEntity);

        // When
        Optional<JobOfferEntity> foundJobOfferEntityOptional = jobOfferRepository.findById(jobOfferEntity.getId());

        // Then
        assertThat(foundJobOfferEntityOptional).isPresent();
        JobOfferEntity foundJobOfferEntity = foundJobOfferEntityOptional.get();
        assertThat(foundJobOfferEntity.getId()).isEqualTo(jobOfferEntity.getId());


    }

    @Test
    public void testDeleteJobOffer() {
        // Given
        UserEntity user = UserFixtures.someUser1();
        userRepository.save(user);
        JobOfferEntity jobOfferEntity = someJobOffer1();
        jobOfferEntity.setUser(user);
        jobOfferRepository.save(jobOfferEntity);

        // When
        jobOfferRepository.delete(jobOfferEntity);

        // Then
        assertThat(jobOfferRepository.findById(jobOfferEntity.getId())).isEmpty();
    }

    @Test
    void testFindAllJobOffers() {

//        given
        UserEntity user = UserFixtures.someUser1();
        userRepository.save(user);
        var jobOffers = List.of(someJobOffer1(), someJobOffer2(), someJobOffer3());
        jobOffers.forEach(jobOffer -> jobOffer.setUser(user));
        jobOfferRepository.saveAll(jobOffers);

//        when
        List<JobOfferEntity> jobOffersFound = jobOfferRepository.findAll();

//        then
        assertThat(jobOffersFound.size()).isEqualTo(3);
    }

    @Test
    void testJobOfferBuilder() {
//      given
        UserEntity user = UserFixtures.someUser1();
        userRepository.save(user);
        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();

//        when
        JobOfferEntity jobOfferEntity = JobOfferEntity.builder()
                .id(1)
                .companyName("company1")
                .position("junior java developer")
                .responsibilities("utrzymywanie aplikacji")
                .requiredTechnologies("spring")
                .benefits("owocowe czwartki")
                .jobOfferDateTime(jobOfferDateTime)
                .user(user)
                .build();

//      then
        assertThat(jobOfferEntity).isNotNull();
        assertThat(jobOfferEntity.getCompanyName()).isEqualTo("company1");
        assertThat(jobOfferEntity.getPosition()).isEqualTo("junior java developer");
        assertThat(jobOfferEntity.getResponsibilities()).isEqualTo("utrzymywanie aplikacji");
        assertThat(jobOfferEntity.getRequiredTechnologies()).isEqualTo("spring");
        assertThat(jobOfferEntity.getBenefits()).isEqualTo("owocowe czwartki");
        assertThat(jobOfferEntity.getJobOfferDateTime()).isEqualTo(jobOfferDateTime);
    }


    }

