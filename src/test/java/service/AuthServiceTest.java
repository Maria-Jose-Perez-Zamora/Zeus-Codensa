package service;

import dto.LoginRequestDTO;
import dto.LoginResponseDTO;
import model.Role;
import model.Jugador;
import util.DataStorage;
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
        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }

    @Test
    public void testLogin_NullFields() {
        LoginRequestDTO request = new LoginRequestDTO(null, "wrong");
        assertThrows(IllegalArgumentException.class, () -> authService.login(request));
    }
}
