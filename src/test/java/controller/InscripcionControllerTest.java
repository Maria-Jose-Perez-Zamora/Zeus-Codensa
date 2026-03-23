package controller;

import dependencies.dto.RegistrationRequestDTO;
import dependencies.dto.RegistrationResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import core.service.RegistrationService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InscripcionControllerTest {

    @Mock
    private RegistrationService inscripcionService;

    @InjectMocks
    private RegistrationController inscripcionController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateInscripcion_Success() {
        RegistrationRequestDTO req = new RegistrationRequestDTO("Eq", "Tor", "url");
        RegistrationResponseDTO res = new RegistrationResponseDTO();
        res.setStatus("PENDIENTE");

        when(inscripcionService.inscribir(any())).thenReturn(res);

        ResponseEntity<?> response = inscripcionController.createInscripcion(req);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testActualizarEstado_Success() {
        RegistrationResponseDTO res = new RegistrationResponseDTO();
        res.setStatus("APROBADO");

        when(inscripcionService.actualizarEstado("1", "APROBADO")).thenReturn(res);

        ResponseEntity<?> response = inscripcionController.actualizarEstado("1", Map.of("status", "APROBADO"));
        assertEquals(200, response.getStatusCode().value());
    }
}
