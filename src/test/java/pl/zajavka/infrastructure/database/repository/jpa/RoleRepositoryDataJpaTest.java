package pl.zajavka.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.RoleRepository;

import static org.assertj.core.api.Assertions.assertThat;

@AllArgsConstructor(onConstructor = @__(@Autowired))
class RoleRepositoryDataJpaTest extends AbstractJpaIT {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void thatRoleCanBeFoundByRoleName() {
        // given
        RoleEntity role = RoleEntity.builder().role("ROLE_CANDIDATE").build();
//        roleRepository.save(role);

        // when
        RoleEntity foundRole = roleRepository.findByRole("ROLE_CANDIDATE");

        // then
        assertThat(foundRole).isNotNull();
        assertThat(foundRole.getRole()).isEqualTo(role.getRole());
    }

    @Test
    void thatNonexistentRoleReturnsNull() {
        // when
        RoleEntity foundRole = roleRepository.findByRole("NONEXISTENT_ROLE");

        // then
        assertThat(foundRole).isNull();
    }




    @Test
    void thatEmptyRoleReturnsNull() {
        // when
        RoleEntity foundRole = roleRepository.findByRole("");

        // then
        assertThat(foundRole).isNull();
    }




}