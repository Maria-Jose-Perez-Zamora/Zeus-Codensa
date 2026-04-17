package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.InvitationRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.InvitationResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Invitation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.zeuscodensa.techcupfutbol.core.service.PlayerService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class JugadorControllerTest {

    @Mock
    private PlayerService jugadorService;

    @InjectMocks
    private PlayerController controller;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuscarDisponibles() {
        when(jugadorService.buscarJugadoresDisponibles(anyString(), anyString())).thenReturn(Collections.emptyList());
        ResponseEntity<List<UserResponseDTO>> res = controller.buscarDisponibles("Juan", "Delantero");
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    public void testEnviarInvitacion() {
        Invitation mockRes = new Invitation();
        when(jugadorService.enviarInvitacion(any(Invitation.class))).thenReturn(mockRes);
        ResponseEntity<InvitationResponseDTO> res = controller.enviarInvitacion(new InvitationRequestDTO());
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    public void testSolicitarUnirse() {
        Invitation mockRes = new Invitation();
        mockRes.setStatus("REQUESTED");
        mockRes.setTeamName("My Team");

        org.springframework.security.core.Authentication auth = org.mockito.Mockito.mock(org.springframework.security.core.Authentication.class);
        when(auth.getName()).thenReturn("player@test.com");
        when(jugadorService.enviarSolicitudUnirse("player@test.com", "My Team")).thenReturn(mockRes);

        ResponseEntity<InvitationResponseDTO> res = controller.solicitarUnirse("My Team", auth);
        assertEquals(200, res.getStatusCode().value());
        assertEquals("REQUESTED", res.getBody().getStatus());
    }
}
