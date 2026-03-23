package dependencias.mapper;

import dependencias.dto.InscripcionRequestDTO;
import dependencias.dto.InscripcionResponseDTO;
import core.model.Inscripcion;

public class InscripcionMapper {

    public static Inscripcion toEntity(InscripcionRequestDTO dto) {
        return new Inscripcion(
                dto.getNombreEquipo(),
                dto.getNombreTorneo(),
                dto.getComprobantePagoUrl()
        );
    }

    public static InscripcionResponseDTO toDTO(Inscripcion entity) {
        return new InscripcionResponseDTO(entity);
    }
}
