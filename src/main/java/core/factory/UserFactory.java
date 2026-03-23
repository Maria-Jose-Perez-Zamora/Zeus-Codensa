package core.factory;

import dependencies.dto.UserRequestDTO;
import core.model.*;

public class UserFactory {

    public static User createUser(UserRequestDTO request) {
        if (request.getRole() == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio");
        }

        User user;
        switch (request.getRole()) {
            case PLAYER:
                Player j = new Player();
                j.setPosicion(request.getPosicion());
                j.setNumeroDorsal(request.getNumeroDorsal());
                user = j;
                break;
            case CAPTAIN:
                Capitan c = new Capitan();
                c.setPosicion(request.getPosicion());
                c.setNumeroDorsal(request.getNumeroDorsal());
                user = c;
                break;
            case ADMINISTRADOR_SISTEMA:
                user = new AdministradorSistema();
                break;
            case TOURNAMENT_ORGANIZER:
                user = new TournamentOrganizer();
                break;
            case REFEREE:
                user = new Arbitro();
                break;
            default:
                throw new IllegalArgumentException("Rol no reconocido: " + request.getRole());
        }

        user.setName(request.getName());
        user.setCorreo(request.getCorreo());
        user.setContrasena(request.getContrasena());
        user.setFoto(request.getFoto());
        user.setRole(request.getRole());

        return user;
    }
}
