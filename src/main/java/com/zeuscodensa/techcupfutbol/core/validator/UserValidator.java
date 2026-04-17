package com.zeuscodensa.techcupfutbol.core.validator;

import org.springframework.stereotype.Component;

import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;

@Component
public class UserValidator {

    private static final String EMAIL_REGEX = "^[a-zA-Z0-9]+\\.[a-zA-Z0-9]+\\-[a-zA-Z]@(mail\\.)?escuelaing\\.edu\\.co$";

    private final IUserRepository userRepository;

    public UserValidator(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void validateForRegistration(User user) {
        validateName(user);
        validateEmail(user);
        validatePassword(user);
        validateRole(user);
        validatePlayerFields(user);
        validateEmailNotRegistered(user);
    }

    private void validateName(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("El name no puede estar vacío");
        }
    }

    private void validateEmail(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }

        if (!user.getEmail().matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("El correo debe ser institucional (ej. nombre.apellido-a@escuelaing.edu.co)");
        }
    }

    private void validatePassword(User user) {
        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
    }

    private void validateRole(User user) {
        if (user.getRole() == null) {
            throw new IllegalArgumentException("El rol del usuario es obligatorio");
        }
    }

    private void validatePlayerFields(User user) {
        if (!(user instanceof Player p)) {
            return;
        }

        if (p.getPosition() == null || p.getPosition().trim().isEmpty()) {
            throw new IllegalArgumentException("La posición es obligatoria para players y capitanes");
        }
        if (p.getJerseyNumber() == null || p.getJerseyNumber() <= 0) {
            throw new IllegalArgumentException("El número de dorsal es obligatorio y debe ser mayor a 0");
        }
    }

    private void validateEmailNotRegistered(User user) {
        boolean exists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (exists) {
            throw new IllegalArgumentException("El correo ya se encuentra registrado");
        }
    }
}
