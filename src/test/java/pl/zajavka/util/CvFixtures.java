package pl.zajavka.util;

import lombok.experimental.UtilityClass;
import pl.zajavka.controller.dto.CvDTO;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.domain.Address;
import pl.zajavka.infrastructure.domain.CV;
import pl.zajavka.infrastructure.domain.User;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.UserEntity;

import java.util.Set;

@UtilityClass
public class CvFixtures {


    public static CvEntity someCvEntity1() {

        AddressEntity address = AddressEntity.builder()
                .city("Sample City")
                .country("Sample Country")
                .streetAndNumber("Sample Street 125")
                .postalCode("09-500")
                .build();

        return CvEntity.builder()
                .name("John")
                .surname("Doe")
                .dateOfBirth("1990-01-01")
                .sex("Male")
                .maritalStatus("Single")
                .phoneNumber("123456789")
                .contactEmail("john@example.com")
                .aboutMe("coś tam")
                .followPosition("junior")
                .workExperience("5 years")
                .education("Master's Degree")
                .skillsAndTools("IntelliJ IDEA, Git")
                .programmingLanguage("Java, Python")
                .certificatesOfCourses("zajavka")
                .language("English")
                .languageLevel("A2")
                .visible(true)
                .socialMediaProfil("linked")
                .projects("smiles")
                .hobby("Reading, Travelling")
                .address(address)
                .build();


    }

    public static CvEntity someCvEntity2() {

        AddressEntity address = AddressEntity.builder()
                .city("Sample City2")
                .country("Sample Country2")
                .streetAndNumber("Sample Street2 125")
                .postalCode("09-500")
                .build();

        UserEntity user = UserEntity.builder()
                .userName("sample_user")
                .email("sample@example.com")
                .password("password123")
                .active(true)
                .build();


       return CvEntity.builder()
                .name("John2")
                .surname("Doe2")
                .dateOfBirth("1990-01-12")
                .sex("Male2")
                .maritalStatus("Single2")
                .phoneNumber("1234567892")
                .contactEmail("john@example.com2")
                .aboutMe("coś tam2")
                .followPosition("junior2")
                .workExperience("5 years2")
                .education("Master's Degree2")
                .skillsAndTools("IntelliJ IDEA, Git2")
                .programmingLanguage("Java, Python2")
                .certificatesOfCourses("zajavka2")
                .language("English2")
                .languageLevel("A2")
                .visible(true)
                .socialMediaProfil("linked2")
                .projects("smiles2")
                .hobby("Reading, Travelling2")
                .address(address)
                .user(user)
                .build();



    }

    public static CV someCv1() {

        Address address = Address.builder()
                .city("Sample City")
                .country("Sample Country")
                .streetAndNumber("Sample Street 125")
                .postalCode("09-500")
                .build();

        User user = User.builder()
                .userName("sample_user")
                .email("sample@example.com")
                .password("password123")
                .active(true)
                .build();

        return CV.builder()
                .name("John")
                .surname("Doe")
                .dateOfBirth("1990-01-01")
                .sex("Male")
                .maritalStatus("Single")
                .phoneNumber("123456789")
                .contactEmail("john@example.com")
                .workExperience("5 years")
                .education("Master's Degree")
                .socialMediaProfil("linked")
                .projects("smiles")
                .aboutMe("coś tam")
                .certificatesOfCourses("zajavka")
                .programmingLanguage("Java, Python")
                .skillsAndTools("IntelliJ IDEA, Git")
                .language("English")
                .languageLevel("A2")
                .hobby("Reading, Travelling")
                .followPosition("junior")
                .visible(true)
                .user(user)
                .address(address)
                .build();

    }

    public static CV someCv2() {

        Address address = Address.builder()
                .city("Sample City2")
                .country("Sample Country2")
                .streetAndNumber("Sample Street2 125")
                .postalCode("09-500")
                .build();

        User user = User.builder()
                .userName("sample_user2")
                .email("sample@example.com2")
                .password("password123")
                .active(true)
                .build();


        return CV.builder()
                .name("John2")
                .surname("Doe2")
                .dateOfBirth("1990-01-12")
                .sex("Male2")
                .maritalStatus("Single2")
                .phoneNumber("1234567892")
                .contactEmail("john@example.com2")
                .aboutMe("coś tam2")
                .followPosition("junior2")
                .workExperience("5 years2")
                .education("Master's Degree2")
                .skillsAndTools("IntelliJ IDEA, Git2")
                .programmingLanguage("Java, Python2")
                .certificatesOfCourses("zajavka2")
                .language("English2")
                .languageLevel("A2")
                .visible(true)
                .socialMediaProfil("linked2")
                .projects("smiles2")
                .hobby("Reading, Travelling2")
                .address(address)
                .user(user)
                .build();



    }

    public static CV someCv1forNotification() {
        RoleEntity candidateRole = RoleEntity.builder().id(1).role("ROLE_CANDIDATE").build();
        Address address = Address.builder()
                .city("Sample City")
                .country("Sample Country")
                .streetAndNumber("Sample Street 125")
                .postalCode("09-500")
                .build();

        User user = User.builder()
                .userName("sample_user")
                .email("sample@example.com")
                .password("password123")
                .active(true)
                .roles(Set.of(candidateRole))
                .build();

        return CV.builder()
                .name("John")
                .surname("Doe")
                .dateOfBirth("1990-01-01")
                .sex("Male")
                .maritalStatus("Single")
                .phoneNumber("123456789")
                .contactEmail("john@example.com")
                .workExperience("5 years")
                .education("Master's Degree")
                .socialMediaProfil("linked")
                .projects("smiles")
                .aboutMe("coś tam")
                .certificatesOfCourses("zajavka")
                .programmingLanguage("Java, Python")
                .skillsAndTools("IntelliJ IDEA, Git")
                .language("English")
                .languageLevel("A2")
                .hobby("Reading, Travelling")
                .followPosition("junior")
                .visible(true)
                .user(user)
                .address(address)
                .build();

    }

    public static CvDTO someCvDTO() {

        Address address = Address.builder()
                .city("Sample City")
                .country("Sample Country")
                .streetAndNumber("Sample Street 125")
                .postalCode("09-500")
                .build();

        User user = User.builder()
                .userName("sample_user")
                .email("sample@example.com")
                .password("password123")
                .active(true)
                .build();

        return CvDTO.builder()
                .name("John")
                .surname("Doe")
                .dateOfBirth("1990-01-01")
                .sex("Male")
                .maritalStatus("Single")
                .phoneNumber("123456789")
                .contactEmail("john@example.com")
                .workExperience("5 years")
                .education("Master's Degree")
                .socialMediaProfil("linked")
                .projects("smiles")
                .aboutMe("coś tam")
                .certificatesOfCourses("zajavka")
                .programmingLanguage("Java, Python")
                .skillsAndTools("IntelliJ IDEA, Git")
                .language("English")
                .languageLevel("A2")
                .hobby("Reading, Travelling")
                .followPosition("junior")
                .visible(true)
                .user(user)
                .address(address)
                .build();

    }


}

