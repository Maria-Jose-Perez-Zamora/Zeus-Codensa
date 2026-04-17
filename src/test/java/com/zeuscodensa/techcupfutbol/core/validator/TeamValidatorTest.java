package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.repository.ITeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamValidatorTest {

    @Mock
    private ITeamRepository teamRepository;

    @InjectMocks
    private TeamValidator teamValidator;
    private List<String> validPlayers() {
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7");
    }

    @Test
    public void testValid_WithPlayers() {
        when(teamRepository.findByTeamName(anyString())).thenReturn(Optional.empty());
        when(teamRepository.existsByPlayersEmail(anyString())).thenReturn(false);
        assertDoesNotThrow(() -> teamValidator.validateForCreation("EqA", validPlayers()));
    }

    @Test
    public void testValid_WithoutPlayers() {
        // Captains can create a team without players and add them later
        when(teamRepository.findByTeamName(anyString())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> teamValidator.validateForCreation("EqB", null));
        assertDoesNotThrow(() -> teamValidator.validateForCreation("EqC", new ArrayList<>()));
    }

    @Test
    public void testNombreNullEmpty() {
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation(null, validPlayers()));
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation("  ", validPlayers()));
    }

    @Test
    public void testExists() {
        when(teamRepository.findByTeamName("EqA")).thenReturn(Optional.of(new Team("EqA")));
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation("EqA", validPlayers()));
    }

    @Test
    public void testJugadoresMaxCount() {
        when(teamRepository.findByTeamName(anyString())).thenReturn(Optional.empty());
        // More than 20 players should still be rejected
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation("EqA", Collections.nCopies(21, "x")));
    }

    @Test
    public void testJugadoresDuplicatedList() {
        when(teamRepository.findByTeamName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation("EqA", Arrays.asList("1", "1", "3", "4", "5", "6", "7")));
    }

    @Test
    public void testJugadorYaEnEquipo() {
        when(teamRepository.findByTeamName(anyString())).thenReturn(Optional.empty());
        when(teamRepository.existsByPlayersEmail(anyString())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation("EqA", validPlayers()));
    }
}
