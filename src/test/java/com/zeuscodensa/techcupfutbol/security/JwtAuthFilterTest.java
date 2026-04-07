package com.zeuscodensa.techcupfutbol.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    public void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testDoFilterInternal_NoToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);
        when(request.getCookies()).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_HeaderProvidedToken_Valid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer my.valid.token");
        when(jwtService.extractUsername("my.valid.token")).thenReturn("test@test.com");
        when(jwtService.extractRole("my.valid.token")).thenReturn("ROLE_PLAYER");
        when(jwtService.isTokenValid("my.valid.token", "test@test.com")).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_CookieProvidedToken_Valid() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("NotBearer blabla");
        Cookie[] cookies = { new Cookie("random", "value"), new Cookie("AUTH_TOKEN", "my.cookie.token") };
        when(request.getCookies()).thenReturn(cookies);
        when(jwtService.extractUsername("my.cookie.token")).thenReturn("test@test.com");
        when(jwtService.extractRole("my.cookie.token")).thenReturn("ROLE_PLAYER");
        when(jwtService.isTokenValid("my.cookie.token", "test@test.com")).thenReturn(true);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(1)).doFilter(request, response);
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    public void testDoFilterInternal_ExpiredToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer expired.jwt.token");
        when(jwtService.extractUsername("expired.jwt.token")).thenThrow(new ExpiredJwtException(null, null, "Expired"));
        
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    public void testDoFilterInternal_InvalidTokenException() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer bad.jwt.token");
        when(jwtService.extractUsername("bad.jwt.token")).thenThrow(new RuntimeException("Bad token"));

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        when(response.getWriter()).thenReturn(pw);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(filterChain, never()).doFilter(request, response);
    }
}
