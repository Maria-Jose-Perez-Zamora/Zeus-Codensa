package core.service;

import dependencies.dto.LoginRequestDTO;
import dependencies.dto.LoginResponseDTO;
import dependencies.dto.UserResponseDTO;
import core.exception.BusinessRuleException;
import core.model.User;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import dependencies.security.JwtService;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    
    private final JwtService jwtService;
    private final dependencies.persistence.repository.UserRepository userRepository;

    public AuthService(JwtService jwtService, dependencies.persistence.repository.UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.debug("Authentication request processed for {}", request.getEmail());
        
        if (request.getEmail() == null || request.getPassword() == null) {
            log.warn("Faltan crendeciales para completar el flujo de login");
            throw new IllegalArgumentException("Correo y contraseña obligatorios");
        }

        Optional<dependencies.persistence.entity.UserEntity> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(request.getPassword())) {
            log.error("Credenciales invalidas intentadas contra {}", request.getEmail());
            // It mimics a normal 400 Bad Request handled globally instead of 401. So BusinessRuleException fits perfectly.
            throw new BusinessRuleException("Credenciales incorrectas");
        }

        User user = dependencies.persistence.mapper.EntityToModelMapper.toUserModel(userOpt.get());
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        log.info("Autenticacion exitosa: {} (Rol: {})", user.getEmail(), user.getRole().name());
        return new LoginResponseDTO(token, new UserResponseDTO(user));
    }
}
