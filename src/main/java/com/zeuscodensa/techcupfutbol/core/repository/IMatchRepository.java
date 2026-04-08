package com.zeuscodensa.techcupfutbol.core.repository;

import com.zeuscodensa.techcupfutbol.core.model.Match;

import java.util.List;
import java.util.Optional;

public interface IMatchRepository {
    List<Match> findAll();
    Optional<Match> findById(String id);
    Match save(Match match);
    List<Match> findByRefereeEmail(String refereeEmail);
    List<Match> findByTournamentName(String tournamentName);
}
