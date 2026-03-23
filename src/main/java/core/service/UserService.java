package core.service;

import dependencias.dto.UserRequestDTO;
import dependencias.dto.UserResponseDTO;
import core.model.User;
import dependencias.util.DataStorage;
import core.validator.UserValidator;
import dependencias.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserResponseDTO registerUser(UserRequestDTO requestDTO) {
        log.debug("Validando reglas de creacion para usuario {}", requestDTO.getCorreo());
        UserValidator.validateForRegistration(requestDTO);

        User newUser = UserMapper.toEntity(requestDTO);

        DataStorage.users.add(newUser);
        log.info("Usuario creado exitosamente: {} con rol {}", newUser.getCorreo(), newUser.getRole());
        return UserMapper.toDTO(newUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return DataStorage.users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
}