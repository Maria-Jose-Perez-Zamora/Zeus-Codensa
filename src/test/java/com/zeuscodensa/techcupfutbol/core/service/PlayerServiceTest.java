package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.persistence.entity.InvitationEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.TeamEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.UserEntity;
import com.zeuscodensa.techcupfutbol.persistence.repository.InvitationRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.TeamRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.UserRepository;
import com.zeuscodensa.techcupfutbol.core.model.Role;
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
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private InvitationRepository invitationRepository;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testProcessInvitation_Accept_Success() {
        InvitationEntity inv = new InvitationEntity();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        inv.setTeamName("Dream Team");
        inv.setStatus("PENDING");

        TeamEntity team = new TeamEntity();
        team.setTeamName("Dream Team");
        team.setPlayers(new ArrayList<>());

        UserEntity player = new UserEntity();
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
        InvitationEntity inv = new InvitationEntity();
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
        InvitationEntity inv = new InvitationEntity();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        inv.setStatus("PENDING");

        TeamEntity otherTeam = new TeamEntity();
        otherTeam.setTeamName("Other Team");
        UserEntity player = new UserEntity();
        player.setEmail("player@test.com");
        ArrayList<UserEntity> players = new ArrayList<>();
        players.add(player);
        otherTeam.setPlayers(players);
        
        ArrayList<TeamEntity> allTeams = new ArrayList<>();
        allTeams.add(otherTeam);

        when(invitationRepository.findById("inv-123")).thenReturn(Optional.of(inv));
        when(teamRepository.findAll()).thenReturn(allTeams);

        assertThrows(BusinessRuleException.class, () -> 
            playerService.processInvitation("inv-123", "player@test.com", "ACEPTADA"));
    }

    @Test
    public void testProcessInvitation_WrongRecipient() {
        InvitationEntity inv = new InvitationEntity();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        when(invitationRepository.findById("inv-123")).thenReturn(Optional.of(inv));

        assertThrows(BusinessRuleException.class, () ->
            playerService.processInvitation("inv-123", "wrong@test.com", "ACEPTADA"));
    }

    @Test
    public void testProcessInvitation_AlreadyAnswered() {
        InvitationEntity inv = new InvitationEntity();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        inv.setStatus("ACEPTADA");
        when(invitationRepository.findById("inv-123")).thenReturn(Optional.of(inv));

        assertThrows(BusinessRuleException.class, () ->
            playerService.processInvitation("inv-123", "player@test.com", "ACEPTADA"));
    }

    @Test
    public void testProcessInvitation_InvalidStatus() {
        InvitationEntity inv = new InvitationEntity();
        inv.setId("inv-123");
        inv.setPlayerEmail("player@test.com");
        inv.setStatus("PENDING");
        when(invitationRepository.findById("inv-123")).thenReturn(Optional.of(inv));

        assertThrows(BusinessRuleException.class, () ->
            playerService.processInvitation("inv-123", "player@test.com", "INVALID_STATUS"));
    }

    @Test
    public void testBuscarJugadoresDisponibles_AllFilters() {
        UserEntity u1 = new UserEntity();
        u1.setEmail("p1@test.com");
        u1.setName("Messi");
        u1.setRole(Role.PLAYER);
        u1.setPosition("Forward");

        UserEntity u2 = new UserEntity();
        u2.setEmail("p2@test.com");
        u2.setName("Ronaldo");
        u2.setRole(Role.PLAYER);
        u2.setPosition("Forward");

        UserEntity u3 = new UserEntity(); // Not a player
        u3.setEmail("p3@test.com");
        u3.setRole(Role.ADMINISTRADOR_SISTEMA);

        TeamEntity team = new TeamEntity();
        team.setPlayers(new ArrayList<>());
        team.getPlayers().add(u2); // Ronaldo already in team

        when(userRepository.findAll()).thenReturn(java.util.List.of(u1, u2, u3));
        when(teamRepository.findAll()).thenReturn(java.util.List.of(team));

        java.util.List<com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO> res = playerService.buscarJugadoresDisponibles("Messi", "Forward");
        
        assertEquals(1, res.size());
        assertEquals("Messi", res.get(0).getName());
    }

    @Test
    public void testBuscarJugadoresDisponibles_NoFilters() {
        UserEntity u1 = new UserEntity();
        u1.setEmail("p1@test.com");
        u1.setName("Messi");
        u1.setRole(Role.PLAYER);
        u1.setPosition("Forward");

        when(userRepository.findAll()).thenReturn(java.util.List.of(u1));
        when(teamRepository.findAll()).thenReturn(new ArrayList<>());

        java.util.List<com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO> res = playerService.buscarJugadoresDisponibles(null, null);
        
        assertEquals(1, res.size());
    }

    @Test
    public void testEnviarInvitacion_Success() {
        com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO req = new com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO();
        req.setPlayerEmail("player@test.com");
        req.setTeamName("Dream Team");
        req.setCaptainEmail("captain@test.com");

        UserEntity u = new UserEntity();
        u.setEmail("player@test.com");
        u.setRole(Role.PLAYER);

        when(userRepository.findByEmail("player@test.com")).thenReturn(Optional.of(u));
        when(teamRepository.findAll()).thenReturn(new ArrayList<>());
        when(invitationRepository.findByPlayerEmail("player@test.com")).thenReturn(new ArrayList<>());

        com.zeuscodensa.techcupfutbol.controller.dto.InvitationResponseDTO res = playerService.enviarInvitacion(req);

        assertNotNull(res);
        assertEquals("PENDING", res.getStatus());
        verify(invitationRepository, times(1)).save(any(InvitationEntity.class));
    }

    @Test
    public void testEnviarInvitacion_MissingEmail() {
        com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO req = new com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO();
        req.setTeamName("Dream Team");

        assertThrows(BusinessRuleException.class, () -> playerService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_MissingTeam() {
        com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO req = new com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO();
        req.setPlayerEmail("player@test.com");

        assertThrows(BusinessRuleException.class, () -> playerService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_PlayerNotFound() {
        com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO req = new com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO();
        req.setPlayerEmail("player@test.com");
        req.setTeamName("Dream Team");

        when(userRepository.findByEmail("player@test.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_NotAPlayer() {
        com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO req = new com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO();
        req.setPlayerEmail("admin@test.com");
        req.setTeamName("Dream Team");

        UserEntity u = new UserEntity();
        u.setEmail("admin@test.com");
        u.setRole(Role.ADMINISTRADOR_SISTEMA);

        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(u));

        assertThrows(BusinessRuleException.class, () -> playerService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_AlreadyInTeam() {
        com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO req = new com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO();
        req.setPlayerEmail("player@test.com");
        req.setTeamName("Dream Team");

        UserEntity u = new UserEntity();
        u.setEmail("player@test.com");
        u.setRole(Role.PLAYER);

        TeamEntity team = new TeamEntity();
        team.setPlayers(new ArrayList<>());
        team.getPlayers().add(u);

        when(userRepository.findByEmail("player@test.com")).thenReturn(Optional.of(u));
        when(teamRepository.findAll()).thenReturn(java.util.List.of(team));

        assertThrows(BusinessRuleException.class, () -> playerService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_AlreadyInvited() {
        com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO req = new com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO();
        req.setPlayerEmail("player@test.com");
        req.setTeamName("Dream Team");

        UserEntity u = new UserEntity();
        u.setEmail("player@test.com");
        u.setRole(Role.PLAYER);

        InvitationEntity existingInv = new InvitationEntity();
        existingInv.setTeamName("Dream Team");
        existingInv.setStatus("PENDING");

        when(userRepository.findByEmail("player@test.com")).thenReturn(Optional.of(u));
        when(teamRepository.findAll()).thenReturn(new ArrayList<>());
        when(invitationRepository.findByPlayerEmail("player@test.com")).thenReturn(java.util.List.of(existingInv));

        assertThrows(BusinessRuleException.class, () -> playerService.enviarInvitacion(req));
    }
}
