package service;

import dto.LoginRequestDTO;
import dto.LoginResponseDTO;
import dto.UserResponseDTO;
import exception.BusinessRuleException;
import model.User;
import util.DataStorage;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.debug("Peticion de autenticacion procesada para {:?}", request.getCorreo());
        
        if (request.getCorreo() == null || request.getContrasena() == null) {
            log.warn("Faltan crendeciales para completar el flujo de login");
            throw new IllegalArgumentException("Correo y contraseña obligatorios");
        }

        Optional<User> userOpt = DataStorage.users.stream()
                .filter(u -> u.getCorreo().equals(request.getCorreo()) && u.getContrasena().equals(request.getContrasena()))
                .findFirst();

        if (userOpt.isEmpty()) {
            log.error("Credenciales invalidas intentadas contra {}", request.getCorreo());
            // It mimics a normal 400 Bad Request handled globally instead of 401. So BusinessRuleException fits perfectly.
            throw new BusinessRuleException("Credenciales incorrectas");
        }

        User user = userOpt.get();
        String tokenStr = user.getCorreo() + ":" + user.getRole().name();
        String token = Base64.getEncoder().encodeToString(tokenStr.getBytes());

        log.info("Autenticacion exitosa: {} (Rol: {})", user.getCorreo(), user.getRole().name());
        return new LoginResponseDTO(token, new UserResponseDTO(user));
    }
}
