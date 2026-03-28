package dependencies.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dependencies.persistence.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
