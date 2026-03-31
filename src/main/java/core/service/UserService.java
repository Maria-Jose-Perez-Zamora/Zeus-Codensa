package core.service;

import dependencies.dto.UserRequestDTO;
import dependencies.dto.UserResponseDTO;
import core.exception.BusinessRuleException;
import core.exception.PersistenceAccessException;
import core.model.User;
import core.validator.UserValidator;
import dependencies.mapper.UserMapper;
import dependencies.persistence.entity.UserEntity;
import dependencies.persistence.mapper.EntityToModelMapper;
import dependencies.persistence.mapper.ModelToEntityMapper;
import dependencies.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserValidator userValidator;

    @Autowired
    public UserService(UserRepository userRepository, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.userValidator = userValidator;
    }

    public UserResponseDTO registerUser(UserRequestDTO requestDTO) {
        log.debug("Validating creation rules for user {}", requestDTO.getEmail());
        userValidator.validateForRegistration(requestDTO);

        User newUser = UserMapper.toEntity(requestDTO);

        try {
            if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
                throw new BusinessRuleException("Ya existe un usuario con ese correo");
            }

            UserEntity saved = userRepository.save(ModelToEntityMapper.toUserEntity(newUser));
            log.info("User created successfully in DB: {} with role {}", newUser.getEmail(), newUser.getRole());
            return UserMapper.toDTO(EntityToModelMapper.toUserModel(saved));
        } catch (DataAccessException ex) {
            throw new PersistenceAccessException("Error al persistir el usuario en base de datos", ex);
        }
    }

    public List<UserResponseDTO> getAllUsers() {
        try {
            return userRepository.findAll().stream()
                    .map(EntityToModelMapper::toUserModel)
                    .map(UserMapper::toDTO)
                    .collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw new PersistenceAccessException("Error al consultar usuarios en base de datos", ex);
        }
    }
}