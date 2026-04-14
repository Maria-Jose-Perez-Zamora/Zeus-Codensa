package com.zeuscodensa.techcupfutbol.config;

import com.zeuscodensa.techcupfutbol.security.JwtAuthFilter;
import com.zeuscodensa.techcupfutbol.security.OAuth2AuthenticationSuccessHandler;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {

    @Test
    public void testPasswordEncoderBean() {
        SecurityConfig config = new SecurityConfig(mock(JwtAuthFilter.class), mock(OAuth2AuthenticationSuccessHandler.class));
        PasswordEncoder encoder = config.passwordEncoder();

        assertNotNull(encoder);
        String rawPassword = "secret123";
        String encoded = encoder.encode(rawPassword);
        assertNotEquals(rawPassword, encoded);
        assertTrue(encoder.matches(rawPassword, encoded));
    }

    @Test
    public void testAuthenticationManagerBean() throws Exception {
        SecurityConfig config = new SecurityConfig(mock(JwtAuthFilter.class), mock(OAuth2AuthenticationSuccessHandler.class));
        AuthenticationConfiguration authConfig = mock(AuthenticationConfiguration.class);
        AuthenticationManager authManager = mock(AuthenticationManager.class);

        when(authConfig.getAuthenticationManager()).thenReturn(authManager);

        AuthenticationManager result = config.authenticationManager(authConfig);
        assertSame(authManager, result);
    }

    @Test
    public void testCorsConfigurationSourceBean() {
        SecurityConfig config = new SecurityConfig(mock(JwtAuthFilter.class), mock(OAuth2AuthenticationSuccessHandler.class));
        CorsConfigurationSource source = config.corsConfigurationSource();
        assertNotNull(source);

        CorsConfiguration cors = source.getCorsConfiguration(new MockHttpServletRequest());
        assertNotNull(cors);
        assertTrue(cors.getAllowedOrigins().contains("http://localhost:3000"));
        assertTrue(cors.getAllowedMethods().contains("POST"));
        assertTrue(cors.getAllowedHeaders().contains("Authorization"));
        assertTrue(cors.getAllowCredentials());
    }
}
