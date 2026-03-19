package service;

import dto.TeamRequestDTO;
import dto.TeamResponseDTO;
import model.User;
import model.Jugador;
import model.Role;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamServiceTest {
    
    private TeamService teamService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        teamService = new TeamService();
    }

    @Test
    public void testCreateTeam_WithoutPlayers_Success() {
        TeamRequestDTO request = new TeamRequestDTO("Tigres FC", "tigres.png", "Amarillo", Collections.emptyList());
        
        TeamResponseDTO response = teamService.createTeam(request);
        
        assertNotNull(response);
        assertEquals("Tigres FC", response.getNombreEquipo());
        assertTrue(response.getJugadores().isEmpty());
        assertEquals(1, DataStorage.teams.size());
    }

    @Test
    public void testCreateTeam_WithPlayers_Success() {
        Jugador player1 = new Jugador();
        player1.setNombre("Ana");
        player1.setCorreo("ana@test.com");
        player1.setContrasena("123456");
        player1.setRole(Role.JUGADOR);
        DataStorage.users.add(player1);

        TeamRequestDTO request = new TeamRequestDTO("Tigres FC", "tigres.png", "Amarillo", Arrays.asList("ana@test.com"));
        TeamResponseDTO response = teamService.createTeam(request);
        
        assertNotNull(response);
        assertEquals(1, response.getJugadores().size());
        assertEquals("Ana", response.getJugadores().get(0).getNombre());
    }

    @Test
    public void testGetAllTeams() {
        TeamRequestDTO request1 = new TeamRequestDTO("Equipo A", null, null, null);
        TeamRequestDTO request2 = new TeamRequestDTO("Equipo B", null, null, null);
        
        teamService.createTeam(request1);
        teamService.createTeam(request2);
        
        List<TeamResponseDTO> teams = teamService.getAllTeams();
        assertEquals(2, teams.size());
    }
}
