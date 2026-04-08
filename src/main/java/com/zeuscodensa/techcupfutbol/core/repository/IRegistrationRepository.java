package com.zeuscodensa.techcupfutbol.core.repository;

import com.zeuscodensa.techcupfutbol.core.model.Registration;

import java.util.List;
import java.util.Optional;

public interface IRegistrationRepository {
    List<Registration> findAll();
    Optional<Registration> findById(String id);
    Registration save(Registration registration);
    boolean existsByTeamNameAndTournamentName(String teamName, String tournamentName);
    boolean existsByTeamNameAndTournamentNameAndStatus(String teamName, String tournamentName, String status);
    List<Registration> findByTournamentName(String tournamentName);
}
