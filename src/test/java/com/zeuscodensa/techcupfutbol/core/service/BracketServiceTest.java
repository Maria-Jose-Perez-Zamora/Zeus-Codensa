package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.KnockoutBracket;
import com.zeuscodensa.techcupfutbol.persistence.entity.RegistrationEntity;
import com.zeuscodensa.techcupfutbol.persistence.repository.RegistrationRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private RegistrationRepository registrationRepository;

    @InjectMocks
    private BracketService llaveService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGenerarLlaves_Success() {
        RegistrationEntity i1 = new RegistrationEntity(); i1.setTeamName("Eq1"); i1.setTournamentName("Liga"); i1.setStatus("APPROVED");
        RegistrationEntity i2 = new RegistrationEntity(); i2.setTeamName("Eq2"); i2.setTournamentName("Liga"); i2.setStatus("APPROVED");
        RegistrationEntity i3 = new RegistrationEntity(); i3.setTeamName("Eq3"); i3.setTournamentName("Liga"); i3.setStatus("APPROVED");
        RegistrationEntity i4 = new RegistrationEntity(); i4.setTeamName("Eq4"); i4.setTournamentName("Liga"); i4.setStatus("APPROVED");
        
        when(registrationRepository.findByTournamentName("Liga")).thenReturn(Arrays.asList(i1, i2, i3, i4));

        List<KnockoutBracket> llaves = llaveService.generarLlaves("Liga", "Cuartos");
        
        assertEquals(2, llaves.size());
        assertEquals("Cuartos", llaves.get(0).getPhase());
    }

    @Test
    public void testGenerarLlaves_OddNumberThrows() {
        RegistrationEntity i1 = new RegistrationEntity(); i1.setTeamName("Eq1"); i1.setTournamentName("Liga"); i1.setStatus("APPROVED");
        when(registrationRepository.findByTournamentName("Liga")).thenReturn(Collections.singletonList(i1));

        assertThrows(IllegalArgumentException.class, () -> llaveService.generarLlaves("Liga", "Cuartos"));
    }
}
