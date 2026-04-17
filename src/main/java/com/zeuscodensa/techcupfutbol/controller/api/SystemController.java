package com.zeuscodensa.techcupfutbol.controller.api;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import jakarta.persistence.EntityManager;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/system")
public class SystemController {
    
    private static final Logger log = LoggerFactory.getLogger(SystemController.class);
    private final EntityManager entityManager;

    public SystemController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @DeleteMapping("/wipe")
    @Transactional
    public ResponseEntity<String> wipeDatabase() {
        log.warn("=== ATENCION: BORRANDO TODA LA BASE DE DATOS ===");
        try {
            // Se usa CASCADE para borrar los registros dependientes
            entityManager.createNativeQuery(
                "TRUNCATE TABLE invitations, registrations, matches, tournament_canchas, tournament_horarios, tournaments, team_players, teams, users CASCADE"
            ).executeUpdate();
            
            return ResponseEntity.ok("Base de datos borrada exitosamente (excepto esquemas). Lista para pruebas en blanco.");
        } catch (Exception e) {
            log.error("Fallo al borrar la base de datos", e);
            return ResponseEntity.internalServerError().body("Error al limpiar DB: " + e.getMessage());
        }
    }
}
