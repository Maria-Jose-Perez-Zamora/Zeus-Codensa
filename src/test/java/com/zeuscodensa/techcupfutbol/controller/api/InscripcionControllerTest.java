package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationStatusUpdateRequestDTO;
import com.zeuscodensa.techcupfutbol.core.model.Registration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.zeuscodensa.techcupfutbol.core.service.RegistrationService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InscripcionControllerTest {

    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateInscripcion_Success() {
        RegistrationRequestDTO req = new RegistrationRequestDTO("Eq", "Tor", "url");
        Registration mockedReg = new Registration();
        mockedReg.setStatus("PENDING");

        when(registrationService.inscribir(any(Registration.class))).thenReturn(mockedReg);

        ResponseEntity<?> response = registrationController.createInscripcion(req);
        assertEquals(200, response.getStatusCode().value());
    }

    @Test
    public void testActualizarEstado_Success() {
        Registration mockedReg = new Registration();
        mockedReg.setStatus("APPROVED");

        when(registrationService.actualizarEstado("1", "APPROVED")).thenReturn(mockedReg);

        ResponseEntity<?> response = registrationController.actualizarEstado("1", new RegistrationStatusUpdateRequestDTO("APPROVED"));
        assertEquals(200, response.getStatusCode().value());
    }
}
