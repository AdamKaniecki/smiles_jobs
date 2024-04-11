package pl.zajavka.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.database.entity.BusinessCardEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.integration.AbstractJpaIT;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(onConstructor = @__(@Autowired))
public class BusinessCardRepositoryDataJpaTest extends AbstractJpaIT {

    private BusinessCardJpaRepository businessCardJpaRepository;
    private UserRepository userRepository;

    @Test
    void testBuilder() {
        // given

        // when
        BusinessCardEntity businessCard = BusinessCardEntity.builder()
                .office("Office 123")
                .scopeOperations("Scope of operations")
                .recruitmentEmail("recruitment@example.com")
                .phoneNumber("+1234567890")
                .companyDescription("Company description")
                .technologiesAndTools("Technologies and tools")
                .certificatesAndAwards("Certificates and awards")
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

    }

//
//



}
