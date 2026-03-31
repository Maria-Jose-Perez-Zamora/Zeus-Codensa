package core.service;

import dependencies.dto.LoginRequestDTO;
import dependencies.dto.LoginResponseDTO;
import dependencies.security.JwtService;
import dependencies.persistence.entity.UserEntity;
import dependencies.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testLogin_Success() {
        UserEntity u = new UserEntity();
        u.setEmail("user.test1-a@escuelaing.edu.co");
        u.setPassword("secret");
        u.setRole(core.model.Role.PLAYER);
        
        when(userRepository.findByEmail("user.test1-a@escuelaing.edu.co")).thenReturn(Optional.of(u));
        when(jwtService.generateToken(anyString(), anyString())).thenReturn("mockToken");

        LoginRequestDTO request = new LoginRequestDTO("user.test1-a@escuelaing.edu.co", "secret");
        LoginResponseDTO response = authService.login(request);

        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
        assertEquals("user.test1-a@escuelaing.edu.co", response.getUser().getEmail());
    }

    @Test
    public void testLogin_InvalidCredentials() {
        UserEntity u = new UserEntity();
        u.setEmail("user.test1-a@escuelaing.edu.co");
        u.setPassword("secret");
        
        when(userRepository.findByEmail("user.test1-a@escuelaing.edu.co")).thenReturn(Optional.of(u));

        LoginRequestDTO request = new LoginRequestDTO("user.test1-a@escuelaing.edu.co", "wrong");
        assertThrows(RuntimeException.class, () -> authService.login(request));
    }

    @Test
    public void testLogin_NullFields() {
        LoginRequestDTO request = new LoginRequestDTO(null, "wrong");
        assertThrows(RuntimeException.class, () -> authService.login(request));
    }
}
