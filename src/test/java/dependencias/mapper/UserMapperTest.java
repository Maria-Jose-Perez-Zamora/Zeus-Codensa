package dependencias.mapper;

import dependencias.dto.UserRequestDTO;
import dependencias.dto.UserResponseDTO;
import core.model.Role;
import core.model.Jugador;
import core.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserMapperTest {

    @Test
    public void testToEntity() {
        UserRequestDTO req = new UserRequestDTO();
        req.setNombre("Juan");
        req.setCorreo("j@a.com");
        req.setRole(Role.JUGADOR);
        req.setPosicion("Delantero");
        User u = UserMapper.toEntity(req);
        assertEquals("Juan", u.getNombre());
        assertEquals("j@a.com", u.getCorreo());
        assertTrue(u instanceof Jugador);
    }

    @Test
    public void testToDTO() {
        Jugador u = new Jugador();
        u.setNombre("Pedro");
        u.setCorreo("p@a.com");
        u.setRole(Role.JUGADOR);
        UserResponseDTO dto = UserMapper.toDTO(u);
        assertEquals("Pedro", dto.getNombre());
        assertEquals("p@a.com", dto.getCorreo());
        assertEquals(Role.JUGADOR, dto.getRole());
    }
}
