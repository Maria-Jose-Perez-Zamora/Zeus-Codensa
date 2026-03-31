package dependencies.persistence.repository;

import dependencies.persistence.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, String> {
    List<RegistrationEntity> findByTournamentName(String tournamentName);
    List<RegistrationEntity> findByTeamName(String teamName);
    boolean existsByTeamNameAndTournamentName(String teamName, String tournamentName);
    boolean existsByTeamNameAndTournamentNameAndStatus(String teamName, String tournamentName, String status);
}
