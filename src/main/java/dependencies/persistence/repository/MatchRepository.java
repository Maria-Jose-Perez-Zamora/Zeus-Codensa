package dependencies.persistence.repository;

import dependencies.persistence.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, String> {
    List<MatchEntity> findByTournamentName(String tournamentName);
    List<MatchEntity> findByRefereeEmail(String refereeEmail);
}
