package core.service;

import dependencias.dto.InscripcionRequestDTO;
import dependencias.dto.InscripcionResponseDTO;
import core.exception.ResourceNotFoundException;
import core.exception.BusinessRuleException;
import core.model.Inscripcion;
import core.model.Inscripcion;
import dependencias.util.DataStorage;
import core.validator.InscripcionValidator;
import dependencias.mapper.InscripcionMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InscripcionService {

    private static final Logger log = LoggerFactory.getLogger(InscripcionService.class);

    public InscripcionResponseDTO inscribir(InscripcionRequestDTO request) {
        log.debug("Ejecutando validaciones para inscripción del equipo {}", request.getNombreEquipo());
        InscripcionValidator.validateForInscripcion(request);

        Inscripcion nuevaInscripcion = InscripcionMapper.toEntity(request);

        DataStorage.inscripciones.add(nuevaInscripcion);
        log.info("Inscripción creada con ID: {}, estado {}", nuevaInscripcion.getId(), nuevaInscripcion.getEstado());
        return InscripcionMapper.toDTO(nuevaInscripcion);
    }

    public InscripcionResponseDTO actualizarEstado(String id, String nuevoEstado) {
        log.debug("Intentando actualizar estado de la inscripción {} a {}", id, nuevoEstado);
        if (!nuevoEstado.equals("PENDIENTE") && 
            !nuevoEstado.equals("EN_REVISION") && 
            !nuevoEstado.equals("APROBADO") && 
            !nuevoEstado.equals("RECHAZADO")) {
            log.error("Violación transaccional: estado '{}' no permitido", nuevoEstado);
            throw new BusinessRuleException("Estado no valido para inscripcion");
        }

        Optional<Inscripcion> inscripcionOpt = DataStorage.inscripciones.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        if (inscripcionOpt.isEmpty()) {
            log.error("Inscripción no encontrada ID: {}", id);
            throw new ResourceNotFoundException("La inscripcion especificada no existe");
        }

        Inscripcion inscripcion = inscripcionOpt.get();
        inscripcion.setEstado(nuevoEstado);

        log.info("Estado de inscripción ID {} actualizado exitosamente a {}", id, nuevoEstado);
        return InscripcionMapper.toDTO(inscripcion);
    }

    public List<InscripcionResponseDTO> getAll() {
        return DataStorage.inscripciones.stream()
                .map(InscripcionMapper::toDTO)
                .collect(Collectors.toList());
    }
}
