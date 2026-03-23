package dependencies.mapper;

import dependencies.dto.TournamentRequestDTO;
import dependencies.dto.TournamentResponseDTO;
import core.model.Tournament;

public class TournamentMapper {

    public static Tournament toEntity(TournamentRequestDTO dto) {
        Tournament t = new Tournament(dto.getTournamentName());
        t.setFechaInicio(dto.getFechaInicio());
        t.setFechaFin(dto.getFechaFin());
        t.setNumeroEquipos(dto.getNumeroEquipos());
        t.setCostoInscripcion(dto.getCostoInscripcion());
        return t;
    }

    public static TournamentResponseDTO toDTO(Tournament entity) {
        return new TournamentResponseDTO(entity);
    }
}
