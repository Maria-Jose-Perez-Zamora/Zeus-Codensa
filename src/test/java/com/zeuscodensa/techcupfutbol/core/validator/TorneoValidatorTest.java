package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import com.zeuscodensa.techcupfutbol.core.repository.ITournamentRepository;
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
    private ITournamentRepository tournamentRepository;

    @InjectMocks
    private TournamentValidator tournamentValidator;

    @BeforeEach
    public void setup() {
    }

    @Test
    public void testValidCreation() {
        Tournament req = new Tournament();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(50.0);
        
        when(tournamentRepository.findByTournamentName(anyString())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNullNombreTorneo() {
        Tournament req = new Tournament();
        req.setTournamentName(null);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testEmptyNombreTorneo() {
        Tournament req = new Tournament();
        req.setTournamentName("   ");
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNullEquipos() {
        Tournament req = new Tournament();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(null);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testInvalidEquipos() {
        Tournament req = new Tournament();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(1);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNullCosto() {
        Tournament req = new Tournament();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(null);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testNegativeCosto() {
        Tournament req = new Tournament();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(-10.0);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }

    @Test
    public void testDuplicateTorneo() {
        Tournament t = new Tournament();
        t.setTournamentName("Valido");

        when(tournamentRepository.findByTournamentName("Valido")).thenReturn(Optional.of(t));

        Tournament req = new Tournament();
        req.setTournamentName("Valido");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(50.0);
        assertThrows(IllegalArgumentException.class, () -> tournamentValidator.validateForCreation(req));
    }
}
