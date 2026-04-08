package com.zeuscodensa.techcupfutbol.security;

import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.service.GoogleOAuth2Service;
import com.zeuscodensa.techcupfutbol.controller.dto.LoginResponseDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.UserResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OAuth2AuthenticationSuccessHandlerTest {

    @Mock
    private GoogleOAuth2Service googleOAuth2Service;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    @Mock
    private OAuth2User oAuth2User;

    private OAuth2AuthenticationSuccessHandler successHandler;

    @BeforeEach
    public void setUp() {
        successHandler = new OAuth2AuthenticationSuccessHandler(
                googleOAuth2Service,
                "http://localhost:3000/dashboard",
                3600000L
        );
    }

    @Test
    public void testOnAuthenticationSuccess() throws IOException, ServletException {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);

        when(googleOAuth2Service.authenticateExternalUser(oAuth2User)).thenReturn("mocked_token");
        when(request.isSecure()).thenReturn(true);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(googleOAuth2Service, times(1)).authenticateExternalUser(oAuth2User);
        verify(response, times(1)).addHeader(eq("Set-Cookie"), anyString());
        verify(response, times(1)).sendRedirect("http://localhost:3000/dashboard");
    }
}
