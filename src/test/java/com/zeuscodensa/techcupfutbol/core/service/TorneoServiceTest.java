package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import com.zeuscodensa.techcupfutbol.core.validator.TournamentValidator;
import com.zeuscodensa.techcupfutbol.core.repository.ITournamentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TorneoServiceTest {

    @Mock
    private ITournamentRepository tournamentRepository;

    @Mock
    private TournamentValidator tournamentValidator;

    @InjectMocks
    private TournamentService torneoService;
    @Test
    public void testCreateTorneo() {
        Tournament req = new Tournament();
        req.setTournamentName("Liga Verano");
        req.setFechaInicio("2026-06-01");
        req.setFechaFin("2026-08-01");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(100.0);
        
        doNothing().when(tournamentValidator).validateForCreation(req);
        when(tournamentRepository.findByTournamentName("Liga Verano")).thenReturn(Optional.empty());

        Tournament saved = new Tournament();
        saved.setTournamentName("Liga Verano");
        saved.setStatus("DRAFT");
        saved.setNumeroEquipos(8);
        saved.setCostoInscripcion(100.0);
        
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(saved);

        Tournament res = torneoService.createTorneo(req);

        assertNotNull(res);
        assertEquals("Liga Verano", res.getTournamentName());
        assertEquals("DRAFT", res.getStatus());
        verify(tournamentRepository, times(1)).save(any(Tournament.class));
    }

    @Test
    public void testConfigurarTorneo_Success() {
        String currId = "12345";
        Tournament t = new Tournament();
        t.setId(currId);
        t.setTournamentName("Liga Invierno");
        t.setStatus("DRAFT");

        when(tournamentRepository.findById(currId)).thenReturn(Optional.of(t));

        Tournament configInfo = new Tournament();
        configInfo.setRules("Reglas Oficiales");
        configInfo.setFechaCierreInscripciones("2026-05-30");
        configInfo.setFechaInicioFaseGrupos("2026-06-02");
        configInfo.setHorariosPartidos(Arrays.asList("18:00", "20:00"));
        configInfo.setCanchas(Arrays.asList("Cancha 1", "Cancha Central"));
        configInfo.setSanctions("Roja = 2 Fechas");

        Tournament updated = new Tournament();
        updated.setId(currId);
        updated.setTournamentName("Liga Invierno");
        updated.setStatus("DRAFT");
        updated.setRules("Reglas Oficiales");
        updated.setFechaCierreInscripciones("2026-05-30");
        updated.setHorariosPartidos(Arrays.asList("18:00", "20:00"));
        updated.setCanchas(Arrays.asList("Cancha 1", "Cancha Central"));

        when(tournamentRepository.save(any(Tournament.class))).thenReturn(updated);

        Tournament res = torneoService.configurarTorneo(currId, configInfo);

        assertNotNull(res);
        assertEquals("Reglas Oficiales", res.getRules());
        assertEquals("2026-05-30", res.getFechaCierreInscripciones());
        assertEquals(2, res.getHorariosPartidos().size());
        assertEquals("Cancha Central", res.getCanchas().get(1));
    }

    @Test
    public void testConfigurarTorneo_InvalidState_Throws() {
        String currId = "abcde";
        Tournament t = new Tournament();
        t.setId(currId);
        t.setTournamentName("Liga Bloqueada");
        t.setStatus("EN_PROGRESO");

        when(tournamentRepository.findById(currId)).thenReturn(Optional.of(t));

        Tournament configInfo = new Tournament();
        configInfo.setRules("Nuevas reglas");

        RuntimeException thrown = assertThrows(RuntimeException.class, 
            () -> torneoService.configurarTorneo(currId, configInfo));
        assertTrue(thrown.getMessage().contains("Solo se pueden configurar tournaments en status DRAFT o OPEN"));
    }

    @Test
    public void testGetAllTorneos() {
        Tournament t1 = new Tournament();
        t1.setTournamentName("Liga 1");
        
        Tournament t2 = new Tournament();
        t2.setTournamentName("Liga 2");

        List<Tournament> entityList = new ArrayList<>();
        entityList.add(t1);
        entityList.add(t2);

        when(tournamentRepository.findAll()).thenReturn(entityList);

        List<Tournament> res = torneoService.getAllTorneos();
        assertEquals(2, res.size());
    }

    @Test
    public void testGetTorneoById_Success() {
        Tournament t = new Tournament();
        t.setId("abc");
        t.setTournamentName("Liga Test");
        t.setStatus("OPEN");
        when(tournamentRepository.findById("abc")).thenReturn(Optional.of(t));

        Tournament res = torneoService.getTorneoById("abc");
        assertNotNull(res);
        assertEquals("Liga Test", res.getTournamentName());
    }

    @Test
    public void testGetTorneoById_NotFound_Throws() {
        when(tournamentRepository.findById("xxx")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> torneoService.getTorneoById("xxx"));
    }

    @Test
    public void testCreateTorneo_DuplicateName_Throws() {
        Tournament req = new Tournament();
        req.setTournamentName("Liga Verano");
        req.setFechaInicio("2026-06-01");
        req.setFechaFin("2026-08-01");
        req.setNumeroEquipos(8);
        req.setCostoInscripcion(100.0);

        doNothing().when(tournamentValidator).validateForCreation(req);
        Tournament existing = new Tournament();
        existing.setTournamentName("Liga Verano");
        when(tournamentRepository.findByTournamentName("Liga Verano")).thenReturn(Optional.of(existing));

        assertThrows(RuntimeException.class, () -> torneoService.createTorneo(req));
    }

    @Test
    public void testConfigurarTorneo_NotFound_Throws() {
        when(tournamentRepository.findById("missing")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> torneoService.configurarTorneo("missing", new Tournament()));
    }

    @Test
    public void testConfigurarTorneo_OpenStatus_Success() {
        Tournament t = new Tournament();
        t.setId("open1");
        t.setStatus("OPEN");
        when(tournamentRepository.findById("open1")).thenReturn(Optional.of(t));
        Tournament saved = new Tournament();
        saved.setId("open1");
        saved.setStatus("OPEN");
        when(tournamentRepository.save(any(Tournament.class))).thenReturn(saved);

        Tournament config = new Tournament();
        config.setRules("New rules");
        assertDoesNotThrow(() -> torneoService.configurarTorneo("open1", config));
    }
}
