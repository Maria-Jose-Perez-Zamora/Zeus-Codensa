package com.zeuscodensa.techcupfutbol.core.factory;

import com.zeuscodensa.techcupfutbol.controller.dto.UserRequestDTO;
import com.zeuscodensa.techcupfutbol.core.model.*;
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
        req.setPosition("Medio");
        req.setJerseyNumber(10);
        
        User user = UserFactory.createUser(req);
        assertTrue(user instanceof Captain);
        assertEquals("Carlos", user.getName());
        assertEquals("Medio", ((Captain) user).getPosition());
        assertEquals(10, ((Captain) user).getJerseyNumber());
    }

    @Test
    public void testAdministradorCreation() {
        UserRequestDTO req = new UserRequestDTO();
        req.setRole(Role.ADMINISTRADOR_SISTEMA);
        User user = UserFactory.createUser(req);
        assertTrue(user instanceof SystemAdministrator);
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
        assertTrue(user instanceof Referee);
    }
}
