package core.service;

import dependencies.dto.RegistrationRequestDTO;
import dependencies.dto.RegistrationResponseDTO;
import core.validator.RegistrationValidator;
import dependencies.persistence.entity.RegistrationEntity;
import dependencies.persistence.repository.RegistrationRepository;
import dependencies.persistence.repository.TeamRepository;
import dependencies.persistence.repository.TournamentRepository;
import core.exception.ResourceNotFoundException;
import core.exception.BusinessRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InscripcionServiceTest {

    @Mock
    private RegistrationRepository registrationRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TournamentRepository tournamentRepository;

    @Mock
    private RegistrationValidator registrationValidator;

    @InjectMocks
    private RegistrationService inscripcionService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testInscribir_Success() {
        RegistrationRequestDTO req = new RegistrationRequestDTO("Aguilas", "Nacional", "url_pago");
        
        doNothing().when(registrationValidator).validateForInscripcion(req);

        RegistrationEntity savedEntity = new RegistrationEntity();
        savedEntity.setId("123");
        savedEntity.setTeamName("Aguilas");
        savedEntity.setTournamentName("Nacional");
        savedEntity.setStatus("PENDING");
        
        when(registrationRepository.save(any(RegistrationEntity.class))).thenReturn(savedEntity);

        RegistrationResponseDTO res = inscripcionService.inscribir(req);

        assertNotNull(res);
        assertNotNull(res.getId());
        assertEquals("PENDING", res.getStatus());
        verify(registrationRepository, times(1)).save(any(RegistrationEntity.class));
    }

    @Test
    public void testActualizarEstado_Success() {
        String idToUpdate = "123";
        RegistrationEntity existingEntity = new RegistrationEntity();
        existingEntity.setId(idToUpdate);
        existingEntity.setTeamName("Aguilas");
        existingEntity.setTournamentName("Nacional");
        existingEntity.setStatus("PENDING");

        when(registrationRepository.findById(idToUpdate)).thenReturn(Optional.of(existingEntity));

        RegistrationEntity afterInReview = new RegistrationEntity();
        afterInReview.setId(idToUpdate);
        afterInReview.setTeamName("Aguilas");
        afterInReview.setTournamentName("Nacional");
        afterInReview.setStatus("IN_REVIEW");

        when(registrationRepository.save(any(RegistrationEntity.class))).thenReturn(afterInReview);
        
        RegistrationResponseDTO resInReview = inscripcionService.actualizarEstado(idToUpdate, "IN_REVIEW");
        assertEquals("IN_REVIEW", resInReview.getStatus());

        // Now mock for moving to APPROVED
        RegistrationEntity currentInReviewEntity = new RegistrationEntity();
        currentInReviewEntity.setId(idToUpdate);
        currentInReviewEntity.setStatus("IN_REVIEW");

        when(registrationRepository.findById(idToUpdate)).thenReturn(Optional.of(currentInReviewEntity));
        
        RegistrationEntity afterApproved = new RegistrationEntity();
        afterApproved.setId(idToUpdate);
        afterApproved.setStatus("APPROVED");

        when(registrationRepository.save(any(RegistrationEntity.class))).thenReturn(afterApproved);

        RegistrationResponseDTO updated = inscripcionService.actualizarEstado(idToUpdate, "APPROVED");
        assertEquals("APPROVED", updated.getStatus());
    }

    @Test
    public void testActualizarEstado_InvalidState() {
        String idToUpdate = "123";
        RegistrationEntity existingEntity = new RegistrationEntity();
        existingEntity.setId(idToUpdate);
        existingEntity.setStatus("PENDING");

        when(registrationRepository.findById(idToUpdate)).thenReturn(Optional.of(existingEntity));

        assertThrows(BusinessRuleException.class, () -> inscripcionService.actualizarEstado(idToUpdate, "MISTERIO"));
    }

    @Test
    public void testActualizarEstado_IdNotFound_Throws() {
        when(registrationRepository.findById(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> inscripcionService.actualizarEstado("Inventado-123", "IN_REVIEW"));
    }
}
