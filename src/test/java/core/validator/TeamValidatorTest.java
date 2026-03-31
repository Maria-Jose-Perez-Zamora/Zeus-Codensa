package core.validator;

import dependencies.dto.TeamRequestDTO;
import dependencies.persistence.entity.TeamEntity;
import dependencies.persistence.entity.UserEntity;
import dependencies.persistence.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamValidator teamValidator;

    @BeforeEach
    public void setup() {
    }

    private TeamRequestDTO validReq() {
        return new TeamRequestDTO("EqA", "E", "C", Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
    }

    @Test
    public void testValid() {
        when(teamRepository.findByTeamName(anyString())).thenReturn(Optional.empty());
        when(teamRepository.existsByPlayersEmail(anyString())).thenReturn(false);
        assertDoesNotThrow(() -> teamValidator.validateForCreation(validReq()));
    }

    @Test
    public void testNombreNullEmpty() {
        TeamRequestDTO req = validReq(); req.setTeamName(null);
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation(req));
        req.setTeamName("  ");
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation(req));
    }

    @Test
    public void testExists() {
        when(teamRepository.findByTeamName("EqA")).thenReturn(Optional.of(new TeamEntity()));
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation(validReq()));
    }

    @Test
    public void testJugadoresNull() {
        TeamRequestDTO req = validReq(); req.setPlayerEmails(null);
        when(teamRepository.findByTeamName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation(req));
    }

    @Test
    public void testJugadoresCount() {
        TeamRequestDTO req = validReq(); req.setPlayerEmails(Arrays.asList("1", "2"));
        when(teamRepository.findByTeamName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation(req));
        
        req.setPlayerEmails(Collections.nCopies(21, "x"));
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation(req));
    }

    @Test
    public void testJugadoresDuplicatedList() {
        TeamRequestDTO req = validReq(); req.setPlayerEmails(Arrays.asList("1", "1", "3", "4", "5", "6", "7"));
        when(teamRepository.findByTeamName(anyString())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation(req));
    }

    @Test
    public void testJugadorYaEnEquipo() {
        when(teamRepository.existsByPlayersEmail("1")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> teamValidator.validateForCreation(validReq()));
    }
}
