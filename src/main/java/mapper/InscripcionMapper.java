package mapper;

import dto.InscripcionRequestDTO;
import dto.InscripcionResponseDTO;
import model.Inscripcion;

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
