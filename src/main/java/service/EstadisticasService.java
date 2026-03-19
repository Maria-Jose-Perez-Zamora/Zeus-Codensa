package service;

import model.Partido;
import model.TablaPosicion;
import util.DataStorage;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EstadisticasService {

    /**
     * RF-008: Máximos goleadores del torneo.
     * Retorna lista ordenada de { correoJugador, goles } descendente.
     */
    public List<Map<String, Object>> getMaximosGoleadores(String nombreTorneo) {
        Map<String, Integer> totales = new HashMap<>();

        DataStorage.partidos.stream()
                .filter(p -> p.getNombreTorneo().equals(nombreTorneo) && "FINALIZADO".equals(p.getEstado()))
                .forEach(p -> p.getGoles().forEach((jugador, g) ->
                        totales.merge(jugador, g, Integer::sum)));

        return totales.entrySet().stream()
                .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                .map(e -> {
                    Map<String, Object> entry = new LinkedHashMap<>();
                    entry.put("correoJugador", e.getKey());
                    entry.put("goles", e.getValue());
                    return entry;
                })
                .collect(Collectors.toList());
    }

    /**
     * RF-008: Historial de partidos de un equipo (todos sus partidos con resultado).
     */
    public List<Map<String, Object>> getHistorialEquipo(String nombreTorneo, String nombreEquipo) {
        return DataStorage.partidos.stream()
                .filter(p -> p.getNombreTorneo().equals(nombreTorneo))
                .filter(p -> p.getEquipoLocal().equals(nombreEquipo) || p.getEquipoVisitante().equals(nombreEquipo))
                .map(p -> {
                    Map<String, Object> info = new LinkedHashMap<>();
                    info.put("id", p.getId());
                    info.put("rival", p.getEquipoLocal().equals(nombreEquipo) ? p.getEquipoVisitante() : p.getEquipoLocal());
                    info.put("condicion", p.getEquipoLocal().equals(nombreEquipo) ? "LOCAL" : "VISITANTE");
                    info.put("marcadorLocal", p.getMarcadorLocal());
                    info.put("marcadorVisitante", p.getMarcadorVisitante());
                    info.put("estado", p.getEstado());
                    info.put("fecha", p.getFechaPartido());

                    String resultado = "PENDIENTE";
                    if ("FINALIZADO".equals(p.getEstado())) {
                        int golesEquipo = p.getEquipoLocal().equals(nombreEquipo) ? p.getMarcadorLocal() : p.getMarcadorVisitante();
                        int golesRival = p.getEquipoLocal().equals(nombreEquipo) ? p.getMarcadorVisitante() : p.getMarcadorLocal();
                        resultado = golesEquipo > golesRival ? "VICTORIA" : golesEquipo == golesRival ? "EMPATE" : "DERROTA";
                    }
                    info.put("resultado", resultado);
                    return info;
                })
                .collect(Collectors.toList());
    }
}
