package validator;

import dto.TeamRequestDTO;
import model.Team;
import model.Jugador;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeamValidatorTest {

    private List<String> validPlayers;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        validPlayers = Arrays.asList("1@a.com", "2@a.com", "3@a.com", "4@a.com", "5@a.com", "6@a.com", "7@a.com");
    }

    @Test
    public void validateCreation_HappyPath() {
        TeamRequestDTO request = new TeamRequestDTO("Ingenieros FC", "escudo.png", "Azul y Blanco", validPlayers);
        assertDoesNotThrow(() -> TeamValidator.validateForCreation(request));
    }

    @Test
    public void validateCreation_NotEnoughPlayersThrows() {
        TeamRequestDTO request = new TeamRequestDTO("Ingenieros FC", "escudo.png", "Azul", Arrays.asList("1@a.com"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(request));
        assertEquals("Un equipo debe tener entre 7 y 20 jugadores inscritos inicialmente", exception.getMessage());
    }

    @Test
    public void validateCreation_DuplicatedEmailsThrows() {
        TeamRequestDTO request = new TeamRequestDTO("Ingenieros FC", "escudo.png", "Azul", Arrays.asList("1@a.com", "1@a.com", "3@a.com", "4@a.com", "5@a.com", "6@a.com", "7@a.com"));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(request));
        assertEquals("Existen correos duplicados en la solicitud del equipo", exception.getMessage());
    }

    @Test
    public void validateCreation_AlreadyInOtherTeamThrows() {
        Team t = new Team("Otro FC");
        Jugador j = new Jugador(); j.setCorreo("1@a.com");
        t.getJugadores().add(j);
        DataStorage.teams.add(t);

        TeamRequestDTO request = new TeamRequestDTO("Ingenieros FC", "escudo.png", "Azul", validPlayers);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> TeamValidator.validateForCreation(request));
        assertEquals("El jugador 1@a.com ya pertenece a otro equipo", exception.getMessage());
    }
}
