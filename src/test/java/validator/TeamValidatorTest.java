package validator;

import dto.TeamRequestDTO;
import model.Team;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class TeamValidatorTest {

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
    }

    @Test
    public void validateCreation_HappyPath() {
        TeamRequestDTO request = new TeamRequestDTO("Ingenieros FC", "escudo.png", "Azul y Blanco", Collections.emptyList());
        assertDoesNotThrow(() -> TeamValidator.validateForCreation(request));
    }

    @Test
    public void validateCreation_EmptyNameThrows() {
        TeamRequestDTO request = new TeamRequestDTO("   ", "escudo.png", "Azul y Blanco", Collections.emptyList());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(request));
        assertEquals("El nombre del equipo no puede estar vacío", exception.getMessage());
    }

    @Test
    public void validateCreation_DuplicatedNameThrows() {
        DataStorage.teams.add(new Team("Ingenieros FC"));

        TeamRequestDTO request = new TeamRequestDTO("Ingenieros FC", "escudo2.png", "Rojo", Collections.emptyList());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(request));
        assertEquals("El nombre del equipo ya existe", exception.getMessage());
    }
}
