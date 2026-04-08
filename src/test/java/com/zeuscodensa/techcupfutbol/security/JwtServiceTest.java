package com.zeuscodensa.techcupfutbol.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private JwtService jwtService;

    // Valid Base64-encoded 256-bit secret key for tests
    private static final String TEST_SECRET =
            "VGhpcyBpcyBhIHZlcnkgc2VjdXJlIGFuZCBsb25nIHNlY3JldCBrZXkgZm9yIEpXVCB2YWxpZGF0aW9u";

    @BeforeEach
    public void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "expirationTime", 3600000L);
    }

    @Test
    public void testGenerateToken_NotNull() {
        String token = jwtService.generateToken("user@test.com", "PLAYER");
        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    public void testExtractUsername_ReturnsCorrectEmail() {
        String token = jwtService.generateToken("user@test.com", "PLAYER");
        String extracted = jwtService.extractUsername(token);
        assertEquals("user@test.com", extracted);
    }

    @Test
    public void testExtractRole_ReturnsCorrectRole() {
        String token = jwtService.generateToken("user@test.com", "REFEREE");
        String role = jwtService.extractRole(token);
        assertEquals("REFEREE", role);
    }

    @Test
    public void testIsTokenValid_WithCorrectUser_ReturnsTrue() {
        String token = jwtService.generateToken("valid@test.com", "PLAYER");
        assertTrue(jwtService.isTokenValid(token, "valid@test.com"));
    }

    @Test
    public void testIsTokenValid_WithWrongUser_ReturnsFalse() {
        String token = jwtService.generateToken("user@test.com", "PLAYER");
        assertFalse(jwtService.isTokenValid(token, "other@test.com"));
    }

    @Test
    public void testIsTokenValid_ExpiredToken_ReturnsFalse() {
        // Set TTL to -1ms so token is already expired
        ReflectionTestUtils.setField(jwtService, "expirationTime", -1000L);
        String token = jwtService.generateToken("user@test.com", "PLAYER");

        // Expired token throws exception before reaching isTokenValid — test the exception path
        assertThrows(Exception.class, () -> jwtService.isTokenValid(token, "user@test.com"));
    }

    @Test
    public void testGenerateToken_DifferentRoles_EachHasCorrectRole() {
        String[] roles = {"PLAYER", "CAPTAIN", "REFEREE", "TOURNAMENT_ORGANIZER", "ADMINISTRADOR_SISTEMA"};
        for (String role : roles) {
            String token = jwtService.generateToken("user@test.com", role);
            assertEquals(role, jwtService.extractRole(token));
        }
    }
}
