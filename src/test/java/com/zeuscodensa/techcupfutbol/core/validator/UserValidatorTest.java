package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserValidatorTest {

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    private Player validUser;

    @BeforeEach
    public void setUp() {
        validUser = new Player();
        validUser.setName("Valid Name");
        validUser.setEmail("john.doe-a@escuelaing.edu.co");
        validUser.setPassword("securePassword");
        validUser.setRole(Role.PLAYER);
        validUser.setPosition("Forward");
        validUser.setJerseyNumber(10);
    }

    @Test
    public void testValidateForRegistration_Success() {
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userValidator.validateForRegistration(validUser));
    }

    @Test
    public void testValidateForRegistration_NullName() {
        validUser.setName(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validUser));
        assertEquals("El name no puede estar vacío", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_EmptyName() {
        validUser.setName("   ");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validUser));
        assertEquals("El name no puede estar vacío", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_NullEmail() {
        validUser.setEmail(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validUser));
        assertEquals("Email cannot be empty", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_InvalidEmailFormat() {
        validUser.setEmail("invalid.email@gmail.com");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validUser));
        assertTrue(ex.getMessage().contains("El correo debe ser institucional"));
    }

    @Test
    public void testValidateForRegistration_PasswordTooShort() {
        validUser.setPassword("12345");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validUser));
        assertEquals("Password must be at least 6 characters", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_NullRole() {
        validUser.setRole(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validUser));
        assertEquals("El rol del usuario es obligatorio", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_PlayerRoleMissingPosition() {
        Player player = new Player();
        player.setName("P"); player.setEmail("p.p-a@escuelaing.edu.co"); player.setPassword("1234567"); player.setRole(Role.PLAYER);
        player.setPosition(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(player));
        assertEquals("La posición es obligatoria para players y capitanes", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_PlayerRoleMissingJerseyNumber() {
        Player player = new Player();
        player.setName("P"); player.setEmail("p.p-a@escuelaing.edu.co"); player.setPassword("1234567"); player.setRole(Role.PLAYER);
        player.setPosition("Forward");
        player.setJerseyNumber(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(player));
        assertEquals("El número de dorsal es obligatorio y debe ser mayor a 0", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_CaptainRoleInvalidJerseyNumber() {
        Player player = new Player();
        player.setName("P"); player.setEmail("p.p-a@escuelaing.edu.co"); player.setPassword("1234567"); player.setRole(Role.CAPTAIN);
        player.setPosition("Midfielder");
        player.setJerseyNumber(0);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(player));
        assertEquals("El número de dorsal es obligatorio y debe ser mayor a 0", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_PlayerRoleSuccess() {
        Player player = new Player();
        player.setName("P"); player.setEmail("p.p-a@escuelaing.edu.co"); player.setPassword("1234567"); player.setRole(Role.PLAYER);
        player.setPosition("Goalkeeper");
        player.setJerseyNumber(1);

        when(userRepository.findByEmail(player.getEmail())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userValidator.validateForRegistration(player));
    }

    @Test
    public void testValidateForRegistration_DuplicatedEmail() {
        when(userRepository.findByEmail(validUser.getEmail())).thenReturn(Optional.of(new Player()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validUser));
        assertEquals("El correo ya se encuentra registrado", ex.getMessage());
    }
}
