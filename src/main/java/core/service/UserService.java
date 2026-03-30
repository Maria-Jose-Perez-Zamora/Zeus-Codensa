package core.service;

import dependencies.dto.UserRequestDTO;
import dependencies.dto.UserResponseDTO;
import core.exception.BusinessRuleException;
import core.exception.PersistenceAccessException;
import core.model.User;
import dependencies.util.DataStorage;
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

    public UserService() {
        this.userRepository = null;
    }

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO registerUser(UserRequestDTO requestDTO) {
        log.debug("Validating creation rules for user {}", requestDTO.getEmail());
        UserValidator.validateForRegistration(requestDTO);

        User newUser = UserMapper.toEntity(requestDTO);

        if (userRepository != null) {
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

        DataStorage.users.add(newUser);
        log.info("User created successfully: {} with role {}", newUser.getEmail(), newUser.getRole());
        return UserMapper.toDTO(newUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        if (userRepository != null) {
            try {
                return userRepository.findAll().stream()
                        .map(EntityToModelMapper::toUserModel)
                        .map(UserMapper::toDTO)
                        .collect(Collectors.toList());
            } catch (DataAccessException ex) {
                throw new PersistenceAccessException("Error al consultar usuarios en base de datos", ex);
            }
        }

        return DataStorage.users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
}