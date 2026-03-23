package controller;

import dependencies.dto.LoginRequestDTO;
import dependencies.dto.LoginResponseDTO;
import dependencies.dto.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import core.service.AuthService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        LoginRequestDTO req = new LoginRequestDTO("t", "pass");
        LoginResponseDTO res = new LoginResponseDTO("token", new UserResponseDTO());

        when(authService.login(any())).thenReturn(res);

        ResponseEntity<?> response = authController.login(req);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(res, response.getBody());
    }

    @Test
    public void testLogin_Error() {
        LoginRequestDTO req = new LoginRequestDTO("t", "pass");

        when(authService.login(any())).thenThrow(new IllegalArgumentException("error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> authController.login(req));
        assertEquals("error", thrown.getMessage());
    }
}
