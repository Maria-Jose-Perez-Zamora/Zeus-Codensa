package com.zeuscodensa.techcupfutbol.config;

import java.util.Arrays;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.zeuscodensa.techcupfutbol.security.JwtAuthFilter;
import com.zeuscodensa.techcupfutbol.security.OAuth2AuthenticationSuccessHandler;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String TOURNAMENT_ORGANIZER = "TOURNAMENT_ORGANIZER";

    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
    }

    // --- FUSIÓN: Aquí añadimos lo que estaba en la carpeta security ---
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    // -----------------------------------------------------------------

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Token requerido o no autorizado\"}");
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        // Health check público — requerido por CD workflows (ZEUS-148, ZEUS-155)
                        .requestMatchers("/health").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/auth/google/**").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/users/register").permitAll()
                        .requestMatchers("/api/tournaments/consulta/**").permitAll()
                        .requestMatchers("/api/tournaments/query/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()

                        // Reglas de acceso por roles del proyecto TechCup
                        .requestMatchers(HttpMethod.POST, "/api/teams/**").hasAuthority("CAPTAIN")
                        .requestMatchers(HttpMethod.POST, "/api/tournaments/**").hasAuthority(TOURNAMENT_ORGANIZER)
                        .requestMatchers(HttpMethod.PUT, "/api/tournaments/**").hasAuthority(TOURNAMENT_ORGANIZER)
                        .requestMatchers(HttpMethod.POST, "/api/matches/**").hasAnyAuthority("REFEREE", TOURNAMENT_ORGANIZER)
                        .requestMatchers(HttpMethod.PUT, "/api/matches/**").hasAnyAuthority("REFEREE", TOURNAMENT_ORGANIZER)
                        .requestMatchers(HttpMethod.POST, "/api/registrations/**").hasAnyAuthority("CAPTAIN", TOURNAMENT_ORGANIZER)
                        .requestMatchers(HttpMethod.PUT, "/api/registrations/**").hasAnyAuthority("ADMINISTRADOR_SISTEMA", TOURNAMENT_ORGANIZER)

                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2AuthenticationSuccessHandler))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                // Local development
                "http://localhost:3000", "http://localhost:4200", "http://localhost:5173",
                "http://127.0.0.1:3000", "http://127.0.0.1:5173",
                // Azure App Service — QA y PROD (actualizar con la URL real del frontend)
                "https://techcup-frontend-qa.azurewebsites.net",
                "https://techcup-frontend.azurewebsites.net"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}