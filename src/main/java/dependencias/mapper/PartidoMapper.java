package dependencias.mapper;

import dependencias.dto.PartidoRequestDTO;
import dependencias.dto.PartidoResponseDTO;
import core.model.Partido;

public class PartidoMapper {

    public static Partido toEntity(PartidoRequestDTO dto) {
        return new Partido(
                dto.getEquipoLocal(),
                dto.getEquipoVisitante(),
                dto.getFechaPartido(),
                dto.getNombreTorneo()
        );
    }

    public static PartidoResponseDTO toDTO(Partido entity) {
        return new PartidoResponseDTO(entity);
    }
}
