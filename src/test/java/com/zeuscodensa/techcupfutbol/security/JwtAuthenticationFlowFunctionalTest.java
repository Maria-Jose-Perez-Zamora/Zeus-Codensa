package com.zeuscodensa.techcupfutbol.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.zeuscodensa.techcupfutbol.TechcupFutbolApplication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TechcupFutbolApplication.class, properties = {
        "security.jwt.secret=VGhpcyBpcyBhIHZlcnkgc2VjdXJlIGFuZCBsb25nIHNlY3JldCBrZXkgZm9yIEpXVCB2YWxpZGF0aW9u",
        "security.jwt.ttl-ms=3600000"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtAuthenticationFlowFunctionalTest {

    private static final String SECRET = "VGhpcyBpcyBhIHZlcnkgc2VjdXJlIGFuZCBsb25nIHNlY3JldCBrZXkgZm9yIEpXVCB2YWxpZGF0aW9u";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    void flujoCorrectoTokenValido() throws Exception {
        String token = jwtService.generateToken("external.user@google.com", "ADMINISTRADOR_SISTEMA");

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void flujoTokenExpirado() throws Exception {
        String expiredToken = buildExpiredToken("external.user@google.com", "PLAYER");

        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Token JWT expirado"));
    }

    @Test
    void flujoTokenInvalido() throws Exception {
        mockMvc.perform(get("/api/users")
                        .header("Authorization", "Bearer token-invalido"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Token JWT invalido"));
    }

    @Test
    void flujoSinEnviarToken() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Token requerido o no autorizado"));
    }

    private String buildExpiredToken(String username, String role) {
        SecretKey signInKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET));
        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
                .subject(username)
                .claim("role", role)
                .issuedAt(new Date(now.getTime() - 7200000))
                .expiration(new Date(now.getTime() - 3600000))
                .signWith(signInKey)
                .compact();
    }
}
