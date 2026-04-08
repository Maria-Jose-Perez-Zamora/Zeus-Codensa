package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.exception.PersistenceAccessException;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.validator.UserValidator;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserService(IUserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public User registerUser(User newUser) {
        log.debug("Validating creation rules for user {}", newUser.getEmail());
        userValidator.validateForRegistration(newUser);

        try {
            if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
                throw new BusinessRuleException("Ya existe un usuario con ese correo");
            }

            User saved = userRepository.save(newUser);
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