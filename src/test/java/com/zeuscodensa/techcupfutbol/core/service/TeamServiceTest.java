package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.controller.dto.TeamRequestDTO;
import com.zeuscodensa.techcupfutbol.controller.dto.TeamResponseDTO;
import com.zeuscodensa.techcupfutbol.core.validator.TeamValidator;
import com.zeuscodensa.techcupfutbol.persistence.entity.TeamEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.UserEntity;
import com.zeuscodensa.techcupfutbol.persistence.repository.TeamRepository;
import com.zeuscodensa.techcupfutbol.persistence.repository.UserRepository;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {
    
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamValidator teamValidator;

    @InjectMocks
    private TeamService teamService;
    
    private List<String> validPlayers;

    @BeforeEach
    public void setUp() {
        validPlayers = Arrays.asList("user.test1-a@escuelaing.edu.co", "user.test2-a@escuelaing.edu.co", 
                "user.test3-a@escuelaing.edu.co", "user.test4-a@escuelaing.edu.co", "user.test5-a@escuelaing.edu.co", 
                "user.test6-a@escuelaing.edu.co", "user.test7-a@escuelaing.edu.co");
    }

    @Test
    public void testCreateTeam_WithPlayers_Success() {
        TeamRequestDTO request = new TeamRequestDTO("Tigres FC", "tigres.png", "Amarillo", validPlayers);
        
        doNothing().when(teamValidator).validateForCreation(request);
        when(teamRepository.findByTeamName("Tigres FC")).thenReturn(Optional.empty());

        for (String email : validPlayers) {
            UserEntity u = new UserEntity();
            u.setEmail(email);
            u.setRole(Role.PLAYER);
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(u));
        }

        TeamEntity savedTeam = new TeamEntity();
        savedTeam.setTeamName("Tigres FC");
        List<UserEntity> teamPlayers = new ArrayList<>();
        for(int i=0; i<7; i++) {
            UserEntity pe = new UserEntity();
            pe.setEmail(validPlayers.get(i));
            teamPlayers.add(pe);
        }
        savedTeam.setPlayers(teamPlayers);
        when(teamRepository.save(any(TeamEntity.class))).thenReturn(savedTeam);

        TeamResponseDTO response = teamService.createTeam(request);
        
        assertNotNull(response);
        assertEquals("Tigres FC", response.getTeamName());
        assertEquals(7, response.getPlayers().size());
        verify(teamRepository, times(1)).save(any(TeamEntity.class));
    }

    @Test
    public void testGetAllTeams() {
        TeamEntity t1 = new TeamEntity();
        t1.setTeamName("Equipo A");
        
        TeamEntity t2 = new TeamEntity();
        t2.setTeamName("Equipo B");

        List<TeamEntity> entityList = new ArrayList<>();
        entityList.add(t1);
        entityList.add(t2);

        when(teamRepository.findAll()).thenReturn(entityList);
        
        List<TeamResponseDTO> teams = teamService.getAllTeams();
        assertEquals(2, teams.size());
        verify(teamRepository, times(1)).findAll();
    }
}
