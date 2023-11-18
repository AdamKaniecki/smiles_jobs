package pl.zajavka.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

   UserEntity findByUserName(String userName);


    boolean existsByEmail(String email);
}
