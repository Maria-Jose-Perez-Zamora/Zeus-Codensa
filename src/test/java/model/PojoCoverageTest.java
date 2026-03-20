package model;

import dto.*;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PojoCoverageTest {

    @Test
    public void testApiErrorDTO() {
        ApiErrorDTO dto = new ApiErrorDTO(400, "Bad", "Test", "/path");
        dto.setStatus(500); dto.setError("Error"); dto.setMessage("Msg"); dto.setPath("/new");
        assertEquals(500, dto.getStatus()); assertEquals("Error", dto.getError());
        assertEquals("Msg", dto.getMessage()); assertEquals("/new", dto.getPath());
    }

    @Test
    public void testInscripcion() {
        InscripcionRequestDTO req = new InscripcionRequestDTO();
        req.setNombreEquipo("E"); req.setNombreTorneo("T"); req.setComprobantePagoUrl("U");
        assertEquals("E", req.getNombreEquipo()); assertEquals("T", req.getNombreTorneo()); assertEquals("U", req.getComprobantePagoUrl());

        Inscripcion i = new Inscripcion();
        i.setId("1"); i.setNombreEquipo("E"); i.setNombreTorneo("T"); i.setComprobantePagoUrl("U"); i.setEstado("P");
        assertEquals("1", i.getId()); assertEquals("E", i.getNombreEquipo()); assertEquals("T", i.getNombreTorneo());
        assertEquals("U", i.getComprobantePagoUrl()); assertEquals("P", i.getEstado());

        InscripcionResponseDTO res = new InscripcionResponseDTO();
        res.setId("1"); res.setNombreEquipo("E"); res.setEstado("P");
        assertEquals("1", res.getId()); assertEquals("E", res.getNombreEquipo()); assertEquals("P", res.getEstado());
        
        InscripcionResponseDTO res2 = new InscripcionResponseDTO(i);
        assertEquals("P", res2.getEstado());
    }

    @Test
    public void testPartido() {
        PartidoRequestDTO req = new PartidoRequestDTO();
        req.setEquipoLocal("L"); req.setEquipoVisitante("V"); req.setNombreTorneo("T"); req.setFechaPartido("F");
        assertEquals("L", req.getEquipoLocal()); assertEquals("V", req.getEquipoVisitante());
        assertEquals("T", req.getNombreTorneo()); assertEquals("F", req.getFechaPartido());

        Partido p = new Partido("L", "V", "F", "T");
        p.setId("1"); p.setEstado("E"); p.setMarcadorLocal(1); p.setMarcadorVisitante(1);
        p.setAlineaciones(new HashMap<>()); p.setTarjetasAmarillas(new HashMap<>());
        p.setTarjetasRojas(new HashMap<>()); p.setCorreoArbitro("C");
        p.setNombreTorneo("T"); p.setFechaPartido("F"); p.setEquipoLocal("L"); p.setEquipoVisitante("V");
        assertEquals("1", p.getId()); assertEquals("E", p.getEstado()); assertEquals("C", p.getCorreoArbitro());
        assertEquals(1, p.getMarcadorLocal()); assertEquals(1, p.getMarcadorVisitante());
        assertNotNull(p.getAlineaciones()); assertNotNull(p.getTarjetasAmarillas()); assertNotNull(p.getTarjetasRojas());
        assertEquals("T", p.getNombreTorneo()); assertEquals("F", p.getFechaPartido());
        assertEquals("L", p.getEquipoLocal()); assertEquals("V", p.getEquipoVisitante());

        PartidoResponseDTO res = new PartidoResponseDTO();
        res.setId("1"); res.setEquipoLocal("L"); res.setEquipoVisitante("V");
        res.setNombreTorneo("T"); res.setFechaPartido("F"); res.setEstado("E");
        res.setMarcadorLocal(1); res.setMarcadorVisitante(1); res.setCorreoArbitro("C");
        res.setAlineaciones(new HashMap<>()); res.setTarjetasAmarillas(new HashMap<>()); res.setTarjetasRojas(new HashMap<>());
        assertEquals("1", res.getId()); assertEquals("E", res.getEstado()); assertEquals("C", res.getCorreoArbitro());

        PartidoResponseDTO res2 = new PartidoResponseDTO(p);
        assertEquals("L", res2.getEquipoLocal());
    }

    @Test
    public void testUsers() {
        Jugador j = new Jugador();
        j.setNombre("N"); j.setCorreo("C"); j.setContrasena("X"); j.setFoto("F"); j.setRole(Role.JUGADOR);
        j.setPosicion("DEL"); j.setNumeroDorsal(9);
        assertEquals("N", j.getNombre()); assertEquals("C", j.getCorreo());
        assertEquals("X", j.getContrasena()); assertEquals("F", j.getFoto()); assertEquals(Role.JUGADOR, j.getRole());
        assertEquals("DEL", j.getPosicion()); assertEquals(9, j.getNumeroDorsal());

        UserRequestDTO req = new UserRequestDTO();
        req.setNombre("N"); req.setCorreo("C"); req.setContrasena("X"); req.setFoto("F"); req.setRole(Role.JUGADOR);
        req.setPosicion("DEL"); req.setNumeroDorsal(9);
        assertEquals("N", req.getNombre()); assertEquals("C", req.getCorreo()); assertEquals("X", req.getContrasena());
        assertEquals("F", req.getFoto()); assertEquals(Role.JUGADOR, req.getRole());
        assertEquals("DEL", req.getPosicion()); assertEquals(9, req.getNumeroDorsal());

        UserResponseDTO res = new UserResponseDTO();
        res.setNombre("N"); res.setCorreo("C"); res.setRole(Role.JUGADOR);
        assertEquals("N", res.getNombre()); assertEquals("C", res.getCorreo()); assertEquals(Role.JUGADOR, res.getRole());

        Capitan c = new Capitan(); c.setPosicion("MED"); c.setNumeroDorsal(1);
        assertEquals("MED", c.getPosicion()); assertEquals(1, c.getNumeroDorsal());
        Arbitro a = new Arbitro(); a.setNombre("A"); assertEquals("A", a.getNombre());
        AdministradorSistema admin = new AdministradorSistema(); admin.setNombre("Admin"); assertEquals("Admin", admin.getNombre());
        OrganizadorTorneo org = new OrganizadorTorneo(); org.setNombre("Org"); assertEquals("Org", org.getNombre());
    }

    @Test
    public void testTournamentsAndTeams() {
        TorneoRequestDTO req = new TorneoRequestDTO();
        req.setNombreTorneo("Tor"); req.setNumeroEquipos(8); req.setCostoInscripcion(5.0);
        assertEquals("Tor", req.getNombreTorneo()); assertEquals(8, req.getNumeroEquipos()); assertEquals(5.0, req.getCostoInscripcion());

        Torneo t = new Torneo();
        t.setId("1"); t.setNombreTorneo("Tor"); t.setEstado("E"); t.setNumeroEquipos(8); t.setCostoInscripcion(5.0);
        assertEquals("1", t.getId()); assertEquals("Tor", t.getNombreTorneo()); assertEquals("E", t.getEstado());
        assertEquals(8, t.getNumeroEquipos()); assertEquals(5.0, t.getCostoInscripcion());

        TorneoResponseDTO tor = new TorneoResponseDTO();
        tor.setId("1"); tor.setNombreTorneo("Tor"); tor.setEstado("E");
        assertEquals("1", tor.getId()); assertEquals("Tor", tor.getNombreTorneo()); assertEquals("E", tor.getEstado());

        TorneoResponseDTO tor2 = new TorneoResponseDTO(new Torneo("Tor"));
        assertEquals("Tor", tor2.getNombreTorneo());

        TeamRequestDTO team = new TeamRequestDTO();
        team.setNombreEquipo("T"); team.setEscudo("ESC"); team.setColoresUniforme("COL"); team.setJugadorCorreos(new ArrayList<>());
        assertEquals("T", team.getNombreEquipo()); assertEquals("ESC", team.getEscudo()); assertEquals("COL", team.getColoresUniforme());
        assertNotNull(team.getJugadorCorreos());
        
        TeamRequestDTO teamReq2 = new TeamRequestDTO("T", "ESC", "COL", new ArrayList<>());
        assertEquals("T", teamReq2.getNombreEquipo());

        Team teamModel = new Team();
        teamModel.setNombreEquipo("T"); teamModel.setEscudo("ESC"); teamModel.setColoresUniforme("COL"); teamModel.setJugadores(new ArrayList<>());
        assertEquals("T", teamModel.getNombreEquipo()); assertEquals("ESC", teamModel.getEscudo());
        assertEquals("COL", teamModel.getColoresUniforme()); assertNotNull(teamModel.getJugadores());

        TeamResponseDTO teamRes = new TeamResponseDTO();
        teamRes.setNombreEquipo("T"); teamRes.setEscudo("ESC"); teamRes.setColoresUniforme("COL");
        assertEquals("T", teamRes.getNombreEquipo());

        TeamResponseDTO teamRes2 = new TeamResponseDTO(teamModel);
        assertEquals("T", teamRes2.getNombreEquipo());
    }

    @Test
    public void testOthers() {
        InvitacionRequestDTO invReq = new InvitacionRequestDTO();
        invReq.setCorreoCapitan("C"); invReq.setCorreoJugador("J"); invReq.setNombreEquipo("E");
        assertEquals("C", invReq.getCorreoCapitan()); assertEquals("J", invReq.getCorreoJugador()); assertEquals("E", invReq.getNombreEquipo());

        Invitacion inv = new Invitacion();
        inv.setId("1"); inv.setCorreoCapitan("C"); inv.setCorreoJugador("J"); inv.setNombreEquipo("E"); inv.setEstado("E");
        assertEquals("1", inv.getId()); assertEquals("C", inv.getCorreoCapitan()); assertEquals("J", inv.getCorreoJugador());
        assertEquals("E", inv.getNombreEquipo()); assertEquals("E", inv.getEstado());

        InvitacionResponseDTO invRes = new InvitacionResponseDTO();
        invRes.setId("1"); invRes.setEstado("E");
        assertEquals("1", invRes.getId()); assertEquals("E", invRes.getEstado());

        InvitacionResponseDTO invRes2 = new InvitacionResponseDTO(inv);
        assertEquals("E", invRes2.getEstado());

        LlaveEliminatoria llave = new LlaveEliminatoria("F", "E1", "E2");
        llave.setFase("F"); llave.setEquipoLocal("E1"); llave.setEquipoVisitante("E2");
        llave.setGanador("E2");
        assertEquals("F", llave.getFase()); assertEquals("E1", llave.getEquipoLocal());
        assertEquals("E2", llave.getEquipoVisitante());
        assertEquals("E2", llave.getGanador());

        TablaPosicion tp = new TablaPosicion("E");
        tp.registrarPartido(2, 1);
        tp.registrarPartido(1, 1);
        tp.registrarPartido(0, 2);
        assertEquals("E", tp.getNombreEquipo());
        assertEquals(3, tp.getPartidosJugados());
        assertEquals(1, tp.getPartidosGanados());
        assertEquals(1, tp.getPartidosEmpatados());
        assertEquals(1, tp.getPartidosPerdidos());
        assertEquals(3, tp.getGolesFavor());
        assertEquals(4, tp.getGolesContra());
        assertEquals(-1, tp.getDiferenciaGoles());
        assertEquals(4, tp.getPuntos());

        LoginRequestDTO lr = new LoginRequestDTO();
        lr.setCorreo("C"); lr.setContrasena("C");
        assertEquals("C", lr.getCorreo()); assertEquals("C", lr.getContrasena());

        LoginResponseDTO ls = new LoginResponseDTO();
        ls.setToken("T"); ls.setUser(new UserResponseDTO());
        assertEquals("T", ls.getToken()); assertNotNull(ls.getUser());
        
        LoginResponseDTO ls2 = new LoginResponseDTO("T", new UserResponseDTO());
        assertEquals("T", ls2.getToken()); 
    }
}
