package core.service;

import dependencias.dto.UserRequestDTO;
import dependencias.dto.UserResponseDTO;
import core.model.Role;
import dependencias.util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        userService = new UserService();
    }

    @Test
    public void testRegisterUser_Success() {
        UserRequestDTO request = new UserRequestDTO("Ana", "ana@test.com", "123456", "Defensa", 3, null, Role.JUGADOR);
        
        UserResponseDTO response = userService.registerUser(request);
        
        assertNotNull(response);
        assertEquals("Ana", response.getNombre());
        assertEquals("ana@test.com", response.getCorreo());
        assertEquals(Role.JUGADOR, response.getRole());
        assertEquals(1, DataStorage.users.size());
    }

    @Test
    public void testGetAllUsers() {
        UserRequestDTO req1 = new UserRequestDTO("Ana", "ana@test.com", "123456", "Defensa", 3, null, Role.JUGADOR);
        UserRequestDTO req2 = new UserRequestDTO("Pepe", "pepe@test.com", "123456", "Portero", 1, null, Role.JUGADOR);
        
        userService.registerUser(req1);
        userService.registerUser(req2);
        
        List<UserResponseDTO> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }
}
