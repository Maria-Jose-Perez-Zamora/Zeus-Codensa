package com.zeuscodensa.techcupfutbol.controller.api;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.zeuscodensa.techcupfutbol.controller.dto.LoginRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.LoginResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.service.AuthService;
import com.zeuscodensa.techcupfutbol.core.service.UserService;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() {
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
    void testLogin_Error() {
        LoginRequestDTO req = new LoginRequestDTO("t", "pass");

        when(authService.login("t", "pass")).thenThrow(new IllegalArgumentException("error"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> authController.login(req));
        assertEquals("error", thrown.getMessage());
    }

    @Test
    void testRegisterUser_Success() {
        UserRequestDTO request = new UserRequestDTO("Juan", "user.test1-a@escuelaing.edu.co", "123456", "Delantero", 9, null, Role.PLAYER);
        com.zeuscodensa.techcupfutbol.core.model.Player userMock = new com.zeuscodensa.techcupfutbol.core.model.Player();
        userMock.setName("Juan");
        userMock.setRole(Role.PLAYER);

        when(userService.registerUser(any(User.class))).thenReturn(userMock);

        ResponseEntity<UserResponseDTO> responseEntity = authController.register(request);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertNotNull(responseEntity.getBody());
        assertEquals("Juan", responseEntity.getBody().getName());
    }

    @Test
    void testRegisterUser_ValidationError() {
        UserRequestDTO request = new UserRequestDTO();
        request.setRole(Role.PLAYER);
        request.setEmail("t@x.com");
        
        when(userService.registerUser(any(com.zeuscodensa.techcupfutbol.core.model.User.class)))
            .thenThrow(new IllegalArgumentException("Error de validación"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> authController.register(request));
        assertEquals("Error de validación", thrown.getMessage());
    }
}
