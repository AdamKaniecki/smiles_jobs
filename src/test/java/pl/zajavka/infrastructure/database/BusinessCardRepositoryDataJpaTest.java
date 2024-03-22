package pl.zajavka.infrastructure.database;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.database.entity.AddressEntity;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.database.repository.jpa.BusinessCardJpaRepository;
import pl.zajavka.infrastructure.security.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessCardRepositoryDataJpaTest extends AbstractJpaIT{

    private BusinessCardJpaRepository businessCardJpaRepository;

    @Test
    void testBuilder() {
        // given
        UserEntity user = UserEntity.builder()
                .userName("john_doe")
                .password("password")
                .email("john@example.com")
                .active(true)
                .build();

        // when
        BusinessCardEntity businessCard = BusinessCardEntity.builder()
                .office("Office 123")
                .scopeOperations("Scope of operations")
                .recruitmentEmail("recruitment@example.com")
                .phoneNumber("+1234567890")
                .companyDescription("Company description")
                .technologiesAndTools("Technologies and tools")
                .certificatesAndAwards("Certificates and awards")
                .user(user)
                .build();

        // then
        assertThat(businessCard).isNotNull();
        assertThat(businessCard.getOffice()).isEqualTo("Office 123");
        assertThat(businessCard.getScopeOperations()).isEqualTo("Scope of operations");
        assertThat(businessCard.getRecruitmentEmail()).isEqualTo("recruitment@example.com");
        assertThat(businessCard.getPhoneNumber()).isEqualTo("+1234567890");
        assertThat(businessCard.getCompanyDescription()).isEqualTo("Company description");
        assertThat(businessCard.getTechnologiesAndTools()).isEqualTo("Technologies and tools");
        assertThat(businessCard.getCertificatesAndAwards()).isEqualTo("Certificates and awards");
        assertThat(businessCard.getUser()).isEqualTo(user);
    }

    @Test
    void testBuilderWithAddress() {
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

        // when
        BusinessCardEntity businessCard = BusinessCardEntity.builder()
                .office("Office 123")
                .scopeOperations("Scope of operations")
                .recruitmentEmail("recruitment@example.com")
                .phoneNumber("+1234567890")
                .companyDescription("Company description")
                .technologiesAndTools("Technologies and tools")
                .certificatesAndAwards("Certificates and awards")
                .user(user)
                .address(address)
                .build();

        // then
        assertThat(businessCard).isNotNull();
        assertThat(businessCard.getAddress()).isEqualTo(address);
    }

}
