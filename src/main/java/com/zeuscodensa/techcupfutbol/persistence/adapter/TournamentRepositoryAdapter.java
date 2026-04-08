package com.zeuscodensa.techcupfutbol.persistence.adapter;

import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import com.zeuscodensa.techcupfutbol.core.repository.ITournamentRepository;
import com.zeuscodensa.techcupfutbol.persistence.entity.TournamentEntity;
import com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper;
import com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper;
import com.zeuscodensa.techcupfutbol.persistence.repository.TournamentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TournamentRepositoryAdapter implements ITournamentRepository {

    private final TournamentRepository tournamentJpaRepository;

    public TournamentRepositoryAdapter(TournamentRepository tournamentJpaRepository) {
        this.tournamentJpaRepository = tournamentJpaRepository;
    }

    @Override
    public List<Tournament> findAll() {
        return tournamentJpaRepository.findAll().stream()
                .map(EntityToModelMapper::toTournamentModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Tournament> findById(String id) {
        return tournamentJpaRepository.findById(id).map(EntityToModelMapper::toTournamentModel);
    }
    
    @Override
    public Optional<Tournament> findByTournamentName(String tournamentName) {
        return tournamentJpaRepository.findByTournamentName(tournamentName).map(EntityToModelMapper::toTournamentModel);
    }

    @Override
    public Tournament save(Tournament tournament) {
        TournamentEntity entity = ModelToEntityMapper.toTournamentEntity(tournament);
        TournamentEntity savedEntity = tournamentJpaRepository.save(entity);
        return EntityToModelMapper.toTournamentModel(savedEntity);
    }
    
    @Override
    public boolean existsByTournamentName(String tournamentName) {
        return tournamentJpaRepository.existsByTournamentName(tournamentName);
    }
}
