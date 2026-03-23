package core.service;

import dependencies.dto.UserRequestDTO;
import dependencies.dto.UserResponseDTO;
import core.model.Role;
import dependencies.util.DataStorage;
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
        UserRequestDTO request = new UserRequestDTO("Ana", "ana@test.com", "123456", "Defensa", 3, null, Role.PLAYER);
        
        UserResponseDTO response = userService.registerUser(request);
        
        assertNotNull(response);
        assertEquals("Ana", response.getName());
        assertEquals("ana@test.com", response.getCorreo());
        assertEquals(Role.PLAYER, response.getRole());
        assertEquals(1, DataStorage.users.size());
    }

    @Test
    public void testGetAllUsers() {
        UserRequestDTO req1 = new UserRequestDTO("Ana", "ana@test.com", "123456", "Defensa", 3, null, Role.PLAYER);
        UserRequestDTO req2 = new UserRequestDTO("Pepe", "pepe@test.com", "123456", "Portero", 1, null, Role.PLAYER);
        
        userService.registerUser(req1);
        userService.registerUser(req2);
        
        List<UserResponseDTO> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }
}
