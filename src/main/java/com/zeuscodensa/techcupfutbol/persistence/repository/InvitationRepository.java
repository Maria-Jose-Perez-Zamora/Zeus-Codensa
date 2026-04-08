package com.zeuscodensa.techcupfutbol.persistence.repository;

import com.zeuscodensa.techcupfutbol.persistence.entity.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<InvitationEntity, String> {
    List<InvitationEntity> findByCaptainEmail(String captainEmail);
    List<InvitationEntity> findByPlayerEmail(String playerEmail);
}
