package dependencies.mapper;

import dependencies.dto.UserRequestDTO;
import dependencies.dto.UserResponseDTO;
import core.model.Role;
import core.model.Player;
import core.model.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserMapperTest {

    @Test
    public void testToEntity() {
        UserRequestDTO req = new UserRequestDTO();
        req.setName("Juan");
        req.setEmail("j@a.com");
        req.setRole(Role.PLAYER);
        req.setPosition("Delantero");
        User u = UserMapper.toEntity(req);
        assertEquals("Juan", u.getName());
        assertEquals("j@a.com", u.getEmail());
        assertTrue(u instanceof Player);
    }

    @Test
    public void testToDTO() {
        Player u = new Player();
        u.setName("Pedro");
        u.setEmail("p@a.com");
        u.setRole(Role.PLAYER);
        UserResponseDTO dto = UserMapper.toDTO(u);
        assertEquals("Pedro", dto.getName());
        assertEquals("p@a.com", dto.getEmail());
        assertEquals(Role.PLAYER, dto.getRole());
    }
}
