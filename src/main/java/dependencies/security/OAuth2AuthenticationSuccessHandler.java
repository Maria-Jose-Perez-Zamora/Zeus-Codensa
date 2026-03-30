package dependencies.security;

import core.service.GoogleOAuth2Service;
import dependencies.dto.LoginResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final GoogleOAuth2Service googleOAuth2Service;
    private final String oauth2SuccessRedirectUrl;
    private final long jwtTtlMs;

    public OAuth2AuthenticationSuccessHandler(
            GoogleOAuth2Service googleOAuth2Service,
            @Value("${app.oauth2.success-redirect-url:http://localhost:3000/dashboard}") String oauth2SuccessRedirectUrl,
            @Value("${security.jwt.ttl-ms:3600000}") long jwtTtlMs
    ) {
        this.googleOAuth2Service = googleOAuth2Service;
        this.oauth2SuccessRedirectUrl = oauth2SuccessRedirectUrl;
        this.jwtTtlMs = jwtTtlMs;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        LoginResponseDTO loginResponse = googleOAuth2Service.authenticateExternalUser(oAuth2User);

        ResponseCookie authCookie = ResponseCookie.from("AUTH_TOKEN", loginResponse.getToken())
                .httpOnly(true)
                .secure(request.isSecure())
                .path("/")
                .sameSite("Lax")
                .maxAge(Duration.ofMillis(jwtTtlMs))
                .build();

        response.addHeader("Set-Cookie", authCookie.toString());
        response.sendRedirect(oauth2SuccessRedirectUrl);
    }
}
