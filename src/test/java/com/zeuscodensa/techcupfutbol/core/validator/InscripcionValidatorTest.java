package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.controller.dto.RegistrationRequestDTO;
import com.zeuscodensa.techcupfutbol.persistence.entity.RegistrationEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.TeamEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.TournamentEntity;
import com.zeuscodensa.techcupfutbol.persistence.repository.RegistrationRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.TeamRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.TournamentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InscripcionValidatorTest {

    @Mock
    private TeamRepository teamRepository;
    
    @Mock
    private TournamentRepository tournamentRepository;
    
    @Mock
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private RegistrationValidator registrationValidator;

    @BeforeEach
    public void setup() {
    }

    private RegistrationRequestDTO validReq() {
        return new RegistrationRequestDTO("EqA", "T1", "url");
    }

    private void setupValidEnv() {
        when(teamRepository.findByTeamName("EqA")).thenReturn(Optional.of(new TeamEntity()));
        TournamentEntity t = new TournamentEntity(); 
        t.setTournamentName("T1");
        t.setStatus("OPEN");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));
        when(registrationRepository.existsByTeamNameAndTournamentName("EqA", "T1")).thenReturn(false);
    }

    @Test
    public void testValid() {
        setupValidEnv();
        assertDoesNotThrow(() -> registrationValidator.validateForInscripcion(validReq()));
    }

    @Test
    public void testNombreEquipoNullEmpty() {
        RegistrationRequestDTO req = validReq(); req.setTeamName(null);
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(req));
        req.setTeamName(" ");
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(req));
    }

    @Test
    public void testTorneoNullEmpty() {
        RegistrationRequestDTO req = validReq(); req.setTournamentName(null);
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(req));
        req.setTournamentName(" ");
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(req));
    }

    @Test
    public void testComprobanteNullEmpty() {
        RegistrationRequestDTO req = validReq(); req.setComprobantePagoUrl(null);
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(req));
        req.setComprobantePagoUrl("");
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(req));
    }

    @Test
    public void testEquipoNoExiste() {
        when(teamRepository.findByTeamName("EqA")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(validReq()));
    }

    @Test
    public void testTorneoNoExisteOEstadoDifferente() {
        when(teamRepository.findByTeamName("EqA")).thenReturn(Optional.of(new TeamEntity()));
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(validReq()));

        TournamentEntity t = new TournamentEntity(); t.setStatus("EN_PROGRESO");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));
        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(validReq()));
    }

    @Test
    public void testYaInscrito() {
        when(teamRepository.findByTeamName("EqA")).thenReturn(Optional.of(new TeamEntity()));
        TournamentEntity t = new TournamentEntity(); 
        t.setTournamentName("T1");
        t.setStatus("OPEN");
        when(tournamentRepository.findByTournamentName("T1")).thenReturn(Optional.of(t));

        when(registrationRepository.existsByTeamNameAndTournamentName("EqA", "T1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> registrationValidator.validateForInscripcion(validReq()));
    }
}
