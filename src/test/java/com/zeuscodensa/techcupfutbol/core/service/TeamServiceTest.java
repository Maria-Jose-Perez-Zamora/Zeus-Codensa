package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.validator.TeamValidator;
import com.zeuscodensa.techcupfutbol.core.repository.ITeamRepository;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
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
    private ITeamRepository teamRepository;

    @Mock
    private IUserRepository userRepository;

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
        Team newTeam = new Team("Tigres FC");
        newTeam.setEscudo("tigres.png");
        newTeam.setColoresUniforme("Amarillo");
        
        doNothing().when(teamValidator).validateForCreation("Tigres FC", validPlayers);
        when(teamRepository.findByTeamName("Tigres FC")).thenReturn(Optional.empty());

        for (String email : validPlayers) {
            Player u = new Player();
            u.setEmail(email);
            u.setRole(Role.PLAYER);
            when(userRepository.findByEmail(email)).thenReturn(Optional.of(u));
        }

        Team savedTeam = new Team("Tigres FC");
        List<com.zeuscodensa.techcupfutbol.core.model.User> teamPlayers = new ArrayList<>();
        for(int i=0; i<7; i++) {
            Player pe = new Player();
            pe.setEmail(validPlayers.get(i));
            teamPlayers.add(pe);
        }
        savedTeam.setPlayers(teamPlayers);
        when(teamRepository.save(any(Team.class))).thenReturn(savedTeam);

        Team response = teamService.createTeam(newTeam, validPlayers);
        
        assertNotNull(response);
        assertEquals("Tigres FC", response.getTeamName());
        assertEquals(7, response.getPlayers().size());
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    public void testGetAllTeams() {
        Team t1 = new Team("Equipo A");
        Team t2 = new Team("Equipo B");

        List<Team> entityList = new ArrayList<>();
        entityList.add(t1);
        entityList.add(t2);

        when(teamRepository.findAll()).thenReturn(entityList);
        
        List<Team> teams = teamService.getAllTeams();
        assertEquals(2, teams.size());
        verify(teamRepository, times(1)).findAll();
    }
}
