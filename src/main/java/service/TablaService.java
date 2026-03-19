package service;

import model.Partido;
import model.TablaPosicion;
import util.DataStorage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TablaService {

    public List<TablaPosicion> calcularTabla(String nombreTorneo) {
        Map<String, TablaPosicion> tabla = new HashMap<>();

        List<Partido> partidos = DataStorage.partidos.stream()
                .filter(p -> p.getNombreTorneo().equals(nombreTorneo) && p.getEstado().equals("FINALIZADO"))
                .collect(Collectors.toList());

        for (Partido p : partidos) {
            tabla.putIfAbsent(p.getEquipoLocal(), new TablaPosicion(p.getEquipoLocal()));
            tabla.putIfAbsent(p.getEquipoVisitante(), new TablaPosicion(p.getEquipoVisitante()));

            tabla.get(p.getEquipoLocal()).registrarPartido(p.getMarcadorLocal(), p.getMarcadorVisitante());
            tabla.get(p.getEquipoVisitante()).registrarPartido(p.getMarcadorVisitante(), p.getMarcadorLocal());
        }

        return tabla.values().stream()
                .sorted((t1, t2) -> {
                    if (t1.getPuntos() != t2.getPuntos()) return Integer.compare(t2.getPuntos(), t1.getPuntos());
                    return Integer.compare(t2.getDiferenciaGoles(), t1.getDiferenciaGoles());
                })
                .collect(Collectors.toList());
    }
}
