package core.validator;

import core.model.Role;
import dependencies.dto.UserRequestDTO;
import dependencies.persistence.entity.UserEntity;
import dependencies.persistence.repository.UserRepository;
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
    private UserRepository userRepository;

    @InjectMocks
    private UserValidator userValidator;

    private UserRequestDTO validRequest;

    @BeforeEach
    public void setUp() {
        validRequest = new UserRequestDTO();
        validRequest.setName("Valid Name");
        validRequest.setEmail("john.doe-a@escuelaing.edu.co");
        validRequest.setPassword("securePassword");
        validRequest.setRole(Role.TOURNAMENT_ORGANIZER);
    }

    @Test
    public void testValidateForRegistration_Success() {
        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userValidator.validateForRegistration(validRequest));
    }

    @Test
    public void testValidateForRegistration_NullName() {
        validRequest.setName(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validRequest));
        assertEquals("El name no puede estar vacío", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_EmptyName() {
        validRequest.setName("   ");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validRequest));
        assertEquals("El name no puede estar vacío", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_NullEmail() {
        validRequest.setEmail(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validRequest));
        assertEquals("Email cannot be empty", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_InvalidEmailFormat() {
        validRequest.setEmail("invalid.email@gmail.com");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validRequest));
        assertTrue(ex.getMessage().contains("El correo debe ser institucional"));
    }

    @Test
    public void testValidateForRegistration_PasswordTooShort() {
        validRequest.setPassword("12345");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validRequest));
        assertEquals("Password must be at least 6 characters", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_NullRole() {
        validRequest.setRole(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validRequest));
        assertEquals("El rol del usuario es obligatorio", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_PlayerRoleMissingPosition() {
        validRequest.setRole(Role.PLAYER);
        validRequest.setPosition(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validRequest));
        assertEquals("La posición es obligatoria para players y capitanes", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_PlayerRoleMissingJerseyNumber() {
        validRequest.setRole(Role.PLAYER);
        validRequest.setPosition("Forward");
        validRequest.setJerseyNumber(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validRequest));
        assertEquals("El número de dorsal es obligatorio y debe ser mayor a 0", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_CaptainRoleInvalidJerseyNumber() {
        validRequest.setRole(Role.CAPTAIN);
        validRequest.setPosition("Midfielder");
        validRequest.setJerseyNumber(0);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validRequest));
        assertEquals("El número de dorsal es obligatorio y debe ser mayor a 0", ex.getMessage());
    }

    @Test
    public void testValidateForRegistration_PlayerRoleSuccess() {
        validRequest.setRole(Role.PLAYER);
        validRequest.setPosition("Goalkeeper");
        validRequest.setJerseyNumber(1);

        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> userValidator.validateForRegistration(validRequest));
    }

    @Test
    public void testValidateForRegistration_DuplicatedEmail() {
        when(userRepository.findByEmail(validRequest.getEmail())).thenReturn(Optional.of(new UserEntity()));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, 
                () -> userValidator.validateForRegistration(validRequest));
        assertEquals("El correo ya se encuentra registrado", ex.getMessage());
    }
}
