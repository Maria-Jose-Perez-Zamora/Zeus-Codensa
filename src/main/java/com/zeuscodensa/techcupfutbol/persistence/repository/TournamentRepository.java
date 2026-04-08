package com.zeuscodensa.techcupfutbol.persistence.repository;

import com.zeuscodensa.techcupfutbol.persistence.entity.TournamentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TournamentRepository extends JpaRepository<TournamentEntity, String> {
    Optional<TournamentEntity> findByTournamentName(String tournamentName);
    boolean existsByTournamentName(String tournamentName);
}
