package controller;

import dto.TeamRequestDTO;
import dto.TeamResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import service.TeamService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateTeam_Success() {
        TeamRequestDTO request = new TeamRequestDTO("Zorros FC", null, null, null);
        TeamResponseDTO responseDto = new TeamResponseDTO();
        responseDto.setNombreEquipo("Zorros FC");

        when(teamService.createTeam(any(TeamRequestDTO.class))).thenReturn(responseDto);

        ResponseEntity<?> responseEntity = teamController.createTeam(request);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(responseDto, responseEntity.getBody());
    }

    @Test
    public void testCreateTeam_ValidationError() {
        TeamRequestDTO request = new TeamRequestDTO();
        
        when(teamService.createTeam(any(TeamRequestDTO.class)))
            .thenThrow(new IllegalArgumentException("Nombre vacío"));

        ResponseEntity<?> responseEntity = teamController.createTeam(request);

        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals("Nombre vacío", responseEntity.getBody());
    }

    @Test
    public void testGetAllTeams() {
        TeamResponseDTO t1 = new TeamResponseDTO();
        t1.setNombreEquipo("Lions");
        when(teamService.getAllTeams()).thenReturn(Arrays.asList(t1));

        ResponseEntity<List<TeamResponseDTO>> responseEntity = teamController.getAllTeams();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals("Lions", responseEntity.getBody().get(0).getNombreEquipo());
    }
}
