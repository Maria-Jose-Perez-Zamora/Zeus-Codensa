package com.zeuscodensa.techcupfutbol.core.repository;

import java.util.List;
import java.util.Optional;

import com.zeuscodensa.techcupfutbol.core.model.Team;

public interface ITeamRepository {
    List<Team> findAll();
    Optional<Team> findByTeamName(String teamName);
    boolean existsByPlayersEmail(String email);
    Team save(Team team);
}
