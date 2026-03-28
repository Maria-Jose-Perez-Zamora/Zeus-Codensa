package dependencies.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dependencies.persistence.entity.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    Optional<TeamEntity> findByTeamName(String teamName);
}
