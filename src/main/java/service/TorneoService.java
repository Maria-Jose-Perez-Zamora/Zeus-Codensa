package service;

import dto.TorneoRequestDTO;
import dto.TorneoResponseDTO;
import model.Torneo;
import util.DataStorage;
import validator.TorneoValidator;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<TorneoResponseDTO> getAllTorneos() {
        return DataStorage.torneos.stream()
                .map(TorneoResponseDTO::new)
                .collect(Collectors.toList());
    }
}
