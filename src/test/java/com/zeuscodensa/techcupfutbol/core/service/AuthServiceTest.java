package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import com.zeuscodensa.techcupfutbol.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtService jwtService;
    
    @Mock
    private IUserRepository userRepository;

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;
    
    @Test
    public void testLogin_Success() {
        com.zeuscodensa.techcupfutbol.core.model.Player u = new com.zeuscodensa.techcupfutbol.core.model.Player();
        u.setEmail("user.test1-a@escuelaing.edu.co");
        u.setPassword("encodedSecret");
        u.setRole(com.zeuscodensa.techcupfutbol.core.model.Role.PLAYER);
        
        when(userRepository.findByEmail("user.test1-a@escuelaing.edu.co")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("secret", "encodedSecret")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("mockToken");

        String responseToken = authService.login("user.test1-a@escuelaing.edu.co", "secret");

        assertNotNull(responseToken);
        assertEquals("mockToken", responseToken);
    }

    @Test
    public void testLogin_InvalidCredentials() {
        com.zeuscodensa.techcupfutbol.core.model.Player u = new com.zeuscodensa.techcupfutbol.core.model.Player();
        u.setEmail("user.test1-a@escuelaing.edu.co");
        u.setPassword("encodedSecret");
        
        when(userRepository.findByEmail("user.test1-a@escuelaing.edu.co")).thenReturn(Optional.of(u));
        when(passwordEncoder.matches("wrong", "encodedSecret")).thenReturn(false);

        assertThrows(BusinessRuleException.class, () -> authService.login("user.test1-a@escuelaing.edu.co", "wrong"));
    }

    @Test
    public void testLogin_UserNotFound_Throws() {
        when(userRepository.findByEmail("ghost@x.com")).thenReturn(Optional.empty());
        assertThrows(BusinessRuleException.class, () -> authService.login("ghost@x.com", "pass"));
    }
}
