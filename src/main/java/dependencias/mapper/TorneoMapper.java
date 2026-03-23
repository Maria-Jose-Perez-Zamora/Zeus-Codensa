package dependencias.mapper;

import dependencias.dto.TorneoRequestDTO;
import dependencias.dto.TorneoResponseDTO;
import core.model.Torneo;

public class TorneoMapper {

    public static Torneo toEntity(TorneoRequestDTO dto) {
        Torneo t = new Torneo(dto.getNombreTorneo());
        t.setFechaInicio(dto.getFechaInicio());
        t.setFechaFin(dto.getFechaFin());
        t.setNumeroEquipos(dto.getNumeroEquipos());
        t.setCostoInscripcion(dto.getCostoInscripcion());
        return t;
    }

    public static TorneoResponseDTO toDTO(Torneo entity) {
        return new TorneoResponseDTO(entity);
    }
}
