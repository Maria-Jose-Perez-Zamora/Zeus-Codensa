package core.service;

import core.model.Role;
import core.model.User;
import core.model.UserType;
import dependencies.dto.LoginResponseDTO;
import dependencies.dto.UserResponseDTO;
import dependencies.security.JwtService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoogleOAuth2Service {

    private final JwtService jwtService;
    private final dependencies.persistence.repository.UserRepository userRepository;

    public GoogleOAuth2Service(JwtService jwtService, dependencies.persistence.repository.UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public LoginResponseDTO authenticateExternalUser(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String photo = oAuth2User.getAttribute("picture");
        Boolean emailVerified = oAuth2User.getAttribute("email_verified");

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Google OAuth2 no retorno un email valido");
        }
        if (Boolean.FALSE.equals(emailVerified)) {
            throw new IllegalArgumentException("El email de Google no esta verificado");
        }

        Optional<dependencies.persistence.entity.UserEntity> existingUserOpt = userRepository.findByEmail(email);

        dependencies.persistence.entity.UserEntity entity;
        if (existingUserOpt.isPresent()) {
            entity = existingUserOpt.get();
        } else {
            entity = createExternalUser(email, name, photo);
        }

        if (entity.getType() == null) {
            entity.setType(UserType.EXTERNAL);
        }
        if (entity.getRole() == null) {
            entity.setRole(Role.PLAYER);
        }

        if (photo != null && !photo.isBlank()) {
            entity.setPhoto(photo);
        }
        if (name != null && !name.isBlank()) {
            entity.setName(name);
        }
        
        entity = userRepository.save(entity);

        User user = dependencies.persistence.mapper.EntityToModelMapper.toUserModel(entity);
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponseDTO(token, new UserResponseDTO(user));
    }

    private dependencies.persistence.entity.UserEntity createExternalUser(String email, String name, String photo) {
        dependencies.persistence.entity.UserEntity entity = new dependencies.persistence.entity.UserEntity();
        entity.setEmail(email);
        entity.setName(name != null && !name.isBlank() ? name : email);
        entity.setPhoto(photo);
        entity.setRole(Role.PLAYER);
        entity.setType(UserType.EXTERNAL);
        entity.setPassword("OAUTH2");
        return userRepository.save(entity);
    }
}
