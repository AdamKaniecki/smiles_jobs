package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.database.entity.ProgrammingLanguage;
import pl.zajavka.infrastructure.database.repository.CvRepository;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CvRepositoryDataJpaTest extends AbstractJpaIT {


    private UserRepository userRepository;
    private CvRepository cvRepository;

    @Test
    void testBuilder() {
        // given
        UserEntity user = UserEntity.builder()
                .userName("john_doe")
                .password("password")
                .email("john@example.com")
                .active(true)
                .build();

        AddressEntity address = AddressEntity.builder()
                .country("Poland")
                .city("Warsaw")
                .postalCode("00-001")
                .streetAndNumber("Main Street 123")
                .build();

        Set<ProgrammingLanguage> programmingLanguages = new HashSet<>();
        programmingLanguages.add(ProgrammingLanguage.JAVA);
        programmingLanguages.add(ProgrammingLanguage.PYTHON);

//        Set<IT_Specializations> it_specializations = new HashSet<>();
//        it_specializations.add(IT_Specializations.WEB_DEVELOPMENT);
//        it_specializations.add(IT_Specializations.DATABASE_ADMINISTRATION);

        // when
        CvEntity cvEntity = CvEntity.builder()
                .name("John")
                .surname("Doe")
                .dateOfBirth("1990-01-01")
                .sex("Male")
                .maritalStatus("Single")
                .phoneNumber("123456789")
                .contactEmail("john@example.com")
                .workExperience("5 years")
                .education("Master's Degree")
                .skills("Java, Python")
                .programmingLanguages(programmingLanguages)
                .tools("IntelliJ IDEA, Git")
//                .it_specializations(it_specializations)
                .yearsOfExperience(5)
                .language("English")
                .languageLevel("Fluent")
                .hobby("Reading, Travelling")
                .user(user)
                .address(address)
                .build();

        // then
        assertThat(cvEntity).isNotNull();
        assertThat(cvEntity.getName()).isEqualTo("John");
        assertThat(cvEntity.getSurname()).isEqualTo("Doe");
        assertThat(cvEntity.getDateOfBirth()).isEqualTo("1990-01-01");
        assertThat(cvEntity.getSex()).isEqualTo("Male");
        assertThat(cvEntity.getMaritalStatus()).isEqualTo("Single");
        assertThat(cvEntity.getPhoneNumber()).isEqualTo("123456789");
        assertThat(cvEntity.getContactEmail()).isEqualTo("john@example.com");
        assertThat(cvEntity.getWorkExperience()).isEqualTo("5 years");
        assertThat(cvEntity.getEducation()).isEqualTo("Master's Degree");
        assertThat(cvEntity.getSkills()).isEqualTo("Java, Python");
        assertThat(cvEntity.getProgrammingLanguages()).containsExactlyInAnyOrder(ProgrammingLanguage.JAVA, ProgrammingLanguage.PYTHON);
        assertThat(cvEntity.getTools()).isEqualTo("IntelliJ IDEA, Git");
//        assertThat(cvEntity.getIt_specializations()).containsExactlyInAnyOrder(IT_Specializations.WEB_DEVELOPMENT, IT_Specializations.DATABASE_ADMINISTRATION);
        assertThat(cvEntity.getYearsOfExperience()).isEqualTo(5);
        assertThat(cvEntity.getLanguage()).isEqualTo("English");
        assertThat(cvEntity.getLanguageLevel()).isEqualTo("Fluent");
        assertThat(cvEntity.getHobby()).isEqualTo("Reading, Travelling");
        assertThat(cvEntity.getUser()).isEqualTo(user);
        assertThat(cvEntity.getAddress()).isEqualTo(address);
    }

    @Test
    public void testFindCvByKeywordAndCategory() {
        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        user = userRepository.save(user);

        CvEntity cvEntity = CvFixtures.sampleCvEntity();
        cvEntity.setUser(user);
        cvRepository.save(cvEntity);

        // When
        List<CvEntity> foundCvs = cvRepository.findCvByKeywordAndCategory("John", "name");

        // Then
        assertThat(foundCvs).isNotEmpty();
        assertThat(foundCvs).contains(cvEntity);
    }

    @Test
    public void testExistsByUserId() {
        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        user = userRepository.save(user);

        CvEntity cvEntity = CvFixtures.sampleCvEntity();
        cvEntity.setUser(user);
        cvRepository.save(cvEntity);

        // When
        boolean exists = cvRepository.existsByUserId(user.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    public void testFindByUser() {
        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        user.setId(1);
        user = userRepository.save(user);
        CvEntity cvEntity = CvFixtures.sampleCvEntity();
        cvEntity.setUser(user);
        cvRepository.save(cvEntity);

        // When
        Optional<CvEntity> foundCvOptional = cvRepository.findByUser(user);

        // Then
        assertThat(foundCvOptional).isPresent();
        assertThat(foundCvOptional.get()).isEqualTo(cvEntity);
    }
    @Test
    public void testExistsByUser() {

        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        user.setId(1);
        user = userRepository.save(user);
        CvEntity cvEntity = CvFixtures.sampleCvEntity();
        cvEntity.setUser(user);
        cvRepository.save(cvEntity);

        // When
        boolean exists = cvRepository.existsByUser(user);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    public void testFindById() {

        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        user.setId(1);
        user = userRepository.save(user);
        CvEntity cvEntity = CvFixtures.sampleCvEntity();
        cvEntity.setUser(user);
        cvRepository.save(cvEntity);

        // When
        Optional<CvEntity> foundCvOptional = cvRepository.findById(cvEntity.getId());

        // Then
        assertThat(foundCvOptional).isPresent();
        assertThat(foundCvOptional.get()).isEqualTo(cvEntity);
    }
}
