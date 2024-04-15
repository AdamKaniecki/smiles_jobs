package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import pl.zajavka.controller.dto.JobOfferDTO;
import pl.zajavka.infrastructure.domain.JobOffer;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

@UtilityClass
public class JobOfferFixtures {
    public static JobOfferEntity someJobOfferEntity1(){

        UserEntity user = UserEntity.builder()
                .userName("sample_user")
                .email("sample@example.com")
                .password("password123")
                .active(true)
                .build();

        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
        return JobOfferEntity.builder()
                .id(1)
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
    }
    public static JobOfferEntity someJobOfferEntity2(){
        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
        return JobOfferEntity.builder()
                .companyName("januszex2")
                .position("junior java developer2")
                .responsibilities("support2")
                .requiredTechnologies("spring2")
                .experience("two years2")
                .jobLocation("remote2")
                .typeOfContract("b2b2")
                .typeOfWork("FULL-TIME JOB2")
                .salaryMin(new BigDecimal("4000")) // Przykładowe wynagrodzenie minimalne
                .salaryMax(new BigDecimal("6000")) // Przykładowe wynagrodzenie maksymalne
                .requiredLanguage("English2") // Wymagany język
                .requiredLanguageLevel("B2") // Poziom wymaganego języka
                .benefits("fruits2") // Korzyści oferowane przez pracodawcę
                .jobDescription("example about us2") // Opis stanowiska pracy
                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
                .active(true) // Czy oferta pracy jest aktywna
                .neededStaff(5) // Ilość potrzebnych pracowników
                .hiredCount(0) // Liczba zatrudnionych pracowników
                .build();
    }

    public static JobOffer someJobOffer1(){

        User user = User.builder()
                .userName("sample_user")
                .email("sample@example.com")
                .password("password123")
                .active(true)
                .build();

        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
        return JobOffer.builder()
                .id(1)
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
    }


    public static JobOffer someJobOffer2(){
        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
        return JobOffer.builder()
                .companyName("januszex2")
                .position("junior java developer2")
                .responsibilities("support2")
                .requiredTechnologies("spring2")
                .experience("two years2")
                .jobLocation("remote2")
                .typeOfContract("b2b2")
                .typeOfWork("FULL-TIME JOB2")
                .salaryMin(new BigDecimal("4000")) // Przykładowe wynagrodzenie minimalne
                .salaryMax(new BigDecimal("6000")) // Przykładowe wynagrodzenie maksymalne
                .requiredLanguage("English2") // Wymagany język
                .requiredLanguageLevel("B2") // Poziom wymaganego języka
                .benefits("fruits2") // Korzyści oferowane przez pracodawcę
                .jobDescription("example about us2") // Opis stanowiska pracy
                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
                .active(true) // Czy oferta pracy jest aktywna
                .neededStaff(5) // Ilość potrzebnych pracowników
                .hiredCount(0) // Liczba zatrudnionych pracowników
                .build();
    }




//    public static JobOffer someJobOffer1(){
//        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_CANDIDATE").build();
//        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
//        User user = User.builder()
//                .userName("adam112")
//                .roles(Set.of(candidateRole))
//                .password("adam1112")
//                .email("adam21113@poczta.onet.pl")
//                .active(true)
//                .build();
//
//        return JobOffer.builder()
//                .id(1)
//                .companyName("company1")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime)
//                .user(user)
//                .build();
//    }
//
//    public static JobOffer someJobOffer2(){
//        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_CANDIDATE").build();
//        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
//        User user = User.builder()
//                .userName("adam1312")
//                .roles(Set.of(candidateRole))
//                .password("adam13112")
//                .email("adam2111e3@poczta.onet.pl")
//                .active(true)
//                .build();
//
//        return JobOffer.builder()
//                .id(1)
//                .companyName("company2")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime)
//                .user(user)
//                .build();
//    }

//    public static List<JobOfferDTO> jobOfferDTOs(){
//
//        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_COMPANY").build();
//        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
//        User user = User.builder()
//                .userName("adam112")
//                .roles(Set.of(candidateRole))
//                .password("adam1112")
//                .email("adam21113@poczta.onet.pl")
//                .active(true)
//                .build();
//
//        JobOfferDTO jobOfferDTO1 = JobOfferDTO.builder()
//                .id(1)
//                .companyName("company1")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime)
//                .user(user)
//                .build();
//
//        RoleEntity candidateRole2 = RoleEntity.builder().id(1).role("ROLE_COMPANY").build();
//        OffsetDateTime jobOfferDateTime2 = OffsetDateTime.now();
//        User user2 = User.builder()
//                .userName("adam1122")
//                .roles(Set.of(candidateRole))
//                .password("adam11212")
//                .email("adam211213@poczta.onet.pl")
//                .active(true)
//                .build();
//
//        JobOfferDTO jobOfferDTO2 = JobOfferDTO.builder()
//                .id(1)
//                .companyName("company1")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime2)
//                .user(user2)
//                .build();
//
//        List<JobOfferDTO> outputJobOffers = List.of(jobOfferDTO1,jobOfferDTO2);
//        return  outputJobOffers;


//    }

//    public static List<JobOffer> jobOffers(){
//
//        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_COMPANY").build();
//        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
//        User user1 = User.builder()
//                .id(1)
//                .userName("adam112")
//                .roles(Set.of(candidateRole))
//                .password("adam1112")
//                .email("adam21113@poczta.onet.pl")
//                .active(true)
//                .build();
//
//        JobOffer jobOffer1 = JobOffer.builder()
//                .id(1)
//                .companyName("company1")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime)
//                .user(user1)
//                .build();
//
//        OffsetDateTime jobOfferDateTime2 = OffsetDateTime.now();
//        User user2 = User.builder()
//                .id(2)
//                .userName("adam1122")
//                .roles(Set.of(candidateRole))
//                .password("adam11212")
//                .email("adam211213@poczta.onet.pl")
//                .active(true)
//                .build();
//
//        JobOffer jobOffer2 = JobOffer.builder()
//                .id(2)
//                .companyName("company1")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime2)
//                .user(user2)
//                .build();
//
//        List<JobOffer> outputJobOffers = List.of(jobOffer1, jobOffer2);
//        return  outputJobOffers;
//
//
//    }
//
//    public static JobOfferDTO someJobOfferDTO() {
//
//        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_COMPANY").build();
//        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
//        User user = User.builder()
//                .id(1)
//                .userName("adam112")
//                .roles(Set.of(candidateRole))
//                .password("adam1112")
//                .email("adam21113@poczta.onet.pl")
//                .active(true)
//                .build();
//
//        JobOfferDTO jobOfferDTO = JobOfferDTO.builder()
//                .id(1)
//                .companyName("company1")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime)
//                .user(user)
//                .build();
//
//        return jobOfferDTO;
//
//    }
}
