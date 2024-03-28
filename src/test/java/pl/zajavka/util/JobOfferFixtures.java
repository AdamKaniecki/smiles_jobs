//package pl.zajavka.util;
//
//import lombok.experimental.UtilityClass;
//import pl.zajavka.controller.dto.JobOfferDTO;
//import pl.zajavka.infrastructure.domain.JobOffer;
//import pl.zajavka.infrastructure.domain.User;
//import pl.zajavka.infrastructure.database.entity.JobOfferEntity;
//import pl.zajavka.infrastructure.security.RoleEntity;
//
//import java.time.OffsetDateTime;
//import java.util.List;
//import java.util.Set;
//
//@UtilityClass
//public class JobOfferFixtures {
//    public static JobOfferEntity someJobOfferEntity1(){
//
//        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
//        return JobOfferEntity.builder()
//                .companyName("company1")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
//                .build();
//    }
//    public static JobOfferEntity someJobOfferEntity2(){
//
//        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
//        return JobOfferEntity.builder()
//                .companyName("company2")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
//                .build();
//    }
//
//    public static JobOfferEntity someJobOfferEntity3(){
//
//        OffsetDateTime jobOfferDateTime = OffsetDateTime.now();
//        return JobOfferEntity.builder()
//                .companyName("company3")
//                .position("junior java developer")
//                .responsibilities("utrzymywanie aplikacji")
//                .requiredTechnologies("spring")
//                .benefits("owocowe czwartki")
//                .jobOfferDateTime(jobOfferDateTime) // Przykładowa data i czas
//                .build();
//    }
//
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
//
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
//
//
//    }
//
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
//}
