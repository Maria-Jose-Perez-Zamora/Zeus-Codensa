package core.service;

import dependencias.dto.TorneoRequestDTO;
import dependencias.dto.TorneoResponseDTO;
import core.exception.ResourceNotFoundException;
import core.exception.BusinessRuleException;
import core.model.Torneo;
import core.model.Torneo;
import dependencias.util.DataStorage;
import core.validator.TorneoValidator;
import dependencias.mapper.TorneoMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TorneoService {

    private static final Logger log = LoggerFactory.getLogger(TorneoService.class);

    public TorneoResponseDTO createTorneo(TorneoRequestDTO request) {
        log.debug("Ejecutando validaciones para creación de torneo: {}", request.getNombreTorneo());
        TorneoValidator.validateForCreation(request);
        
        Torneo t = TorneoMapper.toEntity(request);
        
        DataStorage.torneos.add(t);
        log.info("Torneo creado exitosamente en memoria con ID: {}", t.getId());
        return TorneoMapper.toDTO(t);
    }

    public TorneoResponseDTO configurarTorneo(String id, TorneoRequestDTO configInfo) {
        log.debug("Buscando torneo con ID: {} para configuración", id);
        Optional<Torneo> torneoOpt = DataStorage.torneos.stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();

        if (torneoOpt.isEmpty()) {
            log.error("Torneo no encontrado ID: {}", id);
            throw new ResourceNotFoundException("Torneo no encontrado con ID: " + id);
        }

        Torneo t = torneoOpt.get();
        if (!"BORRADOR".equals(t.getEstado()) && !"ABIERTO".equals(t.getEstado())) {
            log.error("Violación temporal: intento de configurar Torneo en estado {}", t.getEstado());
            throw new BusinessRuleException("Solo se pueden configurar torneos en estado BORRADOR o ABIERTO");
        }

        if (configInfo.getReglamento() != null) t.setReglamento(configInfo.getReglamento());
        if (configInfo.getFechaCierreInscripciones() != null) t.setFechaCierreInscripciones(configInfo.getFechaCierreInscripciones());
        if (configInfo.getFechaInicioFaseGrupos() != null) t.setFechaInicioFaseGrupos(configInfo.getFechaInicioFaseGrupos());
        if (configInfo.getHorariosPartidos() != null) t.setHorariosPartidos(configInfo.getHorariosPartidos());
        if (configInfo.getCanchas() != null) t.setCanchas(configInfo.getCanchas());
        if (configInfo.getSanciones() != null) t.setSanciones(configInfo.getSanciones());

        log.info("Torneo ID {} configurado exitosamente", id);
        return TorneoMapper.toDTO(t);
    }

    public List<TorneoResponseDTO> getAllTorneos() {
        log.debug("Recuperando todos los torneos ({})", DataStorage.torneos.size());
        return DataStorage.torneos.stream()
                .map(TorneoMapper::toDTO)
                .collect(Collectors.toList());
    }
}
