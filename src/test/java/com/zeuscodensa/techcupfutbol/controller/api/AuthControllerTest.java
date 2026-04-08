package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.LoginRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.LoginResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.zeuscodensa.techcupfutbol.core.service.AuthService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin_Success() {
        LoginRequestDTO req = new LoginRequestDTO("t@x.com", "pass");
        com.zeuscodensa.techcupfutbol.core.model.Player mockUser = new com.zeuscodensa.techcupfutbol.core.model.Player();
        mockUser.setEmail("t@x.com");
        mockUser.setRole(Role.PLAYER);

        when(authService.login("t@x.com", "pass")).thenReturn("token");
        when(userService.findByEmail("t@x.com")).thenReturn(Optional.of(mockUser));

        ResponseEntity<LoginResponseDTO> response = authController.login(req);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("token", response.getBody().getToken());
    }

    @Test
    public void testLogin_Error() {
        LoginRequestDTO req = new LoginRequestDTO("t", "pass");

        when(authService.login("t", "pass")).thenThrow(new IllegalArgumentException("error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> authController.login(req));
        assertEquals("error", thrown.getMessage());
    }
}
