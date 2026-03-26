package core.service;

import core.model.Invitation;
import core.model.Player;
import core.model.Team;
import core.exception.BusinessRuleException;
import core.exception.ResourceNotFoundException;
import dependencies.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {

    private PlayerService playerService;

    @BeforeEach
    public void setUp() {
        playerService = new PlayerService();
        DataStorage.users.clear();
        DataStorage.teams.clear();
        DataStorage.invitations.clear();
    }

    @Test
    public void testAcceptInvitation_Success() {
        Player player = new Player();
        player.setEmail("user.test1-a@escuelaing.edu.co");
        DataStorage.users.add(player);

        Team team = new Team("Dream Team");
        DataStorage.teams.add(team);

        Invitation inv = new Invitation("user.test2-a@escuelaing.edu.co", "user.test1-a@escuelaing.edu.co", "Dream Team");
        inv.setStatus("PENDING");
        DataStorage.invitations.add(inv);

        playerService.acceptInvitation(inv.getId(), "user.test1-a@escuelaing.edu.co");

        assertEquals("ACCEPTED", inv.getStatus());
        assertEquals(1, team.getPlayers().size());
        assertEquals("user.test1-a@escuelaing.edu.co", team.getPlayers().get(0).getEmail());
    }

    @Test
    public void testDeclineInvitation_Success() {
        Invitation inv = new Invitation("user.test2-a@escuelaing.edu.co", "user.test1-a@escuelaing.edu.co", "Dream Team");
        inv.setStatus("PENDING");
        DataStorage.invitations.add(inv);

        playerService.declineInvitation(inv.getId(), "user.test1-a@escuelaing.edu.co");

        assertEquals("DECLINED", inv.getStatus());
    }

    @Test
    public void testAcceptInvitation_NotFound() {
        assertThrows(ResourceNotFoundException.class, () -> 
            playerService.acceptInvitation("fake-id", "user.test1-a@escuelaing.edu.co"));
    }

    @Test
    public void testAcceptInvitation_AlreadyInTeam() {
        Player player = new Player();
        player.setEmail("user.test1-a@escuelaing.edu.co");
        DataStorage.users.add(player);

        Team team = new Team("Dream Team");
        team.getPlayers().add(player);
        DataStorage.teams.add(team);

        Invitation inv = new Invitation("user.test2-a@escuelaing.edu.co", "user.test1-a@escuelaing.edu.co", "Otro Team");
        inv.setStatus("PENDING");
        DataStorage.invitations.add(inv);

        assertThrows(BusinessRuleException.class, () -> 
            playerService.acceptInvitation(inv.getId(), "user.test1-a@escuelaing.edu.co"));
    }
}
