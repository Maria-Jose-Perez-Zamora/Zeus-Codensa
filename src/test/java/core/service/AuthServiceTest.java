package core.service;

import dependencies.dto.LoginRequestDTO;
import dependencies.dto.LoginResponseDTO;
import core.model.Role;
import core.model.Player;
import dependencies.util.DataStorage;
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
        Player u = new Player();
        u.setCorreo("test@test.com");
        u.setContrasena("secret");
        u.setRole(Role.PLAYER);
        DataStorage.users.add(u);

        LoginRequestDTO request = new LoginRequestDTO("test@test.com", "secret");
        LoginResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertNotNull(response.getToken());
        assertEquals("test@test.com", response.getUser().getCorreo());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        Player u = new Player();
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
