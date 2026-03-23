package core.service;

import core.model.LlaveEliminatoria;
import core.model.Inscripcion;
import core.service.strategy.GeneracionLlavesStrategy;
import core.service.strategy.SorteoAleatorioStrategy;
import dependencias.util.DataStorage;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LlaveService {
    private GeneracionLlavesStrategy strategy;

    public LlaveService() {
        this.strategy = new SorteoAleatorioStrategy();
    }

    public void setStrategy(GeneracionLlavesStrategy strategy) {
        this.strategy = strategy;
    }

    public List<LlaveEliminatoria> generarLlaves(String nombreTorneo, String fase) {
        List<String> equiposAprobados = DataStorage.inscripciones.stream()
                .filter(i -> i.getNombreTorneo().equals(nombreTorneo) && i.getEstado().equals("APROBADO"))
                .map(Inscripcion::getNombreEquipo)
                .collect(Collectors.toList());

        if (equiposAprobados.isEmpty() || equiposAprobados.size() % 2 != 0) {
            throw new IllegalArgumentException("Para generar llaves se requiere una cantidad par de equipos aprobados en el torneo.");
        }
        
        return strategy.generarLlaves(equiposAprobados, fase);
    }
}
