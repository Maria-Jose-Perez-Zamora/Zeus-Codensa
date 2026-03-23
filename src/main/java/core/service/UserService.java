package core.service;

import dependencies.dto.UserRequestDTO;
import dependencies.dto.UserResponseDTO;
import core.model.User;
import dependencies.util.DataStorage;
import core.validator.UserValidator;
import dependencies.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    public UserResponseDTO registerUser(UserRequestDTO requestDTO) {
        log.debug("Validating creation rules for user {}", requestDTO.getEmail());
        UserValidator.validateForRegistration(requestDTO);

        User newUser = UserMapper.toEntity(requestDTO);

        DataStorage.users.add(newUser);
        log.info("User created successfully: {} with role {}", newUser.getEmail(), newUser.getRole());
        return UserMapper.toDTO(newUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return DataStorage.users.stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }
}