package com.zeuscodensa.techcupfutbol.core.repository;

import com.zeuscodensa.techcupfutbol.core.model.Tournament;

import java.util.List;
import java.util.Optional;

public interface ITournamentRepository {
    List<Tournament> findAll();
    Optional<Tournament> findById(String id);
    Optional<Tournament> findByTournamentName(String tournamentName);
    Tournament save(Tournament tournament);
    boolean existsByTournamentName(String tournamentName);
}
