package controller;

import dto.UserRequestDTO;
import dto.UserResponseDTO;
import model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        UserRequestDTO request = new UserRequestDTO("Juan", "juan@test.com", "123456", "Delantero", 9, null, Role.JUGADOR);
        UserResponseDTO responseDto = new UserResponseDTO();
        responseDto.setNombre("Juan");

        when(userService.registerUser(any(UserRequestDTO.class))).thenReturn(responseDto);

        ResponseEntity<?> responseEntity = userController.register(request);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(responseDto, responseEntity.getBody());
    }

    @Test
    public void testRegisterUser_ValidationError() {
        UserRequestDTO request = new UserRequestDTO();
        
        when(userService.registerUser(any(UserRequestDTO.class)))
            .thenThrow(new IllegalArgumentException("Error de validación"));

        ResponseEntity<?> responseEntity = userController.register(request);

        assertEquals(400, responseEntity.getStatusCode().value());
        assertEquals("Error de validación", responseEntity.getBody());
    }

    @Test
    public void testGetAllUsers() {
        UserResponseDTO u1 = new UserResponseDTO();
        u1.setNombre("Carlos");
        when(userService.getAllUsers()).thenReturn(Arrays.asList(u1));

        ResponseEntity<List<UserResponseDTO>> responseEntity = userController.getAll();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals("Carlos", responseEntity.getBody().get(0).getNombre());
    }
}
