package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.ResourceNotFoundException;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.validator.MatchValidator;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.MatchResponseDTO;
import com.zeuscodensa.techcupfutbol.persistence.entity.MatchEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.UserEntity;
import com.zeuscodensa.techcupfutbol.persistence.repository.MatchRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {

    @Mock private MatchRepository matchRepository;
    @Mock private UserRepository userRepository;
    @Mock private MatchValidator matchValidator;

    @InjectMocks
    private MatchService matchService;

    private MatchEntity sampleEntity;

    @BeforeEach
    public void setUp() {
        sampleEntity = new MatchEntity();
        sampleEntity.setId("m1");
        sampleEntity.setHomeTeam("EqA");
        sampleEntity.setAwayTeam("EqB");
        sampleEntity.setMatchDate("2026-06-01");
        sampleEntity.setTournamentName("Liga");
        sampleEntity.setStatus("SCHEDULED");
    }

    // ── registrarPartido ─────────────────────────────────────────────────────

    @Test
    public void testRegistrarPartido_Success() {
        MatchRequestDTO req = new MatchRequestDTO("EqA", "EqB", "2026-06-01", "Liga");
        doNothing().when(matchValidator).validateForCreation(req);
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(sampleEntity);

        MatchResponseDTO res = matchService.registrarPartido(req);

        assertNotNull(res);
        verify(matchValidator).validateForCreation(req);
        verify(matchRepository).save(any(MatchEntity.class));
    }

    // ── actualizarMarcador ───────────────────────────────────────────────────

    @Test
    public void testActualizarMarcador_Success() {
        when(matchRepository.findById("m1")).thenReturn(Optional.of(sampleEntity));
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(sampleEntity);

        MatchResponseDTO res = matchService.actualizarMarcador("m1", 2, 1);

        assertNotNull(res);
        verify(matchRepository).save(any(MatchEntity.class));
    }

    @Test
    public void testActualizarMarcador_NegativeScore_Throws() {
        assertThrows(BusinessRuleException.class,
                () -> matchService.actualizarMarcador("m1", -1, 0));
        assertThrows(BusinessRuleException.class,
                () -> matchService.actualizarMarcador("m1", 0, -1));
    }

    @Test
    public void testActualizarMarcador_NullScore_Throws() {
        assertThrows(BusinessRuleException.class,
                () -> matchService.actualizarMarcador("m1", null, 1));
        assertThrows(BusinessRuleException.class,
                () -> matchService.actualizarMarcador("m1", 1, null));
    }

    @Test
    public void testActualizarMarcador_MatchNotFound_Throws() {
        when(matchRepository.findById("x")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> matchService.actualizarMarcador("x", 1, 0));
    }

    // ── registrarAlineacion ──────────────────────────────────────────────────

    @Test
    public void testRegistrarAlineacion_Success() {
        when(matchRepository.findById("m1")).thenReturn(Optional.of(sampleEntity));
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(sampleEntity);

        MatchResponseDTO res = matchService.registrarAlineacion("m1", "EqA", Arrays.asList("p1", "p2"));

        assertNotNull(res);
        verify(matchRepository).save(any(MatchEntity.class));
    }

    @Test
    public void testRegistrarAlineacion_EmptyPlayers_Throws() {
        assertThrows(BusinessRuleException.class,
                () -> matchService.registrarAlineacion("m1", "EqA", Collections.emptyList()));
    }

    @Test
    public void testRegistrarAlineacion_NullPlayers_Throws() {
        assertThrows(BusinessRuleException.class,
                () -> matchService.registrarAlineacion("m1", "EqA", null));
    }

    @Test
    public void testRegistrarAlineacion_TeamNotInMatch_Throws() {
        when(matchRepository.findById("m1")).thenReturn(Optional.of(sampleEntity));

        assertThrows(BusinessRuleException.class,
                () -> matchService.registrarAlineacion("m1", "EqX", Arrays.asList("p1")));
    }

    @Test
    public void testRegistrarAlineacion_MatchNotFound_Throws() {
        when(matchRepository.findById("x")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.registrarAlineacion("x", "EqA", Arrays.asList("p1")));
    }

    // ── registrarTarjetas ────────────────────────────────────────────────────

    @Test
    public void testRegistrarTarjetas_WithBothCards() {
        when(matchRepository.findById("m1")).thenReturn(Optional.of(sampleEntity));
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(sampleEntity);

        Map<String, List<String>> amarillas = Map.of("EqA", Arrays.asList("p1"));
        Map<String, List<String>> rojas = Map.of("EqB", Arrays.asList("p2"));

        MatchResponseDTO res = matchService.registrarTarjetas("m1", amarillas, rojas);
        assertNotNull(res);
    }

    @Test
    public void testRegistrarTarjetas_NullCards_Allowed() {
        when(matchRepository.findById("m1")).thenReturn(Optional.of(sampleEntity));
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(sampleEntity);

        // Both null — should not throw
        assertDoesNotThrow(() -> matchService.registrarTarjetas("m1", null, null));
    }

    @Test
    public void testRegistrarTarjetas_MatchNotFound_Throws() {
        when(matchRepository.findById("xx")).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> matchService.registrarTarjetas("xx", null, null));
    }

    // ── getMatchesByReferee ──────────────────────────────────────────────────

    @Test
    public void testGetMatchesByReferee_ReturnsList() {
        sampleEntity.setRefereeEmail("ref@escuelaing.edu.co");
        when(matchRepository.findByRefereeEmail("ref@escuelaing.edu.co"))
                .thenReturn(Collections.singletonList(sampleEntity));

        List<MatchResponseDTO> result = matchService.getMatchesByReferee("ref@escuelaing.edu.co");
        assertEquals(1, result.size());
    }

    @Test
    public void testGetMatchesByReferee_Empty() {
        when(matchRepository.findByRefereeEmail(anyString())).thenReturn(Collections.emptyList());
        List<MatchResponseDTO> result = matchService.getMatchesByReferee("nobody@x.com");
        assertTrue(result.isEmpty());
    }

    // ── asignarArbitro ───────────────────────────────────────────────────────

    @Test
    public void testAsignarArbitro_Success() {
        UserEntity referee = new UserEntity();
        referee.setEmail("ref@escuelaing.edu.co");
        referee.setRole(Role.REFEREE);

        when(userRepository.findByEmail("ref@escuelaing.edu.co")).thenReturn(Optional.of(referee));
        when(matchRepository.findById("m1")).thenReturn(Optional.of(sampleEntity));
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(sampleEntity);

        MatchResponseDTO res = matchService.asignarArbitro("m1", "ref@escuelaing.edu.co");
        assertNotNull(res);
    }

    @Test
    public void testAsignarArbitro_NotReferee_Throws() {
        UserEntity player = new UserEntity();
        player.setRole(Role.PLAYER);

        when(userRepository.findByEmail("player@x.com")).thenReturn(Optional.of(player));
        assertThrows(BusinessRuleException.class,
                () -> matchService.asignarArbitro("m1", "player@x.com"));
    }

    @Test
    public void testAsignarArbitro_UserNotFound_Throws() {
        when(userRepository.findByEmail("ghost@x.com")).thenReturn(Optional.empty());
        assertThrows(BusinessRuleException.class,
                () -> matchService.asignarArbitro("m1", "ghost@x.com"));
    }

    @Test
    public void testAsignarArbitro_MatchNotFound_Throws() {
        UserEntity referee = new UserEntity();
        referee.setRole(Role.REFEREE);
        when(userRepository.findByEmail("ref@x.com")).thenReturn(Optional.of(referee));
        when(matchRepository.findById("bad")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> matchService.asignarArbitro("bad", "ref@x.com"));
    }

    // ── getAll ───────────────────────────────────────────────────────────────

    @Test
    public void testGetAll_ReturnsList() {
        when(matchRepository.findAll()).thenReturn(Arrays.asList(sampleEntity, sampleEntity));
        List<MatchResponseDTO> result = matchService.getAll();
        assertEquals(2, result.size());
    }

    @Test
    public void testGetAll_Empty() {
        when(matchRepository.findAll()).thenReturn(Collections.emptyList());
        List<MatchResponseDTO> result = matchService.getAll();
        assertTrue(result.isEmpty());
    }
}
