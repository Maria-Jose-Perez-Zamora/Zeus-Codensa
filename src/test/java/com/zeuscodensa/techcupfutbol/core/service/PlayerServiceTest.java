package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Invitation;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.repository.IInvitationRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITeamRepository;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ITeamRepository teamRepository;

    @Mock
    private IInvitationRepository invitationRepository;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testProcessInvitation_Accept_Success() {
        Invitation inv = new Invitation();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        inv.setTeamName("Dream Team");
        inv.setStatus("PENDING");

        Team team = new Team("Dream Team");
        team.setPlayers(new ArrayList<>());

        Player player = new Player();
        player.setEmail("player@test.com");

        when(invitationRepository.findById("inv-123")).thenReturn(Optional.of(inv));
        when(teamRepository.findByTeamName("Dream Team")).thenReturn(Optional.of(team));
        when(userRepository.findByEmail("player@test.com")).thenReturn(Optional.of(player));

        playerService.processInvitation("inv-123", "player@test.com", "ACEPTADA");

        assertEquals("ACEPTADA", inv.getStatus());
        assertEquals(1, team.getPlayers().size());
        verify(teamRepository, times(1)).save(team);
        verify(invitationRepository, times(1)).save(inv);
    }

    @Test
    public void testProcessInvitation_Decline_Success() {
        Invitation inv = new Invitation();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        inv.setStatus("PENDING");

        when(invitationRepository.findById("inv-123")).thenReturn(Optional.of(inv));

        playerService.processInvitation("inv-123", "player@test.com", "DECLINADA");

        assertEquals("DECLINADA", inv.getStatus());
        verify(invitationRepository, times(1)).save(inv);
        verify(teamRepository, never()).save(any());
    }

    @Test
    public void testProcessInvitation_NotFound() {
        when(invitationRepository.findById("fake-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            playerService.processInvitation("fake-id", "player@test.com", "ACEPTADA"));
    }

    @Test
    public void testProcessInvitation_AlreadyInTeam() {
        Invitation inv = new Invitation();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        inv.setStatus("PENDING");

        Team otherTeam = new Team("Other Team");
        Player player = new Player();
        player.setEmail("player@test.com");
        ArrayList<User> players = new ArrayList<>();
        players.add(player);
        otherTeam.setPlayers(players);
        
        ArrayList<Team> allTeams = new ArrayList<>();
        allTeams.add(otherTeam);

        when(invitationRepository.findById("inv-123")).thenReturn(Optional.of(inv));
        when(teamRepository.findAll()).thenReturn(allTeams);

        assertThrows(BusinessRuleException.class, () -> 
            playerService.processInvitation("inv-123", "player@test.com", "ACEPTADA"));
    }

    @Test
    public void testProcessInvitation_WrongRecipient() {
        Invitation inv = new Invitation();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        when(invitationRepository.findById("inv-123")).thenReturn(Optional.of(inv));

        assertThrows(BusinessRuleException.class, () ->
            playerService.processInvitation("inv-123", "wrong@test.com", "ACEPTADA"));
    }

    @Test
    public void testProcessInvitation_AlreadyAnswered() {
        Invitation inv = new Invitation();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        inv.setStatus("ACEPTADA");
        when(invitationRepository.findById("inv-123")).thenReturn(Optional.of(inv));

        assertThrows(BusinessRuleException.class, () ->
            playerService.processInvitation("inv-123", "player@test.com", "ACEPTADA"));
    }

    @Test
    public void testProcessInvitation_InvalidStatus() {
        Invitation inv = new Invitation();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        inv.setStatus("PENDING");
        when(invitationRepository.findById("inv-123")).thenReturn(Optional.of(inv));

        assertThrows(BusinessRuleException.class, () ->
            playerService.processInvitation("inv-123", "player@test.com", "INVALID_STATUS"));
    }

    @Test
    public void testBuscarJugadoresDisponibles_AllFilters() {
        Player u1 = new Player();
        u1.setEmail("p1@test.com");
        u1.setName("Messi");
        u1.setRole(Role.PLAYER);

        Player u2 = new Player();
        u2.setEmail("p2@test.com");
        u2.setName("Ronaldo");
        u2.setRole(Role.PLAYER);

        Player u3 = new Player(); // Not a player
        u3.setEmail("p3@test.com");
        u3.setRole(Role.ADMINISTRADOR_SISTEMA);

        Team team = new Team("A");
        team.setPlayers(new ArrayList<>());
        team.getPlayers().add(u2); // Ronaldo already in team

        when(userRepository.findAll()).thenReturn(java.util.List.of(u1, u2, u3));
        when(teamRepository.findAll()).thenReturn(java.util.List.of(team));

        java.util.List<User> res = playerService.buscarJugadoresDisponibles("Messi", null);
        
        assertEquals(1, res.size());
        assertEquals("Messi", res.get(0).getName());
    }

    @Test
    public void testBuscarJugadoresDisponibles_NoFilters() {
        Player u1 = new Player();
        u1.setEmail("p1@test.com");
        u1.setName("Messi");
        u1.setRole(Role.PLAYER);

        when(userRepository.findAll()).thenReturn(java.util.List.of(u1));
        when(teamRepository.findAll()).thenReturn(new ArrayList<>());

        java.util.List<User> res = playerService.buscarJugadoresDisponibles(null, null);
        
        assertEquals(1, res.size());
    }

    @Test
    public void testEnviarInvitacion_Success() {
        Invitation req = new Invitation();
        req.setPlayerEmail("player@test.com");
        req.setTeamName("Dream Team");
        req.setCaptainEmail("captain@test.com");

        Player u = new Player();
        u.setEmail("player@test.com");
        u.setRole(Role.PLAYER);

        when(userRepository.findByEmail("player@test.com")).thenReturn(Optional.of(u));
        when(teamRepository.findAll()).thenReturn(new ArrayList<>());
        when(invitationRepository.findByPlayerEmail("player@test.com")).thenReturn(new ArrayList<>());

        // Ensure save returns what was passed in
        when(invitationRepository.save(any(Invitation.class))).thenAnswer(i -> i.getArgument(0));

        Invitation res = playerService.enviarInvitacion(req);

        assertNotNull(res);
        assertEquals("PENDING", res.getStatus());
        verify(invitationRepository, times(1)).save(any(Invitation.class));
    }

    @Test
    public void testEnviarInvitacion_MissingEmail() {
        Invitation req = new Invitation();
        req.setTeamName("Dream Team");

        assertThrows(BusinessRuleException.class, () -> playerService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_MissingTeam() {
        Invitation req = new Invitation();
        req.setPlayerEmail("player@test.com");

        assertThrows(BusinessRuleException.class, () -> playerService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_PlayerNotFound() {
        Invitation req = new Invitation();
        req.setPlayerEmail("player@test.com");
        req.setTeamName("Dream Team");

        when(userRepository.findByEmail("player@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_NotAPlayer() {
        Invitation req = new Invitation();
        req.setPlayerEmail("admin@test.com");
        req.setTeamName("Dream Team");

        Player u = new Player();
        u.setEmail("admin@test.com");
        u.setRole(Role.ADMINISTRADOR_SISTEMA);

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(u));

        assertThrows(BusinessRuleException.class, () -> playerService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_AlreadyInTeam() {
        Invitation req = new Invitation();
        req.setPlayerEmail("player@test.com");
        req.setTeamName("Dream Team");

        Player u = new Player();
        u.setEmail("player@test.com");
        u.setRole(Role.PLAYER);

        Team team = new Team("Dream Team");
        team.setPlayers(new ArrayList<>());
        team.getPlayers().add(u);

        when(userRepository.findByEmail("player@test.com")).thenReturn(Optional.of(u));
        when(teamRepository.findAll()).thenReturn(java.util.List.of(team));

        assertThrows(BusinessRuleException.class, () -> playerService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_AlreadyInvited() {
        Invitation req = new Invitation();
        req.setPlayerEmail("player@test.com");
        req.setTeamName("Dream Team");

        Player u = new Player();
        u.setEmail("player@test.com");
        u.setRole(Role.PLAYER);

        Invitation existingInv = new Invitation();
        existingInv.setTeamName("Dream Team");
        existingInv.setStatus("PENDING");

        when(userRepository.findByEmail("player@test.com")).thenReturn(Optional.of(u));
        when(teamRepository.findAll()).thenReturn(new ArrayList<>());
        when(invitationRepository.findByPlayerEmail("player@test.com")).thenReturn(java.util.List.of(existingInv));

        assertThrows(BusinessRuleException.class, () -> playerService.enviarInvitacion(req));
    }
}
