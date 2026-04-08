package com.zeuscodensa.techcupfutbol.persistence.adapter;

import com.zeuscodensa.techcupfutbol.core.model.Invitation;
import com.zeuscodensa.techcupfutbol.core.repository.IInvitationRepository;
import com.zeuscodensa.techcupfutbol.persistence.entity.InvitationEntity;
import com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper;
import com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper;
import com.zeuscodensa.techcupfutbol.persistence.repository.InvitationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class InvitationRepositoryAdapter implements IInvitationRepository {

    private final InvitationRepository invitationJpaRepository;

    public InvitationRepositoryAdapter(InvitationRepository invitationJpaRepository) {
        this.invitationJpaRepository = invitationJpaRepository;
    }

    @Override
    public List<Invitation> findByCaptainEmail(String captainEmail) {
        return invitationJpaRepository.findByCaptainEmail(captainEmail).stream()
                .map(EntityToModelMapper::toInvitationModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Invitation> findByPlayerEmail(String playerEmail) {
        return invitationJpaRepository.findByPlayerEmail(playerEmail).stream()
                .map(EntityToModelMapper::toInvitationModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Invitation> findById(String id) {
        return invitationJpaRepository.findById(id).map(EntityToModelMapper::toInvitationModel);
    }

    @Override
    public Invitation save(Invitation invitation) {
        InvitationEntity entity = ModelToEntityMapper.toInvitationEntity(invitation);
        InvitationEntity savedEntity = invitationJpaRepository.save(entity);
        return EntityToModelMapper.toInvitationModel(savedEntity);
    }
}
