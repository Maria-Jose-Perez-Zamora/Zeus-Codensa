package dependencies.persistence.repository;

import dependencies.persistence.entity.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TournamentRepository extends JpaRepository<TournamentEntity, String> {
    Optional<TournamentEntity> findByTournamentName(String tournamentName);
}
