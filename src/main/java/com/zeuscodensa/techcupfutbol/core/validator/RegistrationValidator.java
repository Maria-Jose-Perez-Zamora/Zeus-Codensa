package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.core.repository.IRegistrationRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITeamRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITournamentRepository;
import org.springframework.stereotype.Component;

@Component
public class RegistrationValidator {

    private final ITeamRepository teamRepository;
    private final ITournamentRepository tournamentRepository;
    private final IRegistrationRepository registrationRepository;

    public RegistrationValidator(ITeamRepository teamRepository, ITournamentRepository tournamentRepository, IRegistrationRepository registrationRepository) {
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
        this.registrationRepository = registrationRepository;
    }

    public void validateForInscripcion(String teamName, String tournamentName, String comprobanteUrl) {
        if (teamName == null || teamName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de team es obligatorio");
        }
        if (tournamentName == null || tournamentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de tournament es obligatorio");
        }
        if (comprobanteUrl == null || comprobanteUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("El comprobante de pago es obligatorio");
        }

        boolean equipoExiste = teamRepository.findByTeamName(teamName).isPresent();
        if (!equipoExiste) {
            throw new IllegalArgumentException("El team especificado no existe");
        }

        boolean torneoExiste = tournamentRepository.findByTournamentName(tournamentName)
                .map(t -> t.getStatus().equals("OPEN"))
                .orElse(false);
        if (!torneoExiste) {
            throw new IllegalArgumentException("El tournament no existe o no se encuentra OPEN para registrations");
        }

        boolean estaInscrito = registrationRepository.existsByTeamNameAndTournamentName(teamName, tournamentName);
        if (estaInscrito) {
            throw new IllegalArgumentException("El team ya cuenta con un proceso de registration para este tournament");
        }
    }
}
