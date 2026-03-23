package controller;

import dependencies.dto.TeamRequestDTO;
import dependencies.dto.TeamResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import core.service.TeamService;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    private List<String> validPlayers;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validPlayers = Arrays.asList("1@a.com", "2@a.com", "3@a.com", "4@a.com", "5@a.com");
    }

    @Test
    public void testCreateTeam_Success() {
        TeamRequestDTO request = new TeamRequestDTO("FC Zeta", "e.png", "Negro", validPlayers);
        TeamResponseDTO responseDto = new TeamResponseDTO();
        responseDto.setNombreEquipo("FC Zeta");

        when(teamService.createTeam(any(TeamRequestDTO.class))).thenReturn(responseDto);

        ResponseEntity<?> responseEntity = teamController.createTeam(request);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(responseDto, responseEntity.getBody());
    }

    @Test
    public void testCreateTeam_ValidationError() {
        TeamRequestDTO request = new TeamRequestDTO();
        
        when(teamService.createTeam(any(TeamRequestDTO.class)))
            .thenThrow(new IllegalArgumentException("Error de validacion"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> teamController.createTeam(request));
        assertEquals("Error de validacion", thrown.getMessage());
    }

    @Test
    public void testGetAllTeams() {
        TeamResponseDTO t1 = new TeamResponseDTO();
        t1.setNombreEquipo("Equipo1");
        when(teamService.getAllTeams()).thenReturn(Collections.singletonList(t1));

        ResponseEntity<List<TeamResponseDTO>> responseEntity = teamController.getAllTeams();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals("Equipo1", responseEntity.getBody().get(0).getNombreEquipo());
    }
}
