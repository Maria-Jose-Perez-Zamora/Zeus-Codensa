package com.zeuscodensa.techcupfutbol.controller.mapper;

import com.zeuscodensa.techcupfutbol.controller.dto.UserRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import com.zeuscodensa.techcupfutbol.core.factory.UserFactory;
import com.zeuscodensa.techcupfutbol.core.model.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {
        // Relies on factory for correct subclassing
        return UserFactory.createUser(dto);
    }

    public static UserResponseDTO toDTO(User entity) {
        return new UserResponseDTO(entity);
    }
}
