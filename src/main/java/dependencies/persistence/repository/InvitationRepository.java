package dependencies.persistence.repository;

import dependencies.persistence.entity.InvitationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvitationRepository extends JpaRepository<InvitationEntity, String> {
    List<InvitationEntity> findByCaptainEmail(String captainEmail);
    List<InvitationEntity> findByPlayerEmail(String playerEmail);
}
