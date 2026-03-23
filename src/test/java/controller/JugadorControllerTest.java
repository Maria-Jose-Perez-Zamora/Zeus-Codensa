package controller;

import dependencies.dto.InvitationRequestDTO;
import dependencies.dto.InvitationResponseDTO;
import dependencies.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import core.service.PlayerService;

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
        InvitationResponseDTO mockRes = new InvitationResponseDTO();
        when(jugadorService.enviarInvitacion(any(InvitationRequestDTO.class))).thenReturn(mockRes);
        ResponseEntity<InvitationResponseDTO> res = controller.enviarInvitacion(new InvitationRequestDTO());
        assertEquals(200, res.getStatusCode().value());
    }
}
