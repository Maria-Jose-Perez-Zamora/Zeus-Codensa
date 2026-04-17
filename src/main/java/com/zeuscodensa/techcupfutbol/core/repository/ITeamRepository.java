package com.zeuscodensa.techcupfutbol.core.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zeuscodensa.techcupfutbol.core.model.Team;

public interface ITeamRepository {
    List<Team> findAll();
    Page<Team> findAll(Pageable pageable);
    Page<Team> findByTeamNameContainingIgnoreCase(String name, Pageable pageable);
    Optional<Team> findByTeamName(String teamName);
    Optional<Team> findById(Long id);
    boolean existsByPlayersEmail(String email);
    Team save(Team team);
    void deleteById(Long id);
}
