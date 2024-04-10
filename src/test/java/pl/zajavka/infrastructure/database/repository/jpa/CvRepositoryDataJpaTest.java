package pl.zajavka.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.entity.CvEntity;
import pl.zajavka.infrastructure.security.UserEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.util.AddressFixtures;
import pl.zajavka.util.CvFixtures;
import pl.zajavka.util.UserFixtures;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CvRepositoryDataJpaTest extends AbstractJpaIT {


    private UserRepository userRepository;
    private CvJpaRepository cvJpaRepository;

    @Test
    void testBuilder() {
        // given
        UserEntity user = UserFixtures.someUserEntity1();

        AddressEntity address = AddressFixtures.someAddressEntity1();

        // when
        CvEntity cvEntity = CvEntity.builder()
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
                .languageLevel("Fluent")
                .visible(true)
                .socialMediaProfil("linked")
                .projects("smiles")
                .hobby("Reading, Travelling")
                .user(user)
                .address(address)
                .build();
        cvJpaRepository.save(cvEntity);
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
        assertThat(cvEntity.getSkillsAndTools()).isEqualToIgnoringCase("IntelliJ idea, git");
        assertThat(cvEntity.getProgrammingLanguage()).isEqualTo("Java, Python");
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


        CvEntity cvEntity = CvFixtures.someCvEntity1();
        cvEntity.setUser(user);
        cvJpaRepository.save(cvEntity);


        // When
        List<CvEntity> foundCvs1 = cvJpaRepository.findCvByKeywordAndCategory("JUNIOR", "followPosition");
        List<CvEntity> foundCvs2 = cvJpaRepository.findCvByKeywordAndCategory("english", "language");
        List<CvEntity> foundCvs3 = cvJpaRepository.findCvByKeywordAndCategory("master's Degree", "education");
        List<CvEntity> foundCvs4 = cvJpaRepository.findCvByKeywordAndCategory("java, Python", "programmingLanguage");
        List<CvEntity> foundCvs5 = cvJpaRepository.findCvByKeywordAndCategory("a2", "languageLevel");
        List<CvEntity> foundCvs6 = cvJpaRepository.findCvByKeywordAndCategory("git", "skillsAndTools");
        List<CvEntity> foundCvs7 = cvJpaRepository.findCvByKeywordAndCategory("jira", "skillsAndTools");

        // Then
        assertThat(foundCvs1).isNotEmpty();
        assertThat(foundCvs1).contains(cvEntity);


        assertThat(foundCvs2).isNotEmpty();
        assertThat(foundCvs2).contains(cvEntity);

        assertThat(foundCvs3).isNotEmpty();
        assertThat(foundCvs3).contains(cvEntity);

        assertThat(foundCvs4).isNotEmpty();
        assertThat(foundCvs4).contains(cvEntity);

        assertThat(foundCvs5).isNotEmpty();
        assertThat(foundCvs5).contains(cvEntity);

        assertThat(foundCvs6).isNotEmpty();
        assertThat(foundCvs6).contains(cvEntity);

        assertThat(foundCvs7).isEmpty();
        assertThat(foundCvs7).doesNotContain(cvEntity);
    }

    @Test
    public void testFindCvByKeywordAndCategoryWhenVisibleIsFalse() {
        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        user = userRepository.save(user);


        CvEntity cvEntity = CvFixtures.someCvEntity1();
        cvEntity.setUser(user);
        cvEntity.setVisible(false);
        cvJpaRepository.save(cvEntity);


        // When
        List<CvEntity> foundCvs1 = cvJpaRepository.findCvByKeywordAndCategory("JUNIOR", "followPosition");

        // Then
        assertThat(foundCvs1).isEmpty();
    }
//
    @Test
    public void testExistsByUserId() {
        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        user = userRepository.save(user);

        CvEntity cvEntity = CvFixtures.someCvEntity1();
        cvEntity.setUser(user);
        cvJpaRepository.save(cvEntity);

        // When
        boolean exists = cvJpaRepository.existsByUserId(user.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    public void testFindByUser() {
        // Given
        UserEntity userEntity = UserFixtures.someUserEntity1();
        userEntity = userRepository.save(userEntity);

        CvEntity cvEntity = CvFixtures.someCvEntity1();
        cvEntity.setUser(userEntity);
        cvJpaRepository.save(cvEntity);

        // When
        Optional<CvEntity> foundCvOptional = cvJpaRepository.findByUser(userEntity);

        // Then
        assertThat(foundCvOptional).isPresent();
        assertThat(foundCvOptional.get()).isEqualTo(cvEntity);


    }

    @Test
    public void testExistsByUser() {

        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        user = userRepository.save(user);

        CvEntity cvEntity = CvFixtures.someCvEntity1();
        cvEntity.setUser(user);
        cvJpaRepository.save(cvEntity);

        // When
        boolean exists = cvJpaRepository.existsByUser(user);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    public void testFindById() {

        // Given
        UserEntity user = UserFixtures.someUserEntity1();
        user = userRepository.save(user);
        CvEntity cvEntity = CvFixtures.someCvEntity1();
        cvEntity.setUser(user);
        cvJpaRepository.save(cvEntity);

        // When
        Optional<CvEntity> foundCvOptional = cvJpaRepository.findById(cvEntity.getId());

        // Then
        assertThat(foundCvOptional).isPresent();
        assertThat(foundCvOptional.get()).isEqualTo(cvEntity);
    }
}
