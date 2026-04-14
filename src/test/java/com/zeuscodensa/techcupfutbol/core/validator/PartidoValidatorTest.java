package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import com.zeuscodensa.techcupfutbol.core.repository.IRegistrationRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PartidoValidatorTest {

    @Mock
    private ITournamentRepository tournamentRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @InjectMocks
    private MatchValidator matchValidator;
    private Match validReq() {
        return new Match("EqA", "EqB", "2026", "T1");
    }

    private void setupValidEnv() {
        Tournament t = new Tournament();
        t.setTournamentName("T1");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));

        when(registrationRepository.existsByTeamNameAndTournamentNameAndStatus("EqA", "T1", "APPROVED")).thenReturn(true);
        when(registrationRepository.existsByTeamNameAndTournamentNameAndStatus("EqB", "T1", "APPROVED")).thenReturn(true);
    }

    @Test
    public void testValid() {
        setupValidEnv();
        assertDoesNotThrow(() -> matchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testLocalNull() {
        Match req = validReq(); req.setHomeTeam(null);
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(req));
    }

    @Test
    public void testVisitanteNull() {
        Match req = validReq(); req.setAwayTeam(null);
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(req));
    }

    @Test
    public void testMismoEquipo() {
        Match req = validReq(); req.setHomeTeam("EqA"); req.setAwayTeam("EqA");
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(req));
    }

    @Test
    public void testTorneoNull() {
        Match req = validReq(); req.setTournamentName(null);
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(req));
    }

    @Test
    public void testFechaNull() {
        Match req = validReq(); req.setMatchDate(null);
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(req));
    }

    @Test
    public void testTorneoNoExiste() {
        when(tournamentRepository.findByTournamentName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testVisitanteNoInscrito() {
        Tournament t = new Tournament();
        t.setTournamentName("T1");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));

        when(registrationRepository.existsByTeamNameAndTournamentNameAndStatus("EqA", "T1", "APPROVED")).thenReturn(true);
        when(registrationRepository.existsByTeamNameAndTournamentNameAndStatus("EqB", "T1", "APPROVED")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testLocalNoInscrito() {
        Tournament t = new Tournament();
        t.setTournamentName("T1");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));

        when(registrationRepository.existsByTeamNameAndTournamentNameAndStatus("EqA", "T1", "APPROVED")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testEstadoNoAprobado() {
        Tournament t = new Tournament();
        t.setTournamentName("T1");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));

        when(registrationRepository.existsByTeamNameAndTournamentNameAndStatus("EqA", "T1", "APPROVED")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(validReq()));
    }
}
