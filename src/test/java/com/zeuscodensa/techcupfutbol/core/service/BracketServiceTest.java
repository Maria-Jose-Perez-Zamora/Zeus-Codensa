package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.KnockoutBracket;
import com.zeuscodensa.techcupfutbol.core.model.Registration;
import com.zeuscodensa.techcupfutbol.core.repository.IRegistrationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BracketServiceTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @InjectMocks
    private BracketService llaveService;
    @Test
    public void testGenerarLlaves_Success() {
        Registration i1 = new Registration(); i1.setTeamName("Eq1"); i1.setTournamentName("Liga"); i1.setStatus("APPROVED");
        Registration i2 = new Registration(); i2.setTeamName("Eq2"); i2.setTournamentName("Liga"); i2.setStatus("APPROVED");
        Registration i3 = new Registration(); i3.setTeamName("Eq3"); i3.setTournamentName("Liga"); i3.setStatus("APPROVED");
        Registration i4 = new Registration(); i4.setTeamName("Eq4"); i4.setTournamentName("Liga"); i4.setStatus("APPROVED");
        
        when(registrationRepository.findByTournamentName("Liga")).thenReturn(Arrays.asList(i1, i2, i3, i4));

        List<KnockoutBracket> llaves = llaveService.generarLlaves("Liga", "Cuartos");
        
        assertEquals(2, llaves.size());
        assertEquals("Cuartos", llaves.get(0).getPhase());
    }

    @Test
    public void testGenerarLlaves_OddNumberThrows() {
        Registration i1 = new Registration(); i1.setTeamName("Eq1"); i1.setTournamentName("Liga"); i1.setStatus("APPROVED");
        when(registrationRepository.findByTournamentName("Liga")).thenReturn(Collections.singletonList(i1));

        assertThrows(IllegalArgumentException.class, () -> llaveService.generarLlaves("Liga", "Cuartos"));
    }
}
