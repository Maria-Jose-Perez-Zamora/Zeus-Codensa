package dependencies.persistence;

import Zeus_Codensa.Techcup_Futbol.TechcupFutbolApplication;
import core.model.Role;
import core.service.UserService;
import dependencies.dto.UserRequestDTO;
import dependencies.persistence.entity.TournamentEntity;
import dependencies.persistence.repository.InvitationRepository;
import dependencies.persistence.repository.MatchRepository;
import dependencies.persistence.repository.RegistrationRepository;
import dependencies.persistence.repository.TeamRepository;
import dependencies.persistence.repository.TournamentRepository;
import dependencies.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = TechcupFutbolApplication.class)
@ActiveProfiles("test")
class PersistenceConnectivityFunctionalTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TournamentRepository tournamentRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @BeforeEach
    void clearRepositories() {
        invitationRepository.deleteAll();
        matchRepository.deleteAll();
        registrationRepository.deleteAll();
        teamRepository.deleteAll();
        tournamentRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldPersistAndReadTournamentThroughRepository() {
        TournamentEntity entity = new TournamentEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setTournamentName("Torneo Persistencia");
        entity.setStatus("OPEN");

        tournamentRepository.save(entity);

        assertTrue(tournamentRepository.findByTournamentName("Torneo Persistencia").isPresent());
    }

    @Test
    void shouldUseRepositoryInjectionInsideUserService() {
        UserRequestDTO request = new UserRequestDTO();
        request.setName("Maria");
        request.setEmail("maria.perez-a@escuelaing.edu.co");
        request.setPassword("secreto123");
        request.setRole(Role.PLAYER);
        request.setPosition("Volante");
        request.setJerseyNumber(8);
        request.setUserType("EXTERNAL");

        userService.registerUser(request);

        assertTrue(userRepository.findByEmail("maria.perez-a@escuelaing.edu.co").isPresent());
        assertEquals(1, userRepository.count());
    }
}
