package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.model.UserType;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import com.zeuscodensa.techcupfutbol.core.repository.IEmailNotificationPort;
import com.zeuscodensa.techcupfutbol.security.JwtService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GoogleOAuth2Service {

    private final JwtService jwtService;
    private final IUserRepository userRepository;
    private final IEmailNotificationPort emailPort;

    public GoogleOAuth2Service(JwtService jwtService, IUserRepository userRepository, IEmailNotificationPort emailPort) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.emailPort = emailPort;
    }

    public String authenticateExternalUser(OAuth2User oAuth2User) {
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

        Optional<User> existingUserOpt = userRepository.findByEmail(email);

        User user;
        boolean isNew = false;
        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
        } else {
            user = createExternalUser(email, name, photo);
            isNew = true;
        }

        // Si ya existía pero no tenian estos datos seteados
        boolean modified = false;
        if (user.getType() == null) {
            user.setType(UserType.EXTERNAL);
            modified = true;
        }
        if (user.getRole() == null) {
            user.setRole(Role.PLAYER);
            modified = true;
        }
        if (photo != null && !photo.isBlank() && !photo.equals(user.getPhoto())) {
            user.setPhoto(photo);
            modified = true;
        }
        if (name != null && !name.isBlank() && !name.equals(user.getName())) {
            user.setName(name);
            modified = true;
        }
        
        if(modified) {
            user = userRepository.save(user);
        }

        if (isNew) {
            emailPort.sendAccountCreationEmail(user.getEmail(), user.getName());
        } else {
            emailPort.sendLoginAlertEmail(user.getEmail(), user.getName());
        }

        return jwtService.generateToken(user.getEmail(), user.getRole().name());
    }

    private User createExternalUser(String email, String name, String photo) {
        Player user = new Player();
        user.setEmail(email);
        user.setName(name != null && !name.isBlank() ? name : email);
        user.setPhoto(photo);
        user.setRole(Role.PLAYER);
        user.setType(UserType.EXTERNAL);
        user.setPassword("OAUTH2");
        return userRepository.save(user);
    }
}
