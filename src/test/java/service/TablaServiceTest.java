package service;

import model.Partido;
import model.TablaPosicion;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TablaServiceTest {

    private TablaService tablaService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        tablaService = new TablaService();
    }

    @Test
    public void testCalcularTabla_Success() {
        Partido p1 = new Partido("Tigres", "Leones", "hoy", "Liga");
        p1.setMarcadorLocal(2); p1.setMarcadorVisitante(1); p1.setEstado("FINALIZADO");
        
        Partido p2 = new Partido("Tigres", "Osos", "ayer", "Liga");
        p2.setMarcadorLocal(1); p2.setMarcadorVisitante(1); p2.setEstado("FINALIZADO");
        
        DataStorage.partidos.add(p1);
        DataStorage.partidos.add(p2);

        List<TablaPosicion> tabla = tablaService.calcularTabla("Liga");
        
        assertEquals(3, tabla.size());
        assertEquals("Tigres", tabla.get(0).getNombreEquipo()); // Tigres win (3) + draw (1) = 4 pts
        assertEquals(4, tabla.get(0).getPuntos());
        assertEquals(2, tabla.get(0).getPartidosJugados());
    }
}
