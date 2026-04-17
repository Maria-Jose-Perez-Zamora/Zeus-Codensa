package com.zeuscodensa.techcupfutbol.security;

import com.zeuscodensa.techcupfutbol.core.service.GoogleOAuth2Service;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuth2AuthenticationSuccessHandler.class);

    private final GoogleOAuth2Service googleOAuth2Service;
    private final String oauth2SuccessRedirectUrl;

    public OAuth2AuthenticationSuccessHandler(
            GoogleOAuth2Service googleOAuth2Service,
            @Value("${app.oauth2.success-redirect-url:http://localhost:5173/auth/callback}") String oauth2SuccessRedirectUrl
    ) {
        this.googleOAuth2Service = googleOAuth2Service;
        this.oauth2SuccessRedirectUrl = oauth2SuccessRedirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        try {
            String token = googleOAuth2Service.authenticateExternalUser(oAuth2User);

            // Redirect to frontend callback with JWT as query parameter.
            // HttpOnly cookies cannot be read by JavaScript SPAs.
            String redirectUrl = UriComponentsBuilder.fromUriString(oauth2SuccessRedirectUrl)
                    .queryParam("token", token)
                    .build()
                    .toUriString();

            log.info("OAuth2 login successful for {}, redirecting to callback", email);
            response.sendRedirect(redirectUrl);

        } catch (Exception e) {
            log.error("OAuth2 authentication failed for {}: {}", email, e.getMessage());
            String errorUrl = UriComponentsBuilder.fromUriString(oauth2SuccessRedirectUrl)
                    .queryParam("error", "auth_failed")
                    .build()
                    .toUriString();
            response.sendRedirect(errorUrl);
        }
    }
}
