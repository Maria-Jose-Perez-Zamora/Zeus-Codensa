package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.UserRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.zeuscodensa.techcupfutbol.core.service.UserService;

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
        UserRequestDTO request = new UserRequestDTO("Juan", "user.test1-a@escuelaing.edu.co", "123456", "Delantero", 9, null, Role.PLAYER);
        UserResponseDTO responseDto = new UserResponseDTO();
        responseDto.setName("Juan");

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

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> userController.register(request));
        assertEquals("Error de validación", thrown.getMessage());
    }

    @Test
    public void testGetAllUsers() {
        UserResponseDTO u1 = new UserResponseDTO();
        u1.setName("Carlos");
        when(userService.getAllUsers()).thenReturn(Arrays.asList(u1));

        ResponseEntity<List<UserResponseDTO>> responseEntity = userController.getAll();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals("Carlos", responseEntity.getBody().get(0).getName());
    }
}
