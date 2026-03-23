package core.service;

import dependencias.dto.LoginRequestDTO;
import dependencias.dto.LoginResponseDTO;
import core.model.Role;
import core.model.Jugador;
import dependencias.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        authService = new AuthService();
    }

    @Test
    public void testLogin_Success() {
        Jugador u = new Jugador();
        u.setCorreo("test@test.com");
        u.setContrasena("secret");
        u.setRole(Role.JUGADOR);
        DataStorage.users.add(u);

        LoginRequestDTO request = new LoginRequestDTO("test@test.com", "secret");
        LoginResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("test@test.com", response.getUser().getCorreo());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        Jugador u = new Jugador();
        u.setCorreo("test@test.com");
        u.setContrasena("secret");
        DataStorage.users.add(u);

        LoginRequestDTO request = new LoginRequestDTO("test@test.com", "wrong");
        assertThrows(RuntimeException.class, () -> authService.login(request));
    }

    @Test
    public void testLogin_NullFields() {
        LoginRequestDTO request = new LoginRequestDTO(null, "wrong");
        assertThrows(RuntimeException.class, () -> authService.login(request));
    }
}
