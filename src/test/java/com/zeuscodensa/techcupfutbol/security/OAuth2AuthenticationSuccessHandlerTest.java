package com.zeuscodensa.techcupfutbol.security;

import com.zeuscodensa.techcupfutbol.core.service.GoogleOAuth2Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;

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
                "http://localhost:5173/auth/callback"
        );
    }

    @Test
    public void testOnAuthenticationSuccess_RedirectsWithToken() throws IOException, ServletException {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("user@google.com");
        when(googleOAuth2Service.authenticateExternalUser(oAuth2User)).thenReturn("mocked_jwt_token");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(googleOAuth2Service, times(1)).authenticateExternalUser(oAuth2User);
        // Should redirect with token as query param, NOT set a cookie
        verify(response, times(1)).sendRedirect(contains("token=mocked_jwt_token"));
        verify(response, never()).addHeader(eq("Set-Cookie"), anyString());
    }

    @Test
    public void testOnAuthenticationSuccess_WhenServiceFails_RedirectsWithError() throws IOException, ServletException {
        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttribute("email")).thenReturn("user@google.com");
        when(googleOAuth2Service.authenticateExternalUser(oAuth2User))
                .thenThrow(new RuntimeException("OAuth2 service error"));

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(response, times(1)).sendRedirect(contains("error=auth_failed"));
    }
}
