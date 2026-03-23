package dependencies.mapper;

import dependencies.dto.UserRequestDTO;
import dependencies.dto.UserResponseDTO;
import core.factory.UserFactory;
import core.model.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {
        // Relies on factory for correct subclassing
        return UserFactory.createUser(dto);
    }

    public static UserResponseDTO toDTO(User entity) {
        return new UserResponseDTO(entity);
    }
}
