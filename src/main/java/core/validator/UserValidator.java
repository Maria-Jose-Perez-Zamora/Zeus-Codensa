package core.validator;

import dependencies.dto.UserRequestDTO;
import core.model.Role;
import dependencies.util.DataStorage;

public class UserValidator {

    public static void validateForRegistration(UserRequestDTO request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El name no puede estar vacío");
        }
        if (request.getCorreo() == null || request.getCorreo().trim().isEmpty()) {
            throw new IllegalArgumentException("El correo no puede estar vacío");
        }
        if (request.getContrasena() == null || request.getContrasena().length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
        
        if (request.getRole() == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio");
        }

        if (request.getRole() == Role.PLAYER || request.getRole() == Role.CAPTAIN) {
            if (request.getPosicion() == null || request.getPosicion().trim().isEmpty()) {
                throw new IllegalArgumentException("La posición es obligatoria para players y capitanes");
            }
            if (request.getNumeroDorsal() == null || request.getNumeroDorsal() <= 0) {
                throw new IllegalArgumentException("El número de dorsal es obligatorio y debe ser mayor a 0");
            }
        }
        
        // Check duplicated email
        boolean exists = DataStorage.users.stream()
                .anyMatch(u -> u.getCorreo().equals(request.getCorreo()));
        if (exists) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado");
        }
    }
}
