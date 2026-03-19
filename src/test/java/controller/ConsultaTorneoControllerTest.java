package controller;

import model.Partido;
import model.TablaPosicion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import service.LlaveService;
import service.TablaService;
import util.DataStorage;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ConsultaTorneoControllerTest {

    @Mock
    private TablaService tablaService;

    @Mock
    private LlaveService llaveService;

    @InjectMocks
    private ConsultaTorneoController consultaTorneoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        DataStorage.clearAll();
    }

    @Test
    public void testGetTabla_Success() {
        when(tablaService.calcularTabla("Liga")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getTabla("Liga");
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    public void testGetLlaves_Success() {
        when(llaveService.generarLlaves("Liga", "Semi")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getLlaves("Liga", "Semi");
        assertEquals(200, res.getStatusCode().value());
    }

    @Test
    public void testGetCalendario_SinPartidos_ReturnsMensaje() {
        ResponseEntity<?> res = consultaTorneoController.getCalendario("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No hay partidos programados"));
    }

    @Test
    public void testGetCalendario_ConPartidos_ReturnsLista() {
        Partido p = new Partido("A", "B", "2026-06-01", "LigaX");
        DataStorage.partidos.add(p);

        ResponseEntity<?> res = consultaTorneoController.getCalendario("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }

    @Test
    public void testGetResultados_ConPartidoFinalizado() {
        Partido p = new Partido("A", "B", "2026-06-01", "LigaX");
        p.setEstado("FINALIZADO");
        p.setMarcadorLocal(2);
        p.setMarcadorVisitante(1);
        DataStorage.partidos.add(p);

        ResponseEntity<?> res = consultaTorneoController.getResultados("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertInstanceOf(List.class, res.getBody());
    }

    @Test
    public void testGetEstadisticas_SinDatos_ReturnsMensaje() {
        when(tablaService.calcularTabla("LigaX")).thenReturn(Collections.emptyList());
        ResponseEntity<?> res = consultaTorneoController.getEstadisticas("LigaX");
        assertEquals(200, res.getStatusCode().value());
        assertTrue(res.getBody().toString().contains("No hay estadísticas"));
    }
}
