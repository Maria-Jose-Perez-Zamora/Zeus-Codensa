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
import java.util.Collections;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

    @Test
    public void testGetAllTeams_Paginated() {
        Team t1 = new Team("Equipo A");
        Page<Team> page = new PageImpl<>(Collections.singletonList(t1));
        when(teamRepository.findAll(any(Pageable.class))).thenReturn(page);
        
        Page<Team> result = teamService.getAllTeams(null, PageRequest.of(0, 10));
        assertEquals(1, result.getContent().size());
        assertEquals("Equipo A", result.getContent().get(0).getTeamName());
    }

    @Test
    public void testGetAllTeams_PaginatedAndFiltered() {
        Team t1 = new Team("Equipo A");
        Page<Team> page = new PageImpl<>(Collections.singletonList(t1));
        when(teamRepository.findByTeamNameContainingIgnoreCase(eq("Equipo"), any(Pageable.class))).thenReturn(page);
        
        Page<Team> result = teamService.getAllTeams("Equipo", PageRequest.of(0, 10));
        assertEquals(1, result.getContent().size());
        assertEquals("Equipo A", result.getContent().get(0).getTeamName());
    }

    @Test
    public void testGetTeamById() {
        Team t1 = new Team("Equipo A");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(t1));
        
        Team result = teamService.getTeamById(1L);
        assertNotNull(result);
        assertEquals("Equipo A", result.getTeamName());
    }

    @Test
    public void testUpdateTeam() {
        Team existing = new Team("Equipo A");
        existing.setEscudo("old.png");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(teamRepository.save(any(Team.class))).thenReturn(existing);

        Team updated = new Team("Equipo B");
        updated.setEscudo("new.png");
        
        Team result = teamService.updateTeam(1L, updated);
        assertEquals("Equipo B", result.getTeamName());
        assertEquals("new.png", result.getEscudo());
    }

    @Test
    public void testDeleteTeam() {
        Team existing = new Team("Equipo A");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(existing));
        doNothing().when(teamRepository).deleteById(1L);
        
        teamService.deleteTeam(1L);
        verify(teamRepository, times(1)).deleteById(1L);
    }
}
