package com.zeuscodensa.techcupfutbol.persistence;

import com.zeuscodensa.techcupfutbol.TechcupFutbolApplication;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.service.UserService;
import com.zeuscodensa.techcupfutbol.controller.dto.UserRequestDTO;
import com.zeuscodensa.techcupfutbol.persistence.entity.TournamentEntity;
import com.zeuscodensa.techcupfutbol.persistence.repository.InvitationRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.MatchRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.RegistrationRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.TeamRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.TournamentRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.UserJpaRepository;
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
    private UserJpaRepository userRepository;

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
        com.zeuscodensa.techcupfutbol.core.model.Player request = new com.zeuscodensa.techcupfutbol.core.model.Player();
        request.setName("Maria");
        request.setEmail("maria.perez-a@escuelaing.edu.co");
        request.setPassword("secreto123");
        request.setRole(Role.PLAYER);
        request.setPosition("Volante");
        request.setJerseyNumber(8);
        request.setType(com.zeuscodensa.techcupfutbol.core.model.UserType.EXTERNAL);

        userService.registerUser(request);

        assertTrue(userRepository.findByEmail("maria.perez-a@escuelaing.edu.co").isPresent());
        assertEquals(1, userRepository.count());
    }
}
