package com.zeuscodensa.techcupfutbol.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeuscodensa.techcupfutbol.controller.dto.TeamRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TeamControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllTeams_WithoutToken_Returns401() throws Exception {
        mockMvc.perform(get("/api/teams"))
               .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "CAPTAIN")
    public void getAllTeams_WithCaptainToken_Returns200() throws Exception {
        mockMvc.perform(get("/api/teams")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "CAPTAIN")
    public void createTeam_WithCaptainToken_ReturnsBadRequestWhenInvalid() throws Exception {
        TeamRequestDTO request = new TeamRequestDTO(); // Empty invalid request
        mockMvc.perform(post("/api/teams")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isBadRequest());
    }
}
