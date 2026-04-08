package com.zeuscodensa.techcupfutbol.persistence.adapter;

import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.repository.IMatchRepository;
import com.zeuscodensa.techcupfutbol.persistence.entity.MatchEntity;
import com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper;
import com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper;
import com.zeuscodensa.techcupfutbol.persistence.repository.MatchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MatchRepositoryAdapter implements IMatchRepository {

    private final MatchRepository matchJpaRepository;

    public MatchRepositoryAdapter(MatchRepository matchJpaRepository) {
        this.matchJpaRepository = matchJpaRepository;
    }

    @Override
    public List<Match> findAll() {
        return matchJpaRepository.findAll().stream()
                .map(EntityToModelMapper::toMatchModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Match> findById(String id) {
        return matchJpaRepository.findById(id).map(EntityToModelMapper::toMatchModel);
    }

    @Override
    public Match save(Match match) {
        MatchEntity entity = ModelToEntityMapper.toMatchEntity(match);
        MatchEntity savedEntity = matchJpaRepository.save(entity);
        return EntityToModelMapper.toMatchModel(savedEntity);
    }

    @Override
    public List<Match> findByRefereeEmail(String refereeEmail) {
        return matchJpaRepository.findByRefereeEmail(refereeEmail).stream()
                .map(EntityToModelMapper::toMatchModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> findByTournamentName(String tournamentName) {
        return matchJpaRepository.findByTournamentName(tournamentName).stream()
                .map(EntityToModelMapper::toMatchModel)
                .collect(Collectors.toList());
    }
}
