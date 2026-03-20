package validator;

import dto.UserRequestDTO;
import model.Role;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorTest {

    @BeforeEach
    public void setup() {
        DataStorage.clearAll();
    }

    private UserRequestDTO validAdmin() {
        UserRequestDTO req = new UserRequestDTO();
        req.setNombre("A"); req.setCorreo("a@x.com"); req.setContrasena("123456"); req.setRole(Role.ADMINISTRADOR_SISTEMA);
        return req;
    }

    private UserRequestDTO validJugador() {
        UserRequestDTO req = validAdmin();
        req.setRole(Role.JUGADOR); req.setPosicion("DEL"); req.setNumeroDorsal(9);
        return req;
    }

    @Test
    public void testValid() {
        assertDoesNotThrow(() -> UserValidator.validateForRegistration(validAdmin()));
        assertDoesNotThrow(() -> UserValidator.validateForRegistration(validJugador()));
    }

    @Test
    public void testNombreNullEmpty() {
        UserRequestDTO req = validAdmin(); req.setNombre(null);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
        req.setNombre(" ");
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
    }

    @Test
    public void testCorreoNullEmpty() {
        UserRequestDTO req = validAdmin(); req.setCorreo(null);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
        req.setCorreo("");
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
    }

    @Test
    public void testContrasenaNullShort() {
        UserRequestDTO req = validAdmin(); req.setContrasena(null);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
        req.setContrasena("12345");
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
    }

    @Test
    public void testRoleNull() {
        UserRequestDTO req = validAdmin(); req.setRole(null);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
    }

    @Test
    public void testJugadorPosicionNullEmpty() {
        UserRequestDTO req = validJugador(); req.setPosicion(null);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
        req.setPosicion("   ");
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
    }

    @Test
    public void testJugadorDorsalNullZero() {
        UserRequestDTO req = validJugador(); req.setNumeroDorsal(null);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
        req.setNumeroDorsal(0);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
        req.setNumeroDorsal(-1);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
        
        req.setRole(Role.CAPITAN); req.setNumeroDorsal(0);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(req));
    }

    @Test
    public void testDuplicatedCorreo() {
        model.Jugador j = new model.Jugador();
        j.setCorreo("a@x.com");
        DataStorage.users.add(j);
        assertThrows(IllegalArgumentException.class, () -> UserValidator.validateForRegistration(validAdmin()));
    }
}
