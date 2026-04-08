package com.zeuscodensa.techcupfutbol.persistence.adapter;

import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.repository.ITeamRepository;
import com.zeuscodensa.techcupfutbol.persistence.entity.TeamEntity;
import com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper;
import com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper;
import com.zeuscodensa.techcupfutbol.persistence.repository.TeamRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class TeamRepositoryAdapter implements ITeamRepository {

    private final TeamRepository teamJpaRepository;

    public TeamRepositoryAdapter(TeamRepository teamJpaRepository) {
        this.teamJpaRepository = teamJpaRepository;
    }

    @Override
    public List<Team> findAll() {
        return teamJpaRepository.findAll().stream()
                .map(EntityToModelMapper::toTeamModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Team> findByTeamName(String teamName) {
        return teamJpaRepository.findByTeamName(teamName).map(EntityToModelMapper::toTeamModel);
    }

    @Override
    public boolean existsByPlayersEmail(String email) {
        return teamJpaRepository.existsByPlayersEmail(email);
    }

    @Override
    public Team save(Team team) {
        TeamEntity entity = ModelToEntityMapper.toTeamEntity(team);
        TeamEntity savedEntity = teamJpaRepository.save(entity);
        return EntityToModelMapper.toTeamModel(savedEntity);
    }
}
