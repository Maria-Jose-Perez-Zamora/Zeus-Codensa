package service;

import dto.PartidoRequestDTO;
import dto.PartidoResponseDTO;
import model.Arbitro;
import model.Inscripcion;
import model.Role;
import model.Torneo;
import model.User;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PartidoServiceTest {

    private PartidoService partidoService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        partidoService = new PartidoService();
    }

    private PartidoResponseDTO crearPartido() {
        DataStorage.torneos.add(new Torneo("Liga"));
        Inscripcion i1 = new Inscripcion("A", "Liga", "url"); i1.setEstado("APROBADO");
        Inscripcion i2 = new Inscripcion("B", "Liga", "url"); i2.setEstado("APROBADO");
        DataStorage.inscripciones.add(i1);
        DataStorage.inscripciones.add(i2);
        return partidoService.registrarPartido(new PartidoRequestDTO("A", "B", "hoy", "Liga"));
    }

    @Test
    public void testRegistrarPartido_Success() {
        PartidoResponseDTO res = crearPartido();
        assertNotNull(res);
        assertEquals("PROGRAMADO", res.getEstado());
        assertEquals(1, DataStorage.partidos.size());
    }

    @Test
    public void testActualizarMarcador_Success() {
        PartidoResponseDTO res = crearPartido();
        PartidoResponseDTO result = partidoService.actualizarMarcador(res.getId(), 2, 1);
        assertEquals(2, result.getMarcadorLocal());
        assertEquals("FINALIZADO", result.getEstado());
    }

    @Test
    public void testRegistrarAlineacion_Success() {
        PartidoResponseDTO res = crearPartido();
        List<String> jugadores = Arrays.asList("j1@a.com", "j2@a.com");
        PartidoResponseDTO result = partidoService.registrarAlineacion(res.getId(), "A", jugadores);
        assertNotNull(result.getAlineaciones());
        assertEquals(2, result.getAlineaciones().get("A").size());
    }

    @Test
    public void testRegistrarAlineacion_EquipoNoParticipa_Throws() {
        PartidoResponseDTO res = crearPartido();
        assertThrows(RuntimeException.class,
                () -> partidoService.registrarAlineacion(res.getId(), "OtroEquipo", Arrays.asList("j@a.com")));
    }

    @Test
    public void testRegistrarTarjetas_Success() {
        PartidoResponseDTO res = crearPartido();
        java.util.Map<String, List<String>> amarillas = new java.util.HashMap<>();
        amarillas.put("A", Arrays.asList("Jugador1 (15')", "Jugador2 (45')"));
        amarillas.put("B", Arrays.asList("Jugador3 (80')"));

        java.util.Map<String, List<String>> rojas = new java.util.HashMap<>();
        rojas.put("B", Arrays.asList("Jugador4 (90')"));

        PartidoResponseDTO result = partidoService.registrarTarjetas(res.getId(), amarillas, rojas);
        assertNotNull(result.getTarjetasAmarillas());
        assertEquals(2, result.getTarjetasAmarillas().size());
        assertEquals(1, result.getTarjetasRojas().size());
    }

    @Test
    public void testAsignarArbitro_Success() {
        PartidoResponseDTO res = crearPartido();

        Arbitro arbitro = new Arbitro();
        arbitro.setCorreo("arbitro@test.com");
        arbitro.setRole(Role.ARBITRO);
        DataStorage.users.add(arbitro);

        PartidoResponseDTO result = partidoService.asignarArbitro(res.getId(), "arbitro@test.com");
        assertEquals("arbitro@test.com", result.getCorreoArbitro());
    }

    @Test
    public void testGetPartidosPorArbitro() {
        PartidoResponseDTO res = crearPartido();

        Arbitro arbitro = new Arbitro();
        arbitro.setCorreo("ref@test.com");
        arbitro.setRole(Role.ARBITRO);
        DataStorage.users.add(arbitro);

        partidoService.asignarArbitro(res.getId(), "ref@test.com");
        List<PartidoResponseDTO> misPartidos = partidoService.getPartidosPorArbitro("ref@test.com");
        assertEquals(1, misPartidos.size());
    }
}
