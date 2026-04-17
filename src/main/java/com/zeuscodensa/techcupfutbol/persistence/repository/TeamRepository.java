package com.zeuscodensa.techcupfutbol.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import com.zeuscodensa.techcupfutbol.persistence.entity.TeamEntity;

public interface TeamRepository extends JpaRepository<TeamEntity, Long> {
    Optional<TeamEntity> findByTeamName(String teamName);
    boolean existsByPlayersEmail(String email);

    @EntityGraph(attributePaths = "players")
    Page<TeamEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "players")
    Page<TeamEntity> findByTeamNameContainingIgnoreCase(String teamName, Pageable pageable);
}
