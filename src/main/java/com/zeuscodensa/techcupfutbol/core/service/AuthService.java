package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    
    private final ITokenService tokenService;
    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthService(ITokenService tokenService, IUserRepository userRepository,
                       PasswordEncoder passwordEncoder, EmailService emailService) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public String login(String email, String password) {
        log.debug("Authentication request processed for {}", email);
        
        if (email == null || password == null) {
            log.warn("Faltan crendeciales para completar el flujo de login");
            throw new IllegalArgumentException("Correo y contraseña obligatorios");
        }

        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty() || !passwordEncoder.matches(password, userOpt.get().getPassword())) {
            log.error("Credenciales invalidas intentadas contra {}", email);
            throw new BusinessRuleException("Credenciales incorrectas");
        }

        User user = userOpt.get();
        String token = tokenService.generateToken(user.getEmail(), user.getRole().name());

        log.info("Autenticacion exitosa: {} (Rol: {})", user.getEmail(), user.getRole().name());

        // Send async login notification — non-blocking
        emailService.sendLoginNotificationEmail(user.getEmail(), user.getName());

        return token;
    }
}
