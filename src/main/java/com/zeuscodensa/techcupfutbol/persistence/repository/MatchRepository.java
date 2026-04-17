package com.zeuscodensa.techcupfutbol.persistence.repository;

import com.zeuscodensa.techcupfutbol.persistence.entity.MatchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface MatchRepository extends JpaRepository<MatchEntity, String> {
    List<MatchEntity> findByTournamentName(String tournamentName);
    List<MatchEntity> findByRefereeEmail(String refereeEmail);

    @EntityGraph(attributePaths = "nextMatch")
    Page<MatchEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "nextMatch")
    Page<MatchEntity> findByTournamentName(String tournamentName, Pageable pageable);
}
