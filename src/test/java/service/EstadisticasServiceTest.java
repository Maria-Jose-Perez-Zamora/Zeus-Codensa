package service;

import model.Partido;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EstadisticasServiceTest {

    private EstadisticasService estadisticasService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        estadisticasService = new EstadisticasService();
    }

    @Test
    public void testGetMaximosGoleadores_Ordenados() {
        Partido p1 = new Partido("A", "B", "hoy", "Liga");
        p1.setEstado("FINALIZADO");
        p1.getGoles().put("jugador1@a.com", 2);
        p1.getGoles().put("jugador2@a.com", 1);

        Partido p2 = new Partido("A", "C", "ayer", "Liga");
        p2.setEstado("FINALIZADO");
        p2.getGoles().put("jugador1@a.com", 1); // jugador1 acumula 3 en total

        DataStorage.partidos.add(p1);
        DataStorage.partidos.add(p2);

        List<Map<String, Object>> result = estadisticasService.getMaximosGoleadores("Liga");
        assertEquals(2, result.size());
        assertEquals("jugador1@a.com", result.get(0).get("correoJugador"));
        assertEquals(3, result.get(0).get("goles"));
    }

    @Test
    public void testGetMaximosGoleadores_SinPartidosFinalizados_RetornaVacio() {
        Partido p = new Partido("A", "B", "hoy", "Liga");
        p.setEstado("PROGRAMADO");
        DataStorage.partidos.add(p);

        List<Map<String, Object>> result = estadisticasService.getMaximosGoleadores("Liga");
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetHistorialEquipo_MarcaResultado() {
        Partido p1 = new Partido("Tigres", "Leones", "hoy", "Liga");
        p1.setEstado("FINALIZADO");
        p1.setMarcadorLocal(2);
        p1.setMarcadorVisitante(0);
        DataStorage.partidos.add(p1);

        List<Map<String, Object>> historial = estadisticasService.getHistorialEquipo("Liga", "Tigres");
        assertEquals(1, historial.size());
        assertEquals("VICTORIA", historial.get(0).get("resultado"));
        assertEquals("LOCAL", historial.get(0).get("condicion"));
    }

    @Test
    public void testGetHistorialEquipo_VisitanteDerrota() {
        Partido p1 = new Partido("Tigres", "Leones", "hoy", "Liga");
        p1.setEstado("FINALIZADO");
        p1.setMarcadorLocal(3);
        p1.setMarcadorVisitante(1);
        DataStorage.partidos.add(p1);

        List<Map<String, Object>> historial = estadisticasService.getHistorialEquipo("Liga", "Leones");
        assertEquals("DERROTA", historial.get(0).get("resultado"));
        assertEquals("VISITANTE", historial.get(0).get("condicion"));
    }
}
