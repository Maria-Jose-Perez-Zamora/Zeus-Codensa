package com.zeuscodensa.techcupfutbol.persistence.adapter;

import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.persistence.entity.TeamEntity;
import com.zeuscodensa.techcupfutbol.persistence.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamRepositoryAdapterTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamRepositoryAdapter adapter;

    private TeamEntity entity;

    @BeforeEach
    public void setUp() {
        entity = new TeamEntity();
        entity.setId(1L);
        entity.setTeamName("Equipo A");
    }

    @Test
    public void testFindAllPaginated() {
        Page<TeamEntity> page = new PageImpl<>(Collections.singletonList(entity));
        when(teamRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Team> result = adapter.findAll(PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Equipo A", result.getContent().get(0).getTeamName());
    }

    @Test
    public void testFindByTeamNameContainingIgnoreCase() {
        Page<TeamEntity> page = new PageImpl<>(Collections.singletonList(entity));
        when(teamRepository.findByTeamNameContainingIgnoreCase(anyString(), any(PageRequest.class))).thenReturn(page);

        Page<Team> result = adapter.findByTeamNameContainingIgnoreCase("Equipo", PageRequest.of(0, 10));

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testFindById() {
        when(teamRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Team> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Equipo A", result.get().getTeamName());
    }

    @Test
    public void testDeleteById() {
        doNothing().when(teamRepository).deleteById(1L);

        adapter.deleteById(1L);

        verify(teamRepository, times(1)).deleteById(1L);
    }
}
