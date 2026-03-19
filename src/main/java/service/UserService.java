package service;

import dto.UserRequestDTO;
import dto.UserResponseDTO;
import factory.UserFactory;
import model.User;
import util.DataStorage;
import validator.UserValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    public UserResponseDTO registerUser(UserRequestDTO requestDTO) {
        UserValidator.validateForRegistration(requestDTO);

        User newUser = UserFactory.createUser(requestDTO);

        DataStorage.users.add(newUser);
        return new UserResponseDTO(newUser);
    }

    public List<UserResponseDTO> getAllUsers() {
        return DataStorage.users.stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }
}