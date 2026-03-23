package controlador;

import dependencias.dto.InscripcionRequestDTO;
import dependencias.dto.InscripcionResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import core.service.InscripcionService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InscripcionControllerTest {

    @Mock
    private InscripcionService inscripcionService;

    @InjectMocks
    private InscripcionController inscripcionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateInscripcion_Success() {
        InscripcionRequestDTO req = new InscripcionRequestDTO("Eq", "Tor", "url");
        InscripcionResponseDTO res = new InscripcionResponseDTO();
        res.setEstado("PENDIENTE");

        when(inscripcionService.inscribir(any())).thenReturn(res);

        ResponseEntity<?> response = inscripcionController.createInscripcion(req);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testActualizarEstado_Success() {
        InscripcionResponseDTO res = new InscripcionResponseDTO();
        res.setEstado("APROBADO");

        when(inscripcionService.actualizarEstado("1", "APROBADO")).thenReturn(res);

        ResponseEntity<?> response = inscripcionController.actualizarEstado("1", Map.of("estado", "APROBADO"));
        assertEquals(200, response.getStatusCode().value());
    }
}
