package com.zeuscodensa.techcupfutbol.controller.api;

import com.zeuscodensa.techcupfutbol.controller.dto.UserRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import com.zeuscodensa.techcupfutbol.core.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.security.Principal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;

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
    public void testGetAllUsers() {
        com.zeuscodensa.techcupfutbol.core.model.Player u1 = new com.zeuscodensa.techcupfutbol.core.model.Player();
        u1.setName("Carlos");
        Page<User> page = new PageImpl<>(Arrays.asList(u1));
        when(userService.getAllUsers(any(PageRequest.class))).thenReturn(page);

        ResponseEntity<Page<UserResponseDTO>> responseEntity = userController.getAll(0, 10);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(1, responseEntity.getBody().getTotalElements());
        assertEquals("Carlos", responseEntity.getBody().getContent().get(0).getName());
    }

    @Test
    public void testGetUserByEmail() {
        com.zeuscodensa.techcupfutbol.core.model.Player u1 = new com.zeuscodensa.techcupfutbol.core.model.Player();
        u1.setEmail("carlos@example.com");
        when(userService.getUserByEmail("carlos@example.com")).thenReturn(u1);

        ResponseEntity<UserResponseDTO> response = userController.getUserByEmail("carlos@example.com");
        assertEquals(200, response.getStatusCode().value());
        assertEquals("carlos@example.com", response.getBody().getEmail());
    }

    @Test
    public void testCreateUser() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("carlos@example.com");
        dto.setRole(Role.PLAYER);
        com.zeuscodensa.techcupfutbol.core.model.Player u1 = new com.zeuscodensa.techcupfutbol.core.model.Player();
        u1.setEmail("carlos@example.com");
        when(userService.registerUser(any())).thenReturn(u1);

        ResponseEntity<UserResponseDTO> response = userController.createUser(dto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("carlos@example.com", response.getBody().getEmail());
    }

    @Test
    public void testUpdateUser() {
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("carlos@example.com");
        dto.setRole(Role.PLAYER);
        com.zeuscodensa.techcupfutbol.core.model.Player u1 = new com.zeuscodensa.techcupfutbol.core.model.Player();
        u1.setEmail("carlos@example.com");
        when(userService.updateUser(eq("carlos@example.com"), any())).thenReturn(u1);

        ResponseEntity<UserResponseDTO> response = userController.updateUser("carlos@example.com", dto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("carlos@example.com", response.getBody().getEmail());
    }

    @Test
    public void testDeleteUser() {
        doNothing().when(userService).deleteUser("carlos@example.com");

        ResponseEntity<Void> response = userController.deleteUser("carlos@example.com");
        assertEquals(204, response.getStatusCode().value());
        verify(userService, times(1)).deleteUser("carlos@example.com");
    }

    @Test
    public void testGetProfile() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("carlos@example.com");
        com.zeuscodensa.techcupfutbol.core.model.Player u1 = new com.zeuscodensa.techcupfutbol.core.model.Player();
        u1.setEmail("carlos@example.com");
        when(userService.findByEmail("carlos@example.com")).thenReturn(Optional.of(u1));

        ResponseEntity<UserResponseDTO> response = userController.getProfile(principal);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("carlos@example.com", response.getBody().getEmail());
    }

    @Test
    public void testUpdateProfile() {
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("carlos@example.com");
        UserRequestDTO dto = new UserRequestDTO();
        dto.setEmail("carlos@example.com");
        dto.setRole(Role.PLAYER);
        com.zeuscodensa.techcupfutbol.core.model.Player u1 = new com.zeuscodensa.techcupfutbol.core.model.Player();
        u1.setEmail("carlos@example.com");
        when(userService.updateUser(eq("carlos@example.com"), any())).thenReturn(u1);

        ResponseEntity<UserResponseDTO> response = userController.updateProfile(principal, dto);
        assertEquals(200, response.getStatusCode().value());
        assertEquals("carlos@example.com", response.getBody().getEmail());
    }
}
