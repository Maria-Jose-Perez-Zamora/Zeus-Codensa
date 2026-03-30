package dependencies.persistence.repository;

import dependencies.persistence.entity.RegistrationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<RegistrationEntity, String> {
    List<RegistrationEntity> findByTournamentName(String tournamentName);
    List<RegistrationEntity> findByTeamName(String teamName);
}
