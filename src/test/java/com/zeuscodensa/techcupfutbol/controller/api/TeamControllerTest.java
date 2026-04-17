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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

        ResponseEntity<TeamResponseDTO> responseEntity = teamController.createTeam(request);

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

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> teamController.createTeam(request));
        assertEquals("Error de validacion", thrown.getMessage());
    }

    @Test
    public void testGetAllTeams() {
        Team t1 = new Team("Equipo1");
        Page<Team> page = new PageImpl<>(Collections.singletonList(t1));
        when(teamService.getAllTeams(any(), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<TeamResponseDTO>> responseEntity = teamController.getAllTeams(null, PageRequest.of(0, 10));

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, responseEntity.getBody().getContent().size());
        assertEquals("Equipo1", responseEntity.getBody().getContent().get(0).getTeamName());
    }
    @Test
    public void testGetTeamById() {
        Team t1 = new Team("Equipo1");
        when(teamService.getTeamById(1L)).thenReturn(t1);

        ResponseEntity<TeamResponseDTO> responseEntity = teamController.getTeamById(1L);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals("Equipo1", responseEntity.getBody().getTeamName());
    }

    @Test
    public void testUpdateTeam() {
        TeamRequestDTO request = new TeamRequestDTO("Equipo Modificado", "e.png", "Blanco", validPlayers);
        Team t1 = new Team("Equipo Modificado");
        when(teamService.updateTeam(eq(1L), any(Team.class))).thenReturn(t1);

        ResponseEntity<TeamResponseDTO> responseEntity = teamController.updateTeam(1L, request);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals("Equipo Modificado", responseEntity.getBody().getTeamName());
    }

    @Test
    public void testDeleteTeam() {
        doNothing().when(teamService).deleteTeam(1L);

        ResponseEntity<Void> responseEntity = teamController.deleteTeam(1L);

        assertEquals(204, responseEntity.getStatusCode().value());
        verify(teamService, times(1)).deleteTeam(1L);
    }
}
