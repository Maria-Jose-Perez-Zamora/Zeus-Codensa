package core.validator;

import dependencies.dto.RegistrationRequestDTO;
import dependencies.persistence.repository.RegistrationRepository;
import dependencies.persistence.repository.TeamRepository;
import dependencies.persistence.repository.TournamentRepository;
import org.springframework.stereotype.Component;

@Component
public class RegistrationValidator {

    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final RegistrationRepository registrationRepository;

    public RegistrationValidator(TeamRepository teamRepository, TournamentRepository tournamentRepository, RegistrationRepository registrationRepository) {
        this.teamRepository = teamRepository;
        this.tournamentRepository = tournamentRepository;
        this.registrationRepository = registrationRepository;
    }

    public void validateForInscripcion(RegistrationRequestDTO request) {
        if (request.getTeamName() == null || request.getTeamName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de team es obligatorio");
        }
        if (request.getTournamentName() == null || request.getTournamentName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre de tournament es obligatorio");
        }
        if (request.getComprobantePagoUrl() == null || request.getComprobantePagoUrl().trim().isEmpty()) {
            throw new IllegalArgumentException("El comprobante de pago es obligatorio");
        }

        boolean equipoExiste = teamRepository.findByTeamName(request.getTeamName()).isPresent();
        if (!equipoExiste) {
            throw new IllegalArgumentException("El team especificado no existe");
        }

        boolean torneoExiste = tournamentRepository.findByTournamentName(request.getTournamentName())
                .map(t -> t.getStatus().equals("OPEN"))
                .orElse(false);
        if (!torneoExiste) {
            throw new IllegalArgumentException("El tournament no existe o no se encuentra OPEN para registrations");
        }

        boolean estaInscrito = registrationRepository.existsByTeamNameAndTournamentName(request.getTeamName(), request.getTournamentName());
        if (estaInscrito) {
            throw new IllegalArgumentException("El team ya cuenta con un proceso de registration para este tournament");
        }
    }
}
