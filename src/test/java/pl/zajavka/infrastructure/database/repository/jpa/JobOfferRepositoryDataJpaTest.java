package pl.zajavka.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.integration.AbstractJpaIT;
import pl.zajavka.util.UserFixtures;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.zajavka.util.JobOfferFixtures.*;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JobOfferRepositoryDataJpaTest extends AbstractJpaIT {

    @InjectMocks
    private JobOfferJpaRepository jobOfferJpaRepository;

    private UserRepository userRepository;

    @Test
    public void testSaveJobOffer() {
        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        userRepository.save(user);
        JobOfferEntity jobOfferEntity = someJobOfferEntity1();
        jobOfferEntity.setUser(user);

        // When
        JobOfferEntity savedJobOfferEntity = jobOfferJpaRepository.save(jobOfferEntity);

        // Then
        assertThat(savedJobOfferEntity.getId()).isNotNull();
    }


    @Test
    public void testFindJobOfferById() {
        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        userRepository.save(user);
        JobOfferEntity jobOfferEntity = someJobOfferEntity1();
        jobOfferEntity.setUser(user);

        // When
        JobOfferEntity savedJobOfferEntity = jobOfferJpaRepository.save(jobOfferEntity);
        Integer savedJobOfferId = savedJobOfferEntity.getId(); // Pobieramy identyfikator zapisanej oferty pracy

        // Then
        Optional<JobOfferEntity> foundJobOfferEntityOptional = jobOfferJpaRepository.findById(savedJobOfferId);

        // Sprawdzamy, czy Optional zawiera wartość
        assertThat(foundJobOfferEntityOptional).isPresent();

        // Jeśli Optional zawiera wartość, pobieramy encję
        JobOfferEntity foundJobOfferEntity = foundJobOfferEntityOptional.get();

        // Sprawdzamy, czy znaleziona oferta pracy ma ten sam identyfikator co zapisana oferta pracy
        assertThat(foundJobOfferEntity.getId()).isEqualTo(savedJobOfferId);
    }
    @Test
    public void testDeleteJobOffer() {
        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        userRepository.save(user);
        JobOfferEntity jobOfferEntity = someJobOfferEntity1();
        jobOfferEntity.setUser(user);
        jobOfferJpaRepository.save(jobOfferEntity);

        // When
        jobOfferJpaRepository.delete(jobOfferEntity);

        // Then
        assertThat(jobOfferJpaRepository.findById(jobOfferEntity.getId())).isEmpty();
    }

    @Test
    void testFindAllJobOffersForUser() {

//        given
        UserEntity user = UserFixtures.someUserEntity1();
        userRepository.save(user);
        var jobOffers = List.of(someJobOfferEntity1(), someJobOfferEntity2());
        jobOffers.forEach(jobOffer -> jobOffer.setUser(user));
        jobOfferJpaRepository.saveAll(jobOffers);

//        when
        List<JobOfferEntity> jobOffersFound = jobOfferJpaRepository.findListByUser(user);

//        then
        assertThat(jobOffersFound.size()).isEqualTo(2);
    }

//    @Test
//    public void testFindActiveJobOffersByKeywordAndCategory() {
//        // Given
//        UserEntity user = UserFixtures.someUserEntity2();
//        userRepository.save(user);
//
//        JobOfferEntity jobOfferEntity1 = someJobOfferEntity1();
//        JobOfferEntity jobOfferEntity2 = someJobOfferEntity2();
//        jobOfferEntity1.setUser(user);
//        jobOfferEntity2.setUser(user);
//        jobOfferJpaRepository.save(jobOfferEntity1);
//        jobOfferJpaRepository.save(jobOfferEntity2);
//
//        // When
//        List<JobOfferEntity> foundJobOffers1 = jobOfferJpaRepository.findActiveJobOffersByKeywordAndCategory("januszex", "companyName");
//        List<JobOfferEntity> foundJobOffers2 = jobOfferJpaRepository.findActiveJobOffersByKeywordAndCategory("junior java developer", "position");
//        List<JobOfferEntity> foundJobOffers3 = jobOfferJpaRepository.findActiveJobOffersByKeywordAndCategory("spring", "requiredTechnologies");
//
//        // Then
////        assertThat(foundJobOffers1).isEmpty(); // Sprawdź, czy brak ofert dla nieistniejącego słowa kluczowego
//        assertThat(foundJobOffers2).containsExactlyInAnyOrder(jobOfferEntity1); // Sprawdź, czy znaleziono poprawną ofertę dla pozycji "junior java developer"
//        assertThat(foundJobOffers3).containsExactlyInAnyOrder(jobOfferEntity1, jobOfferEntity2); // Sprawdź, czy znaleziono poprawne oferty dla technologii "spring"
//    }



    @Test
    public void testFindJobOffersByKeywordAndCategoryWhenActiveJobOfferIsFalse() {
        // Given
        UserEntity user = UserFixtures.someUserEntity2();
        userRepository.save(user);
        JobOfferEntity jobOfferEntity1 = someJobOfferEntity1();
        JobOfferEntity jobOfferEntity2 = someJobOfferEntity2();
        jobOfferEntity1.setUser(user);
        jobOfferEntity1.setActive(false);
        jobOfferEntity2.setUser(user);
        jobOfferEntity2.setActive(false);
        jobOfferJpaRepository.save(jobOfferEntity1);
        jobOfferJpaRepository.save(jobOfferEntity2);

        // When
        List<JobOfferEntity> foundJobOffers1 = jobOfferJpaRepository.findActiveJobOffersByKeywordAndCategory("januszex", "companyName");
        List<JobOfferEntity> foundJobOffers2 = jobOfferJpaRepository.findActiveJobOffersByKeywordAndCategory("java developer", "position");
        List<JobOfferEntity> foundJobOffers3 = jobOfferJpaRepository.findActiveJobOffersByKeywordAndCategory("spring", "requiredTechnologies");

        // Then

        assertThat(foundJobOffers1).isEmpty();

        assertThat(foundJobOffers2).isEmpty();

        assertThat(foundJobOffers3).isEmpty();



    }




    @Test
    void testJobOfferBuilder() {
//      given
        UserEntity user = UserFixtures.someUserEntity2();
        userRepository.save(user);
        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();

//        when
      JobOfferEntity jobOfferEntity =  JobOfferEntity.builder()
                .companyName("januszex")
                .position("junior java developer")
                .responsibilities("support")
                .requiredTechnologies("spring")
                .experience("two years")
                .jobLocation("remote")
                .typeOfContract("b2b")
                .typeOfWork("FULL-TIME JOB")
                .salaryMin(new BigDecimal("4000")) // Przykładowe wynagrodzenie minimalne
                .salaryMax(new BigDecimal("6000")) // Przykładowe wynagrodzenie maksymalne
                .requiredLanguage("English") // Wymagany język
                .requiredLanguageLevel("B2") // Poziom wymaganego języka
                .benefits("fruits") // Korzyści oferowane przez pracodawcę
                .jobDescription("example about us") // Opis stanowiska pracy
                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
                .active(true) // Czy oferta pracy jest aktywna
                .neededStaff(5) // Ilość potrzebnych pracowników
                .hiredCount(0) // Liczba zatrudnionych pracowników
                .user(user)
                .build();
        // then
        assertThat(jobOfferEntity).isNotNull();
        assertThat(jobOfferEntity.getCompanyName()).isEqualTo("januszex");
        assertThat(jobOfferEntity.getPosition()).isEqualTo("junior java developer");
        assertThat(jobOfferEntity.getResponsibilities()).isEqualTo("support");
        assertThat(jobOfferEntity.getRequiredTechnologies()).isEqualTo("spring");
        assertThat(jobOfferEntity.getExperience()).isEqualTo("two years");
        assertThat(jobOfferEntity.getJobLocation()).isEqualTo("remote");
        assertThat(jobOfferEntity.getTypeOfContract()).isEqualTo("b2b");
        assertThat(jobOfferEntity.getTypeOfWork()).isEqualTo("FULL-TIME JOB");
        assertThat(jobOfferEntity.getSalaryMin()).isEqualByComparingTo(new BigDecimal("4000"));
        assertThat(jobOfferEntity.getSalaryMax()).isEqualByComparingTo(new BigDecimal("6000"));
        assertThat(jobOfferEntity.getRequiredLanguage()).isEqualTo("English");
        assertThat(jobOfferEntity.getRequiredLanguageLevel()).isEqualTo("B2");
        assertThat(jobOfferEntity.getBenefits()).isEqualTo("fruits");
        assertThat(jobOfferEntity.getJobDescription()).isEqualTo("example about us");
        assertThat(jobOfferEntity.getJobOfferDateTime()).isEqualTo(jobOfferDateTime);
        assertThat(jobOfferEntity.getActive()).isTrue();
        assertThat(jobOfferEntity.getNeededStaff()).isEqualTo(5);
        assertThat(jobOfferEntity.getHiredCount()).isEqualTo(0);
        assertThat(jobOfferEntity.getUser()).isEqualTo(user);    }


    }

