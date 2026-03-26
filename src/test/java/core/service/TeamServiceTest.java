package core.service;

import dependencies.dto.TeamRequestDTO;
import dependencies.dto.TeamResponseDTO;
import core.model.Player;
import core.model.Role;
import dependencies.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamServiceTest {
    
    private TeamService teamService;
    private List<String> validPlayers;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        teamService = new TeamService();
        validPlayers = Arrays.asList("user.test1-a@escuelaing.edu.co", "user.test2-a@escuelaing.edu.co", "user.test3-a@escuelaing.edu.co", "user.test4-a@escuelaing.edu.co", "user.test5-a@escuelaing.edu.co", "user.test6-a@escuelaing.edu.co", "user.test7-a@escuelaing.edu.co");
        
        for (String email : validPlayers) {
            Player j = new Player();
            j.setEmail(email);
            j.setName("Player " + email);
            j.setRole(Role.PLAYER);
            DataStorage.users.add(j);
        }
    }

    @Test
    public void testCreateTeam_WithPlayers_Success() {
        TeamRequestDTO request = new TeamRequestDTO("Tigres FC", "tigres.png", "Amarillo", validPlayers);
        TeamResponseDTO response = teamService.createTeam(request);
        
        assertNotNull(response);
        assertEquals(7, response.getPlayers().size());
        assertEquals("Tigres FC", response.getTeamName());
        assertEquals(1, DataStorage.teams.size());
    }

    @Test
    public void testGetAllTeams() {
        TeamRequestDTO request1 = new TeamRequestDTO("Equipo A", null, null, validPlayers);
        
        List<String> validPlayersB = Arrays.asList("user.test8-a@escuelaing.edu.co", "user.test9-a@escuelaing.edu.co", "user.test10-a@escuelaing.edu.co", "user.test11-a@escuelaing.edu.co", "user.test12-a@escuelaing.edu.co", "user.test13-a@escuelaing.edu.co", "user.test14-a@escuelaing.edu.co");
        for (String email : validPlayersB) {
            Player j = new Player(); j.setEmail(email); DataStorage.users.add(j);
        }
        TeamRequestDTO request2 = new TeamRequestDTO("Equipo B", null, null, validPlayersB);
        
        teamService.createTeam(request1);
        teamService.createTeam(request2);
        
        List<TeamResponseDTO> teams = teamService.getAllTeams();
        assertEquals(2, teams.size());
    }
}
