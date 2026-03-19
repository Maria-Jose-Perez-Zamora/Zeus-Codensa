package service;

import dto.LoginRequestDTO;
import dto.LoginResponseDTO;
import dto.UserResponseDTO;
import model.User;
import util.DataStorage;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {

    public LoginResponseDTO login(LoginRequestDTO request) {
        if (request.getCorreo() == null || request.getContrasena() == null) {
            throw new IllegalArgumentException("Correo y contraseña obligatorios");
        }

        Optional<User> userOpt = DataStorage.users.stream()
                .filter(u -> u.getCorreo().equals(request.getCorreo()) && u.getContrasena().equals(request.getContrasena()))
                .findFirst();

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Credenciales incorrectas");
        }

        User user = userOpt.get();
        // Generar token simulado en Base64
        String tokenStr = user.getCorreo() + ":" + user.getRole().name();
        String token = Base64.getEncoder().encodeToString(tokenStr.getBytes());

        return new LoginResponseDTO(token, new UserResponseDTO(user));
    }
}
