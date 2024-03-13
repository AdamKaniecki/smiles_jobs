package pl.zajavka.infrastructure.configuration;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.zajavka.infrastructure.business.UserService;
import pl.zajavka.infrastructure.security.RoleRepository;
import pl.zajavka.infrastructure.security.RoleEntity;
import pl.zajavka.infrastructure.security.UserRepository;
import pl.zajavka.infrastructure.security.mapper.UserMapper;

@Slf4j
@Component
@AllArgsConstructor
public class BootstrapApplicationComponent implements ApplicationListener<ContextRefreshedEvent> {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserService userService;
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public void onApplicationEvent(final @NonNull ContextRefreshedEvent event) {
        initializeRoles();
    }

    private void initializeRoles() {
        createRoleIfNotExists("ROLE_CANDIDATE");
        createRoleIfNotExists("ROLE_COMPANY");
        log.info("Roles initialized.");
    }

    private void createRoleIfNotExists(String roleName) {
        RoleEntity existingRole = roleRepository.findByRole(roleName);
        if (existingRole == null) {
            RoleEntity roleEntity = RoleEntity.builder()
                    .role(roleName)
                    .build();
            roleRepository.save(roleEntity);
        }
    }
}
