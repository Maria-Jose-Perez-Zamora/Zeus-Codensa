package core.validator;

import dependencies.dto.TournamentRequestDTO;
import dependencies.persistence.entity.TournamentEntity;
import dependencies.persistence.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TorneoValidatorTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentValidator tournamentValidator;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testValidCreation() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(50.0);
        
        when(tournamentRepository.findByTournamentName(anyString())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNullNombreTorneo() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName(null);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testEmptyNombreTorneo() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("   ");
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNullEquipos() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(null);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testInvalidEquipos() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(1);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNullCosto() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(null);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNegativeCosto() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(-10.0);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testDuplicateTorneo() {
        TournamentEntity t = new TournamentEntity();
        t.setTournamentName("Valido");

        when(tournamentRepository.findByTournamentName("Valido")).thenReturn(Optional.of(t));

        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(50.0);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }
}
