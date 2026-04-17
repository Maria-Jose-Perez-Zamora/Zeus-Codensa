package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.MatchCardsRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchLineupRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchScoreRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.RefereeAssignmentRequestDTO;
import com.zeuscodensa.techcupfutbol.core.model.Match;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.zeuscodensa.techcupfutbol.core.service.MatchService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PartidoControllerTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchController matchController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegistrarPartido_Success() {
        Match mockedMatch = new Match();
        when(matchService.registrarPartido(any(Match.class))).thenReturn(mockedMatch);

        ResponseEntity<?> response = matchController.registrarPartido(new MatchRequestDTO());
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testActualizarMarcador_Success() {
        Match mockedMatch = new Match();
        mockedMatch.setHomeScore(2);
        when(matchService.actualizarMarcador("1", 2, 1)).thenReturn(mockedMatch);

        ResponseEntity<?> response = matchController.actualizarMarcador("1", new MatchScoreRequestDTO(2, 1));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegistrarAlineacion_Success() {
        Match mockedMatch = new Match();
        mockedMatch.setId("1");
        when(matchService.registrarAlineacion(eq("1"), eq("TeamA"), anyList())).thenReturn(mockedMatch);

        MatchLineupRequestDTO body = new MatchLineupRequestDTO("TeamA", Arrays.asList("user.test1-a@escuelaing.edu.co", "user.test2-a@escuelaing.edu.co"));

        ResponseEntity<?> response = matchController.registrarAlineacion("1", body);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegistrarAlineacion_Error_Returns400() {
        when(matchService.registrarAlineacion(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Equipo no participa"));

        MatchLineupRequestDTO body = new MatchLineupRequestDTO("Otro", List.of("user.test3-a@escuelaing.edu.co"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> matchController.registrarAlineacion("1", body));
        assertEquals("Equipo no participa", thrown.getMessage());
    }

    @Test
    public void testRegistrarTarjetas_Success() {
        Match mockedMatch = new Match();
        when(matchService.registrarTarjetas(eq("1"), anyMap(), anyMap())).thenReturn(mockedMatch);

        MatchCardsRequestDTO body = new MatchCardsRequestDTO(new HashMap<>(), new HashMap<>());

        ResponseEntity<?> response = matchController.registrarTarjetas("1", body);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testRegistrarTarjetas_Error_Returns400() {
        when(matchService.registrarTarjetas(any(), any(), any()))
                .thenThrow(new IllegalArgumentException("Match no encontrado"));

        MatchCardsRequestDTO body = new MatchCardsRequestDTO();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> matchController.registrarTarjetas("999", body));
        assertEquals("Match no encontrado", thrown.getMessage());
    }

    @Test
    public void testAsignarArbitro_Success() {
        Match mockedMatch = new Match();
        mockedMatch.setRefereeEmail("user.test4-a@escuelaing.edu.co");
        when(matchService.asignarArbitro("1", "user.test4-a@escuelaing.edu.co")).thenReturn(mockedMatch);

        ResponseEntity<?> response = matchController.asignarArbitro("1", new RefereeAssignmentRequestDTO("user.test4-a@escuelaing.edu.co"));
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testAsignarArbitro_NotArbitro_Returns400() {
        when(matchService.asignarArbitro(any(), any()))
                .thenThrow(new IllegalArgumentException("No es árbitro"));

        RefereeAssignmentRequestDTO body = new RefereeAssignmentRequestDTO("user.test5-a@escuelaing.edu.co");

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> matchController.asignarArbitro("1", body));
        assertEquals("No es árbitro", thrown.getMessage());
    }

    @Test
    public void testGetMisPartidos_Success() {
        when(matchService.getMatchesByReferee("user.test4-a@escuelaing.edu.co")).thenReturn(Collections.emptyList());
        ResponseEntity<List<MatchResponseDTO>> response = matchController.getMisPartidos("user.test4-a@escuelaing.edu.co");
        assertEquals(200, response.getStatusCode().value());
    }
}
