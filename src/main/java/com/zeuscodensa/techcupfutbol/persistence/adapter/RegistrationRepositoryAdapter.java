package com.zeuscodensa.techcupfutbol.persistence.adapter;

import com.zeuscodensa.techcupfutbol.core.model.Registration;
import com.zeuscodensa.techcupfutbol.core.repository.IRegistrationRepository;
import com.zeuscodensa.techcupfutbol.persistence.entity.RegistrationEntity;
import com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper;
import com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper;
import com.zeuscodensa.techcupfutbol.persistence.repository.RegistrationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class RegistrationRepositoryAdapter implements IRegistrationRepository {

    private final RegistrationRepository registrationJpaRepository;

    public RegistrationRepositoryAdapter(RegistrationRepository registrationJpaRepository) {
        this.registrationJpaRepository = registrationJpaRepository;
    }

    @Override
    public List<Registration> findAll() {
        return registrationJpaRepository.findAll().stream()
                .map(EntityToModelMapper::toRegistrationModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Registration> findById(String id) {
        return registrationJpaRepository.findById(id).map(EntityToModelMapper::toRegistrationModel);
    }

    @Override
    public Registration save(Registration registration) {
        RegistrationEntity entity = ModelToEntityMapper.toRegistrationEntity(registration);
        RegistrationEntity savedEntity = registrationJpaRepository.save(entity);
        return EntityToModelMapper.toRegistrationModel(savedEntity);
    }
    
    @Override
    public boolean existsByTeamNameAndTournamentName(String teamName, String tournamentName) {
        return registrationJpaRepository.existsByTeamNameAndTournamentName(teamName, tournamentName);
    }

    @Override
    public boolean existsByTeamNameAndTournamentNameAndStatus(String teamName, String tournamentName, String status) {
        return registrationJpaRepository.existsByTeamNameAndTournamentNameAndStatus(teamName, tournamentName, status);
    }
    
    @Override
    public List<Registration> findByTournamentName(String tournamentName) {
        return registrationJpaRepository.findByTournamentName(tournamentName).stream()
                .map(EntityToModelMapper::toRegistrationModel)
                .collect(Collectors.toList());
    }
}
