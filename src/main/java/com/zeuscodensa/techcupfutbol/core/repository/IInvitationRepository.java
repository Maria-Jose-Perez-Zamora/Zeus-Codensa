package com.zeuscodensa.techcupfutbol.core.repository;

import java.util.List;
import java.util.Optional;

import com.zeuscodensa.techcupfutbol.core.model.Invitation;

public interface IInvitationRepository {
    List<Invitation> findByCaptainEmail(String captainEmail);
    List<Invitation> findByPlayerEmail(String playerEmail);
    Optional<Invitation> findById(String id);
    Invitation save(Invitation invitation);
}
