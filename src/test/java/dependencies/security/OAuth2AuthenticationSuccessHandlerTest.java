package dependencies.security;

import core.model.Role;
import core.model.User;
import core.service.GoogleOAuth2Service;
import dependencies.dto.LoginResponseDTO;
import dependencies.dto.UserResponseDTO;
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

        core.model.Player user = new core.model.Player();
        user.setName("Test User");
        user.setEmail("test@gmail.com");
        user.setRole(Role.PLAYER);
        LoginResponseDTO loginResponse = new LoginResponseDTO("mocked_token", new UserResponseDTO(user));
        
        when(googleOAuth2Service.authenticateExternalUser(oAuth2User)).thenReturn(loginResponse);
        when(request.isSecure()).thenReturn(true);

        successHandler.onAuthenticationSuccess(request, response, authentication);

        verify(googleOAuth2Service, times(1)).authenticateExternalUser(oAuth2User);
        verify(response, times(1)).addHeader(eq("Set-Cookie"), anyString());
        verify(response, times(1)).sendRedirect("http://localhost:3000/dashboard");
    }
}
