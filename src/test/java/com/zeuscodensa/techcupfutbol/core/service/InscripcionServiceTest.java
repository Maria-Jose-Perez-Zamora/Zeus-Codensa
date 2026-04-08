package com.zeuscodensa.techcupfutbol.core.service;

import com.zeuscodensa.techcupfutbol.core.model.Registration;
import com.zeuscodensa.techcupfutbol.core.validator.RegistrationValidator;
import com.zeuscodensa.techcupfutbol.core.repository.IRegistrationRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITeamRepository;
import com.zeuscodensa.techcupfutbol.core.repository.ITournamentRepository;
import com.zeuscodensa.techcupfutbol.core.exception.ResourceNotFoundException;
import com.zeuscodensa.techcupfutbol.core.exception.BusinessRuleException;
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
    private IRegistrationRepository registrationRepository;

    @Mock
    private ITeamRepository teamRepository;

    @Mock
    private ITournamentRepository tournamentRepository;

    @Mock
    private RegistrationValidator registrationValidator;

    @InjectMocks
    private RegistrationService inscripcionService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testInscribir_Success() {
        Registration req = new Registration("Aguilas", "Nacional", "url_pago");
        
        doNothing().when(registrationValidator).validateForInscripcion("Aguilas", "Nacional", "url_pago");

        Registration savedEntity = new Registration();
        savedEntity.setId("123");
        savedEntity.setTeamName("Aguilas");
        savedEntity.setTournamentName("Nacional");
        savedEntity.setStatus("PENDING");
        
        when(registrationRepository.save(any(Registration.class))).thenReturn(savedEntity);

        Registration res = inscripcionService.inscribir(req);

        assertNotNull(res);
        assertNotNull(res.getId());
        assertEquals("PENDING", res.getStatus());
        verify(registrationRepository, times(1)).save(any(Registration.class));
    }

    @Test
    public void testActualizarEstado_Success() {
        String idToUpdate = "123";
        Registration existingEntity = new Registration();
        existingEntity.setId(idToUpdate);
        existingEntity.setTeamName("Aguilas");
        existingEntity.setTournamentName("Nacional");
        existingEntity.setStatus("PENDING");

        when(registrationRepository.findById(idToUpdate)).thenReturn(Optional.of(existingEntity));

        Registration afterInReview = new Registration();
        afterInReview.setId(idToUpdate);
        afterInReview.setTeamName("Aguilas");
        afterInReview.setTournamentName("Nacional");
        afterInReview.setStatus("IN_REVIEW");

        when(registrationRepository.save(any(Registration.class))).thenReturn(afterInReview);
        
        Registration resInReview = inscripcionService.actualizarEstado(idToUpdate, "IN_REVIEW");
        assertEquals("IN_REVIEW", resInReview.getStatus());

        // Now mock for moving to APPROVED
        Registration currentInReviewEntity = new Registration();
        currentInReviewEntity.setId(idToUpdate);
        currentInReviewEntity.setStatus("IN_REVIEW");

        when(registrationRepository.findById(idToUpdate)).thenReturn(Optional.of(currentInReviewEntity));
        
        Registration afterApproved = new Registration();
        afterApproved.setId(idToUpdate);
        afterApproved.setStatus("APPROVED");

        when(registrationRepository.save(any(Registration.class))).thenReturn(afterApproved);

        Registration updated = inscripcionService.actualizarEstado(idToUpdate, "APPROVED");
        assertEquals("APPROVED", updated.getStatus());
    }

    @Test
    public void testActualizarEstado_InvalidState() {
        String idToUpdate = "123";
        Registration existingEntity = new Registration();
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
