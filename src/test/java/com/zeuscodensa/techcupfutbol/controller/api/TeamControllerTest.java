package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.TeamRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TeamResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.zeuscodensa.techcupfutbol.core.service.TeamService;

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
        validPlayers = Arrays.asList("user.test1-a@escuelaing.edu.co", "user.test2-a@escuelaing.edu.co");
    }

    @Test
    public void testCreateTeam_Success() {
        TeamRequestDTO request = new TeamRequestDTO("FC Zeta", "e.png", "Negro", validPlayers);
        Team mockedSavedTeam = new Team("FC Zeta");
        
        when(teamService.createTeam(any(Team.class), eq(validPlayers))).thenReturn(mockedSavedTeam);

        ResponseEntity<TeamResponseDTO> responseEntity = teamController.createTeam(request, null);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals("FC Zeta", responseEntity.getBody().getTeamName());
    }

    @Test
    public void testCreateTeam_ValidationError() {
        TeamRequestDTO request = new TeamRequestDTO();
        request.setPlayerEmails(Collections.emptyList());
        
        when(teamService.createTeam(any(Team.class), anyList()))
            .thenThrow(new IllegalArgumentException("Error de validacion"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> teamController.createTeam(request, null));
        assertEquals("Error de validacion", thrown.getMessage());
    }

    @Test
    public void testGetAllTeams() {
        Team t1 = new Team("Equipo1");
        when(teamService.getAllTeams()).thenReturn(Collections.singletonList(t1));

        ResponseEntity<List<TeamResponseDTO>> responseEntity = teamController.getAllTeams();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals("Equipo1", responseEntity.getBody().get(0).getTeamName());
    }
}
