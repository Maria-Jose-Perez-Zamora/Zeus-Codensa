package core.factory;

import dependencies.dto.UserRequestDTO;
import core.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserFactoryTest {

    @Test
    public void testNullRole_Throws() {
        UserRequestDTO req = new UserRequestDTO();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> UserFactory.createUser(req));
        assertTrue(thrown.getMessage().contains("El rol del usuario es obligatorio"));
    }

    @Test
    public void testCapitanCreation() {
        UserRequestDTO req = new UserRequestDTO();
        req.setRole(Role.CAPTAIN);
        req.setName("Carlos");
        req.setPosicion("Medio");
        req.setNumeroDorsal(10);
        
        User user = UserFactory.createUser(req);
        assertTrue(user instanceof Capitan);
        assertEquals("Carlos", user.getName());
        assertEquals("Medio", ((Capitan) user).getPosicion());
        assertEquals(10, ((Capitan) user).getNumeroDorsal());
    }

    @Test
    public void testAdministradorCreation() {
        UserRequestDTO req = new UserRequestDTO();
        req.setRole(Role.ADMINISTRADOR_SISTEMA);
        User user = UserFactory.createUser(req);
        assertTrue(user instanceof AdministradorSistema);
    }

    @Test
    public void testOrganizadorCreation() {
        UserRequestDTO req = new UserRequestDTO();
        req.setRole(Role.TOURNAMENT_ORGANIZER);
        User user = UserFactory.createUser(req);
        assertTrue(user instanceof TournamentOrganizer);
    }

    @Test
    public void testArbitroCreation() {
        UserRequestDTO req = new UserRequestDTO();
        req.setRole(Role.REFEREE);
        User user = UserFactory.createUser(req);
        assertTrue(user instanceof Arbitro);
    }
}
