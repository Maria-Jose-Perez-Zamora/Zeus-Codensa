package mapper;

import dto.UserRequestDTO;
import dto.UserResponseDTO;
import factory.UserFactory;
import model.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {
        // Relies on factory for correct subclassing
        return UserFactory.createUser(dto);
    }

    public static UserResponseDTO toDTO(User entity) {
        return new UserResponseDTO(entity);
    }
}
