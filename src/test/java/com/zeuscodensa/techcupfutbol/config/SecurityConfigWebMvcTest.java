package com.zeuscodensa.techcupfutbol.config;

import com.zeuscodensa.techcupfutbol.core.repository.IMatchRepository;
import com.zeuscodensa.techcupfutbol.core.service.BracketService;
import com.zeuscodensa.techcupfutbol.core.service.StandingService;
import com.zeuscodensa.techcupfutbol.core.service.StatisticsService;
import com.zeuscodensa.techcupfutbol.security.JwtAuthFilter;
import com.zeuscodensa.techcupfutbol.security.OAuth2AuthenticationSuccessHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = com.zeuscodensa.techcupfutbol.controller.api.TournamentQueryController.class)
@Import(SecurityConfig.class)
public class SecurityConfigWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StandingService tablaService;

    @MockBean
    private BracketService llaveService;

    @MockBean
    private StatisticsService estadisticasService;

    @MockBean
    private IMatchRepository matchRepository;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @MockBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    public void testPublicQueryEndpointIsAccessibleWithoutAuth() throws Exception {
        when(tablaService.calcularTabla(anyString())).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/tournaments/query/Liga/standings"))
                .andExpect(status().isOk());
    }
}
