package com.zeuscodensa.techcupfutbol.controller.api;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Endpoint público de health check.
 * No requiere autenticación (configurado en SecurityConfig).
 *
 * Respuesta: { "status": "UP", "version": "1.3.3", "db": "OK" }
 *
 * Usado por los workflows de CD (ZEUS-148, ZEUS-155) para validar
 * que la app y la BD están operativas tras el deploy.
 */
@RestController
@RequestMapping("/health")
@Tag(name = "Health Check", description = "Verificación de salud del sistema y conectividad con la BD")
public class HealthController {

    private static final String APP_VERSION = "1.3.3";

    private final DataSource dataSource;

    public HealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * GET /health
     * Retorna el estado de la aplicación y la conectividad con la base de datos.
     * Siempre retorna HTTP 200 para que el health check del App Service no reinicie la app.
     * El campo "db" indica si la BD está disponible.
     */
    @GetMapping
    @Operation(
        summary = "Health Check",
        description = "Verifica el estado de la aplicación y la conectividad con la base de datos. Retorna HTTP 200 siempre."
    )
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new LinkedHashMap<>();
        response.put("status", "UP");
        response.put("version", APP_VERSION);
        response.put("db", checkDatabaseConnectivity());
        return ResponseEntity.ok(response);
    }

    private String checkDatabaseConnectivity() {
        try (var connection = dataSource.getConnection()) {
            return connection.isValid(2) ? "OK" : "DEGRADED";
        } catch (Exception e) {
            return "ERROR";
        }
    }
}
