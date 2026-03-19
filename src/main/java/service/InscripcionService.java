package service;

import dto.InscripcionRequestDTO;
import dto.InscripcionResponseDTO;
import model.Inscripcion;
import util.DataStorage;
import validator.InscripcionValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InscripcionService {

    public InscripcionResponseDTO inscribir(InscripcionRequestDTO request) {
        InscripcionValidator.validateForInscripcion(request);

        Inscripcion nuevaInscripcion = new Inscripcion(
                request.getNombreEquipo(),
                request.getNombreTorneo(),
                request.getComprobantePagoUrl()
        );

        DataStorage.inscripciones.add(nuevaInscripcion);
        return new InscripcionResponseDTO(nuevaInscripcion);
    }

    public InscripcionResponseDTO actualizarEstado(String id, String nuevoEstado) {
        if (!nuevoEstado.equals("PENDIENTE") && 
            !nuevoEstado.equals("EN_REVISION") && 
            !nuevoEstado.equals("APROBADO") && 
            !nuevoEstado.equals("RECHAZADO")) {
            throw new IllegalArgumentException("Estado no valido para inscripcion");
        }

        Optional<Inscripcion> inscripcionOpt = DataStorage.inscripciones.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        if (inscripcionOpt.isEmpty()) {
            throw new IllegalArgumentException("La inscripcion especificada no existe");
        }

        Inscripcion inscripcion = inscripcionOpt.get();
        inscripcion.setEstado(nuevoEstado);

        return new InscripcionResponseDTO(inscripcion);
    }

    public List<InscripcionResponseDTO> getAll() {
        return DataStorage.inscripciones.stream()
                .map(InscripcionResponseDTO::new)
                .collect(Collectors.toList());
    }
}
