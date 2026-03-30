package core.service;

import core.model.Player;
import core.model.Role;
import core.model.User;
import core.model.UserType;
import dependencies.dto.LoginResponseDTO;
import dependencies.dto.UserResponseDTO;
import dependencies.security.JwtService;
import dependencies.util.DataStorage;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoogleOAuth2Service {

    private final JwtService jwtService;

    public GoogleOAuth2Service(JwtService jwtService) {
        this.jwtService = jwtService;
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

        Optional<User> existingUser = DataStorage.users.stream()
                .filter(user -> email.equalsIgnoreCase(user.getEmail()))
                .findFirst();

        User user = existingUser.orElseGet(() -> createExternalUser(email, name, photo));

        if (user.getType() == null) {
            user.setType(UserType.EXTERNAL);
        }
        if (user.getRole() == null) {
            user.setRole(Role.PLAYER);
        }

        if (photo != null && !photo.isBlank()) {
            user.setPhoto(photo);
        }
        if (name != null && !name.isBlank()) {
            user.setName(name);
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponseDTO(token, new UserResponseDTO(user));
    }

    private User createExternalUser(String email, String name, String photo) {
        Player player = new Player();
        player.setEmail(email);
        player.setName(name != null && !name.isBlank() ? name : email);
        player.setPhoto(photo);
        player.setRole(Role.PLAYER);
        player.setType(UserType.EXTERNAL);
        player.setPassword("OAUTH2");
        DataStorage.users.add(player);
        return player;
    }
}
