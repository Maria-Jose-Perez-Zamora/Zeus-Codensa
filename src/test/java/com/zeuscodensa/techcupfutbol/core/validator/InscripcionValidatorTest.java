package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import com.zeuscodensa.techcupfutbol.core.repository.IRegistrationRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITeamRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITournamentRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class InscripcionValidatorTest {

    @Mock
    private ITeamRepository teamRepository;
    
    @Mock
    private ITournamentRepository tournamentRepository;
    
    @Mock
    private IRegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationValidator registrationValidator;

    @BeforeEach
    public void setup() {
    }

    private void setupValidEnv() {
        when(teamRepository.findByTeamName("EqA")).thenReturn(Optional.of(new Team("EqA")));
        Tournament t = new Tournament(); 
        t.setTournamentName("T1");
        t.setStatus("OPEN");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));
        when(registrationRepository.existsByTeamNameAndTournamentName("EqA", "T1")).thenReturn(false);
    }

    @Test
    public void testValid() {
        setupValidEnv();
        assertDoesNotThrow(() -> registrationValidator.validateForInscripcion("EqA", "T1", "url"));
    }

    @Test
    public void testNombreEquipoNullEmpty() {
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(null, "T1", "url"));
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(" ", "T1", "url"));
    }

    @Test
    public void testTorneoNullEmpty() {
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion("EqA", null, "url"));
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion("EqA", " ", "url"));
    }

    @Test
    public void testComprobanteNullEmpty() {
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion("EqA", "T1", null));
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion("EqA", "T1", ""));
    }

    @Test
    public void testEquipoNoExiste() {
        when(teamRepository.findByTeamName("EqA")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion("EqA", "T1", "url"));
    }

    @Test
    public void testTorneoNoExisteOEstadoDifferente() {
        when(teamRepository.findByTeamName("EqA")).thenReturn(Optional.of(new Team("EqA")));
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion("EqA", "T1", "url"));

        Tournament t = new Tournament(); t.setStatus("EN_PROGRESO");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion("EqA", "T1", "url"));
    }

    @Test
    public void testYaInscrito() {
        when(teamRepository.findByTeamName("EqA")).thenReturn(Optional.of(new Team("EqA")));
        Tournament t = new Tournament(); 
        t.setTournamentName("T1");
        t.setStatus("OPEN");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));

        when(registrationRepository.existsByTeamNameAndTournamentName("EqA", "T1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion("EqA", "T1", "url"));
    }
}
