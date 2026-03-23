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
                j.setPosition(request.getPosition());
                j.setJerseyNumber(request.getJerseyNumber());
                user = j;
                break;
            case CAPTAIN:
                Captain c = new Captain();
                c.setPosition(request.getPosition());
                c.setJerseyNumber(request.getJerseyNumber());
                user = c;
                break;
            case ADMINISTRADOR_SISTEMA:
                user = new AdministradorSistema();
                break;
            case TOURNAMENT_ORGANIZER:
                user = new TournamentOrganizer();
                break;
            case REFEREE:
                user = new Referee();
                break;
            default:
                throw new IllegalArgumentException("Rol no reconocido: " + request.getRole());
        }

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setPhoto(request.getPhoto());
        user.setRole(request.getRole());
        
        if (request.getUserType() != null) {
            try {
                user.setType(UserType.valueOf(request.getUserType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                user.setType(UserType.EXTERNAL);
            }
        } else {
            user.setType(UserType.EXTERNAL);
        }

        return user;
    }
}
