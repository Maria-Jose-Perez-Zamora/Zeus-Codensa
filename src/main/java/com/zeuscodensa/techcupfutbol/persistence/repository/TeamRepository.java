package com.zeuscodensa.techcupfutbol.persistence.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

import com.zeuscodensa.techcupfutbol.persistence.entity.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    
    @EntityGraph(attributePaths = "players")
    List<TeamEntity> findAll();

    @EntityGraph(attributePaths = "players")
    Optional<TeamEntity> findByTeamName(String teamName);
    boolean existsByPlayersEmail(String email);
}
