package core.service;

import dependencies.persistence.entity.InvitationEntity;
import dependencies.persistence.entity.TeamEntity;
import dependencies.persistence.entity.UserEntity;
import dependencies.persistence.repository.InvitationRepository;
import dependencies.persistence.repository.TeamRepository;
import dependencies.persistence.repository.UserRepository;
import core.model.Role;
import core.exception.BusinessRuleException;
import core.exception.ResourceNotFoundException;
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
}
