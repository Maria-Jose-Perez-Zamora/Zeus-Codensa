package service;

import dto.InvitacionRequestDTO;
import dto.InvitacionResponseDTO;
import dto.UserResponseDTO;
import model.Jugador;
import model.Role;
import model.Team;
import util.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JugadorServiceTest {

    private JugadorService jugadorService;

    @BeforeEach
    public void setUp() {
        DataStorage.clearAll();
        jugadorService = new JugadorService();

        Jugador j1 = new Jugador();
        j1.setNombre("Ana González"); j1.setCorreo("ana@test.com");
        j1.setRole(Role.JUGADOR); j1.setPosicion("portero");

        Jugador j2 = new Jugador();
        j2.setNombre("Carlos Ruiz"); j2.setCorreo("carlos@test.com");
        j2.setRole(Role.JUGADOR); j2.setPosicion("delantero");

        DataStorage.users.add(j1);
        DataStorage.users.add(j2);
    }

    @Test
    public void testBuscarJugadoresDisponibles_SinFiltro() {
        List<UserResponseDTO> result = jugadorService.buscarJugadoresDisponibles(null, null);
        assertEquals(2, result.size());
    }

    @Test
    public void testBuscarJugadoresDisponibles_PorNombre() {
        List<UserResponseDTO> result = jugadorService.buscarJugadoresDisponibles("Ana", null);
        assertEquals(1, result.size());
        assertEquals("Ana González", result.get(0).getNombre());
    }

    @Test
    public void testBuscarJugadoresDisponibles_PorPosicion() {
        List<UserResponseDTO> result = jugadorService.buscarJugadoresDisponibles(null, "delantero");
        assertEquals(1, result.size());
        assertEquals("Carlos Ruiz", result.get(0).getNombre());
    }

    @Test
    public void testBuscarDisponibles_ExcluyeJugadoresConEquipo() {
        Team t = new Team("FC Test");
        Jugador miembro = new Jugador();
        miembro.setCorreo("ana@test.com");
        t.getJugadores().add(miembro);
        DataStorage.teams.add(t);

        List<UserResponseDTO> result = jugadorService.buscarJugadoresDisponibles(null, null);
        assertEquals(1, result.size());
        assertEquals("Carlos Ruiz", result.get(0).getNombre());
    }

    @Test
    public void testEnviarInvitacion_Success() {
        InvitacionRequestDTO req = new InvitacionRequestDTO("capitan@test.com", "ana@test.com", "FC Alpha");
        InvitacionResponseDTO res = jugadorService.enviarInvitacion(req);

        assertNotNull(res.getId());
        assertEquals("ENVIADA", res.getEstado());
        assertEquals(1, DataStorage.invitaciones.size());
    }

    @Test
    public void testEnviarInvitacion_JugadorNoExisteThrows() {
        InvitacionRequestDTO req = new InvitacionRequestDTO("cap@test.com", "noexiste@test.com", "FC Alpha");
        assertThrows(RuntimeException.class, () -> jugadorService.enviarInvitacion(req));
    }

    @Test
    public void testEnviarInvitacion_JugadorEnEquipoThrows() {
        Team t = new Team("FC Test");
        Jugador miembro = new Jugador(); miembro.setCorreo("ana@test.com");
        t.getJugadores().add(miembro);
        DataStorage.teams.add(t);

        InvitacionRequestDTO req = new InvitacionRequestDTO("cap@test.com", "ana@test.com", "FC Alpha");
        assertThrows(RuntimeException.class, () -> jugadorService.enviarInvitacion(req));
    }
}
