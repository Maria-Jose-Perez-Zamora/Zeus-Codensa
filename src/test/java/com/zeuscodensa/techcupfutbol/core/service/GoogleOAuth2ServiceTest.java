package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.UserType;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import com.zeuscodensa.techcupfutbol.core.repository.IEmailNotificationPort;
import com.zeuscodensa.techcupfutbol.security.JwtService;
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
    private IUserRepository userRepository;

    @Mock
    private OAuth2User oAuth2User;

    @Mock
    private IEmailNotificationPort emailPort;

    @InjectMocks
    private GoogleOAuth2Service googleOAuth2Service;
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

        Player existing = new Player();
        existing.setEmail("user@gmail.com");
        existing.setName("Old Name");
        existing.setPhoto("old_pic");

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(eq("user@gmail.com"), anyString())).thenReturn("mocked_token");

        String res = googleOAuth2Service.authenticateExternalUser(oAuth2User);

        assertNotNull(res);
        assertEquals("mocked_token", res);
        
        verify(emailPort, times(1)).sendLoginAlertEmail(anyString(), anyString());
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
        
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        when(jwtService.generateToken(eq("new@gmail.com"), anyString())).thenReturn("mocked_token");

        String res = googleOAuth2Service.authenticateExternalUser(oAuth2User);

        assertNotNull(res);
        assertEquals("mocked_token", res);
        verify(userRepository, times(1)).save(any(User.class)); 
        verify(emailPort, times(1)).sendAccountCreationEmail(anyString(), any());
    }

    @Test
    public void testAuthenticateExternalUser_ExistingUser_NoTypeOrRole() {
        when(oAuth2User.getAttribute("email")).thenReturn("user@gmail.com");
        when(oAuth2User.getAttribute("name")).thenReturn("");
        when(oAuth2User.getAttribute("picture")).thenReturn("");
        when(oAuth2User.getAttribute("email_verified")).thenReturn(true);

        Player existing = new Player();
        existing.setEmail("user@gmail.com");

        when(userRepository.findByEmail("user@gmail.com")).thenReturn(Optional.of(existing));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("mocked_token");

        String res = googleOAuth2Service.authenticateExternalUser(oAuth2User);

        assertNotNull(res);
        assertEquals(UserType.EXTERNAL, existing.getType());
        assertEquals(Role.PLAYER, existing.getRole());
    }
}
