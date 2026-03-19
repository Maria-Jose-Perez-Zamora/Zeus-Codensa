package validator;

import dto.UserRequestDTO;
import util.DataStorage;
import model.User;
import model.Jugador;
import model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserValidatorTest {

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
    }

    @Test
    public void validateRegistration_HappyPath() {
        UserRequestDTO request = new UserRequestDTO("Juan", "juan@test.com", "123456", "Delantero", 9, null, Role.JUGADOR);
        assertDoesNotThrow(() -> UserValidator.validateForRegistration(request));
    }

    @Test
    public void validateRegistration_NullNameThrows() {
        UserRequestDTO request = new UserRequestDTO(null, "juan@test.com", "123456", "Delantero", 9, null, Role.JUGADOR);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(request));
        assertEquals("El nombre no puede estar vacío", exception.getMessage());
    }

    @Test
    public void validateRegistration_ShortPasswordThrows() {
        UserRequestDTO request = new UserRequestDTO("Juan", "juan@test.com", "123", "Delantero", 9, null, Role.JUGADOR);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(request));
        assertEquals("La contraseña debe tener al menos 6 caracteres", exception.getMessage());
    }

    @Test
    public void validateRegistration_DuplicatedEmailThrows() {
        User existingUser = new Jugador();
        existingUser.setCorreo("juan@test.com");
        existingUser.setRole(Role.JUGADOR);
        DataStorage.users.add(existingUser);

        UserRequestDTO request = new UserRequestDTO("Juan 2", "juan@test.com", "123456", "Delantero", 9, null, Role.JUGADOR);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(request));
        assertEquals("El correo ya se encuentra registrado", exception.getMessage());
    }
}
