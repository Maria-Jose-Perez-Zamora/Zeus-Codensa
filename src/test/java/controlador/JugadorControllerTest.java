package controlador;

import dependencias.dto.InvitacionRequestDTO;
import dependencias.dto.InvitacionResponseDTO;
import dependencias.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import core.service.JugadorService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class JugadorControllerTest {

    @Mock
    private JugadorService jugadorService;

    @InjectMocks
    private JugadorController controller;

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
        InvitacionResponseDTO mockRes = new InvitacionResponseDTO();
        when(jugadorService.enviarInvitacion(any(InvitacionRequestDTO.class))).thenReturn(mockRes);
        ResponseEntity<InvitacionResponseDTO> res = controller.enviarInvitacion(new InvitacionRequestDTO());
        assertEquals(200, res.getStatusCode().value());
    }
}
