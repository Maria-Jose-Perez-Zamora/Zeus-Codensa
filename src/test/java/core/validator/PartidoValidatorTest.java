package core.validator;

import dependencies.dto.MatchRequestDTO;
import dependencies.persistence.entity.RegistrationEntity;
import dependencies.persistence.entity.TournamentEntity;
import dependencies.persistence.repository.RegistrationRepository;
import dependencies.persistence.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PartidoValidatorTest {

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private MatchValidator matchValidator;

    @BeforeEach
    public void setup() {
    }

    private MatchRequestDTO validReq() {
        return new MatchRequestDTO("EqA", "EqB", "2026", "T1");
    }

    private void setupValidEnv() {
        TournamentEntity t = new TournamentEntity();
        t.setTournamentName("T1");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));

        RegistrationEntity i1 = new RegistrationEntity();
        i1.setTeamName("EqA");
        i1.setTournamentName("T1");
        i1.setStatus("APPROVED");

        RegistrationEntity i2 = new RegistrationEntity();
        i2.setTeamName("EqB");
        i2.setTournamentName("T1");
        i2.setStatus("APPROVED");

        when(registrationRepository.findAll()).thenReturn(Arrays.asList(i1, i2));
    }

    @Test
    public void testValid() {
        setupValidEnv();
        assertDoesNotThrow(() -> matchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testLocalNull() {
        MatchRequestDTO req = validReq(); req.setHomeTeam(null);
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(req));
    }

    @Test
    public void testVisitanteNull() {
        MatchRequestDTO req = validReq(); req.setAwayTeam(null);
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(req));
    }

    @Test
    public void testMismoEquipo() {
        MatchRequestDTO req = validReq(); req.setHomeTeam("EqA"); req.setAwayTeam("EqA");
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(req));
    }

    @Test
    public void testTorneoNull() {
        MatchRequestDTO req = validReq(); req.setTournamentName(null);
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(req));
    }

    @Test
    public void testFechaNull() {
        MatchRequestDTO req = validReq(); req.setMatchDate(null);
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(req));
    }

    @Test
    public void testTorneoNoExiste() {
        when(tournamentRepository.findByTournamentName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testVisitanteNoInscrito() {
        TournamentEntity t = new TournamentEntity();
        t.setTournamentName("T1");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));

        RegistrationEntity i1 = new RegistrationEntity();
        i1.setTeamName("EqA");
        i1.setTournamentName("T1");
        i1.setStatus("APPROVED");

        when(registrationRepository.findAll()).thenReturn(Collections.singletonList(i1));

        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testLocalNoInscrito() {
        TournamentEntity t = new TournamentEntity();
        t.setTournamentName("T1");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));

        RegistrationEntity i2 = new RegistrationEntity();
        i2.setTeamName("EqB");
        i2.setTournamentName("T1");
        i2.setStatus("APPROVED");

        when(registrationRepository.findAll()).thenReturn(Collections.singletonList(i2));

        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(validReq()));
    }

    @Test
    public void testEstadoNoAprobado() {
        TournamentEntity t = new TournamentEntity();
        t.setTournamentName("T1");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));

        RegistrationEntity i1 = new RegistrationEntity();
        i1.setTeamName("EqA");
        i1.setTournamentName("T1");
        i1.setStatus("PENDING");

        RegistrationEntity i2 = new RegistrationEntity();
        i2.setTeamName("EqB");
        i2.setTournamentName("T1");
        i2.setStatus("APPROVED");

        when(registrationRepository.findAll()).thenReturn(Arrays.asList(i1, i2));

        assertThrows(IllegalArgumentException.class, () -> matchValidator.validateForCreation(validReq()));
    }
}
