package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.UserType;
import com.zeuscodensa.techcupfutbol.controller.dto.LoginResponseDTO;
import com.zeuscodensa.techcupfutbol.persistence.entity.UserEntity;
import com.zeuscodensa.techcupfutbol.persistence.repository.UserRepository;
import com.zeuscodensa.techcupfutbol.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GoogleOAuth2ServiceTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OAuth2User oAuth2User;

    @InjectMocks
    private GoogleOAuth2Service googleOAuth2Service;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testAuthenticateExternalUser_MissingEmail() {
        when(oAuth2User.getAttribute("email")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                googleOAuth2Service.authenticateExternalUser(oAuth2User));
        assertTrue(ex.getMessage().contains("valido"));
    }

    @Test
    public void testAuthenticateExternalUser_EmailNotVerified() {
        when(oAuth2User.getAttribute("email")).thenReturn("user@gmail.com");
        when(oAuth2User.getAttribute("name")).thenReturn("User");
        when(oAuth2User.getAttribute("picture")).thenReturn("pic_url");
        when(oAuth2User.getAttribute("email_verified")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                googleOAuth2Service.authenticateExternalUser(oAuth2User));
        assertTrue(ex.getMessage().contains("verificado"));
    }

    @Test
    public void testAuthenticateExternalUser_ExistingUser() {
        when(oAuth2User.getAttribute("email")).thenReturn("user@gmail.com");
        when(oAuth2User.getAttribute("name")).thenReturn("User");
        when(oAuth2User.getAttribute("picture")).thenReturn("pic_url");
        when(oAuth2User.getAttribute("email_verified")).thenReturn(true);

        UserEntity existing = new UserEntity();
        existing.setId(1L);
        existing.setEmail("user@gmail.com");
        existing.setName("Old Name");
        existing.setPhoto("old_pic");

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(existing));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(eq("user@gmail.com"), anyString())).thenReturn("mocked_token");

        LoginResponseDTO res = googleOAuth2Service.authenticateExternalUser(oAuth2User);

        assertNotNull(res);
        assertEquals("mocked_token", res.getToken());
        assertEquals("User", existing.getName());
        assertEquals("pic_url", existing.getPhoto());
        assertEquals(UserType.EXTERNAL, existing.getType());
        assertEquals(Role.PLAYER, existing.getRole());
    }

    @Test
    public void testAuthenticateExternalUser_NewUser() {
        when(oAuth2User.getAttribute("email")).thenReturn("new@gmail.com");
        when(oAuth2User.getAttribute("name")).thenReturn(null);
        when(oAuth2User.getAttribute("picture")).thenReturn(null);
        when(oAuth2User.getAttribute("email_verified")).thenReturn(true);

        when(userRepository.findByEmail("new@gmail.com")).thenReturn(Optional.empty());
        
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> {
            UserEntity entity = i.getArgument(0);
            entity.setId(2L);
            return entity;
        });

        when(jwtService.generateToken(eq("new@gmail.com"), anyString())).thenReturn("mocked_token");

        LoginResponseDTO res = googleOAuth2Service.authenticateExternalUser(oAuth2User);

        assertNotNull(res);
        assertEquals("mocked_token", res.getToken());
        verify(userRepository, times(2)).save(any(UserEntity.class)); // 1 for creation, 1 at the end
    }

    @Test
    public void testAuthenticateExternalUser_ExistingUser_NoTypeOrRole() {
        when(oAuth2User.getAttribute("email")).thenReturn("user@gmail.com");
        when(oAuth2User.getAttribute("name")).thenReturn("");
        when(oAuth2User.getAttribute("picture")).thenReturn("");
        when(oAuth2User.getAttribute("email_verified")).thenReturn(true);

        UserEntity existing = new UserEntity();
        existing.setId(3L);
        existing.setEmail("user@gmail.com");
        existing.setType(null);
        existing.setRole(null);

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(existing));
        when(userRepository.save(any(UserEntity.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("mocked_token");

        LoginResponseDTO res = googleOAuth2Service.authenticateExternalUser(oAuth2User);

        assertNotNull(res);
        assertEquals(UserType.EXTERNAL, existing.getType());
        assertEquals(Role.PLAYER, existing.getRole());
    }
}
