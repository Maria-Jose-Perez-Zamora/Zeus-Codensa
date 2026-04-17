package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.PersistenceAccessException;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.validator.UserValidator;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import com.zeuscodensa.techcupfutbol.core.repository.IEmailNotificationPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // NUEVA IMPORTACIÓN
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final IUserRepository userRepository;
    private final UserValidator userValidator;
    private final PasswordEncoder passwordEncoder;
    private final IEmailNotificationPort emailPort; // NUEVA DEPENDENCIA

    @Autowired
    public UserService(IUserRepository userRepository, UserValidator userValidator, PasswordEncoder passwordEncoder, IEmailNotificationPort emailPort) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
        this.passwordEncoder = passwordEncoder;
        this.emailPort = emailPort;
    }

    public User registerUser(User newUser) {
        log.debug("Validating creation rules for user {}", newUser.getEmail());
        userValidator.validateForRegistration(newUser);

        try {
            if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
                throw new BusinessRuleException("Ya existe un usuario con ese correo");
            }

            //  CIFRADO DE CONTRASEÑA ---
            // Se transforma el texto plano en un hash seguro antes de guardar
            String encodedPassword = passwordEncoder.encode(newUser.getPassword());
            newUser.setPassword(encodedPassword);
            // ------------------------------------------------

            User saved = userRepository.save(newUser);
            emailPort.sendAccountCreationEmail(saved.getEmail(), saved.getName());
            log.info("User created successfully in DB: {} with role {}", newUser.getEmail(), newUser.getRole());
            return saved;
        } catch (Exception ex) {
            if (ex instanceof BusinessRuleException) {
                throw (BusinessRuleException) ex;
            }
            throw new PersistenceAccessException("Error al persistir el usuario en base de datos", ex);
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception ex) {
            throw new PersistenceAccessException("Error al consultar usuarios en base de datos", ex);
        }
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}