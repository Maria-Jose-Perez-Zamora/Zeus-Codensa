package service;

import dto.TorneoRequestDTO;
import dto.TorneoResponseDTO;
import model.Torneo;
import util.DataStorage;
import validator.TorneoValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TorneoService {

    public TorneoResponseDTO createTorneo(TorneoRequestDTO request) {
        TorneoValidator.validateForCreation(request);
        
        Torneo t = new Torneo(request.getNombreTorneo());
        t.setFechaInicio(request.getFechaInicio());
        t.setFechaFin(request.getFechaFin());
        t.setNumeroEquipos(request.getNumeroEquipos());
        t.setCostoInscripcion(request.getCostoInscripcion());
        
        DataStorage.torneos.add(t);
        return new TorneoResponseDTO(t);
    }

    /**
     * RF-005: Advanced Tournament Configuration
     * The Organizer configures the tournament (Rules, deadlines, schedules, fields, sanctions)
     */
    public TorneoResponseDTO configurarTorneo(String id, TorneoRequestDTO configInfo) {
        Optional<Torneo> torneoOpt = DataStorage.torneos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();

        if (torneoOpt.isEmpty()) {
            throw new IllegalArgumentException("Torneo no encontrado con ID: " + id);
        }

        Torneo t = torneoOpt.get();
        if (!"BORRADOR".equals(t.getEstado()) && !"ABIERTO".equals(t.getEstado())) {
            throw new IllegalArgumentException("Solo se pueden configurar torneos en estado BORRADOR o ABIERTO");
        }

        // Apply Configurations
        if (configInfo.getReglamento() != null) t.setReglamento(configInfo.getReglamento());
        if (configInfo.getFechaCierreInscripciones() != null) t.setFechaCierreInscripciones(configInfo.getFechaCierreInscripciones());
        if (configInfo.getFechaInicioFaseGrupos() != null) t.setFechaInicioFaseGrupos(configInfo.getFechaInicioFaseGrupos());
        if (configInfo.getHorariosPartidos() != null) t.setHorariosPartidos(configInfo.getHorariosPartidos());
        if (configInfo.getCanchas() != null) t.setCanchas(configInfo.getCanchas());
        if (configInfo.getSanciones() != null) t.setSanciones(configInfo.getSanciones());

        return new TorneoResponseDTO(t);
    }

    public List<TorneoResponseDTO> getAllTorneos() {
        return DataStorage.torneos.stream()
                .map(TorneoResponseDTO::new)
                .collect(Collectors.toList());
    }
}
