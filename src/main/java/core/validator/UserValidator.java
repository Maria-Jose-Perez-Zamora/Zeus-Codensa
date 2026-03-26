package core.validator;

import dependencies.dto.UserRequestDTO;
import core.model.Role;
import dependencies.util.DataStorage;

public class UserValidator {

    public static void validateForRegistration(UserRequestDTO request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El name no puede estar vacío");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        // Permitimos números por si hay colisiones en el AD (ej. juan.perez2-a@...)
        String emailRegex = "^[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\-[a-zA-Z]@escuelaing\\.edu\\.co$";
        if (!request.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("El correo debe ser institucional (ej. nombre.apellido-a@escuelaing.edu.co)");
        }
        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        
        if (request.getRole() == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio");
        }

        if (request.getRole() == Role.PLAYER || request.getRole() == Role.CAPTAIN) {
            if (request.getPosition() == null || request.getPosition().trim().isEmpty()) {
                throw new IllegalArgumentException("La posición es obligatoria para players y capitanes");
            }
            if (request.getJerseyNumber() == null || request.getJerseyNumber() <= 0) {
                throw new IllegalArgumentException("El número de dorsal es obligatorio y debe ser mayor a 0");
            }
        }
        
        // Check duplicated email
        boolean exists = DataStorage.users.stream()
                .anyMatch(u -> u.getEmail().equals(request.getEmail()));
        if (exists) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado");
        }
    }
}
