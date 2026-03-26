package core.service;

import dependencies.dto.LoginRequestDTO;
import dependencies.dto.LoginResponseDTO;
import dependencies.dto.UserResponseDTO;
import core.exception.BusinessRuleException;
import core.model.User;
import dependencies.util.DataStorage;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import dependencies.security.JwtService;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    
    private final JwtService jwtService;

    public AuthService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.debug("Authentication request processed for {}", request.getEmail());
        
        if (request.getEmail() == null || request.getPassword() == null) {
            log.warn("Faltan crendeciales para completar el flujo de login");
            throw new IllegalArgumentException("Correo y contraseña obligatorios");
        }

        Optional<User> userOpt = DataStorage.users.stream()
                .filter(u -> u.getEmail().equals(request.getEmail()) && u.getPassword().equals(request.getPassword()))
                .findFirst();

        if (userOpt.isEmpty()) {
            log.error("Credenciales invalidas intentadas contra {}", request.getEmail());
            // It mimics a normal 400 Bad Request handled globally instead of 401. So BusinessRuleException fits perfectly.
            throw new BusinessRuleException("Credenciales incorrectas");
        }

        User user = userOpt.get();
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        log.info("Autenticacion exitosa: {} (Rol: {})", user.getEmail(), user.getRole().name());
        return new LoginResponseDTO(token, new UserResponseDTO(user));
    }
}
