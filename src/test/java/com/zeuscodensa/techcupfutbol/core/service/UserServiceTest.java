package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.validator.UserValidator;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder; // NUEVA IMPORTACIÓN

import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.PersistenceAccessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private UserValidator userValidator;

    @Mock
    private PasswordEncoder passwordEncoder; // NUEVA DEPENDENCIA MOCK

    @InjectMocks
    private UserService userService;
    @Test
    public void testRegisterUser_Success() {
        Player newUser = new Player();
        newUser.setName("Ana");
        newUser.setEmail("user.test1-a@escuelaing.edu.co");
        newUser.setPassword("password123"); // Password original
        newUser.setRole(Role.PLAYER);

        String encodedPass = "encoded_password_hash"; // Password cifrada simulada

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn(encodedPass); // Simular cifrado
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Retornar el mismo usuario
        doNothing().when(userValidator).validateForRegistration(newUser);

        User saved = userService.registerUser(newUser);

        assertNotNull(saved);
        assertEquals(encodedPass, saved.getPassword()); // VERIFICAR QUE SE GUARDÓ CIFRADA
        assertNotEquals("password123", saved.getPassword()); // VERIFICAR QUE NO ES PLANA

        verify(passwordEncoder, times(1)).encode(anyString()); // VERIFICAR QUE SE LLAMÓ AL ENCODER
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testGetAllUsers() {
        Player u1 = new Player();
        u1.setName("Ana");
        u1.setEmail("user.test1-a@escuelaing.edu.co");
        u1.setRole(Role.PLAYER);

        Player u2 = new Player();
        u2.setName("Pepe");
        u2.setEmail("user.test2-a@escuelaing.edu.co");
        u2.setRole(Role.PLAYER);

        List<User> list = new ArrayList<>();
        list.add(u1);
        list.add(u2);

        when(userRepository.findAll()).thenReturn(list);

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testRegisterUser_DuplicateEmail_Throws() {
        Player existing = new Player();
        existing.setEmail("user.test1-a@escuelaing.edu.co");

        doNothing().when(userValidator).validateForRegistration(existing);
        when(userRepository.findByEmail(existing.getEmail())).thenReturn(Optional.of(existing));

        assertThrows(BusinessRuleException.class, () -> userService.registerUser(existing));
    }

    @Test
    public void testRegisterUser_PersistenceError_Throws() {
        Player newUser = new Player();
        newUser.setEmail("user.test1-a@escuelaing.edu.co");
        newUser.setPassword("plain");

        doNothing().when(userValidator).validateForRegistration(newUser);
        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenThrow(new PersistenceAccessException("db error"));

        assertThrows(PersistenceAccessException.class, () -> userService.registerUser(newUser));
    }

    @Test
    public void testGetAllUsers_Empty() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        List<User> users = userService.getAllUsers();
        assertTrue(users.isEmpty());
    }

    @Test
    public void testGetAllUsers_PersistenceError_Throws() {
        when(userRepository.findAll()).thenThrow(new RuntimeException("db error"));
        assertThrows(PersistenceAccessException.class, () -> userService.getAllUsers());
    }
}