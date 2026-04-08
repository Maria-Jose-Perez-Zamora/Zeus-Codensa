package com.zeuscodensa.techcupfutbol.core.validator;

import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    private final IUserRepository userRepository;

    public UserValidator(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateForRegistration(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El name no puede estar vacío");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        // Permitimos números por si hay colisiones en el AD (ej. juan.perez2-a@...)
        String emailRegex = "^[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\-[a-zA-Z]@escuelaing\\.edu\\.co$";
        if (!user.getEmail().matches(emailRegex)) {
            throw new IllegalArgumentException("El correo debe ser institucional (ej. nombre.apellido-a@escuelaing.edu.co)");
        }
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        
        if (user.getRole() == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio");
        }

        if (user instanceof Player) {
            Player p = (Player) user;
            if (p.getPosition() == null || p.getPosition().trim().isEmpty()) {
                throw new IllegalArgumentException("La posición es obligatoria para players y capitanes");
            }
            if (p.getJerseyNumber() == null || p.getJerseyNumber() <= 0) {
                throw new IllegalArgumentException("El número de dorsal es obligatorio y debe ser mayor a 0");
            }
        }
        
        // Check duplicated email
        boolean exists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (exists) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado");
        }
    }
}
