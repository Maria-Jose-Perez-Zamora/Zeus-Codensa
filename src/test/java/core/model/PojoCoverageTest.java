package core.model;

import dependencies.dto.*;
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
        RegistrationRequestDTO req = new RegistrationRequestDTO();
        req.setNombreEquipo("E"); req.setTournamentName("T"); req.setComprobantePagoUrl("U");
        assertEquals("E", req.getNombreEquipo()); assertEquals("T", req.getTournamentName()); assertEquals("U", req.getComprobantePagoUrl());

        Registration i = new Registration();
        i.setId("1"); i.setNombreEquipo("E"); i.setTournamentName("T"); i.setComprobantePagoUrl("U"); i.setStatus("P");
        assertEquals("1", i.getId()); assertEquals("E", i.getNombreEquipo()); assertEquals("T", i.getTournamentName());
        assertEquals("U", i.getComprobantePagoUrl()); assertEquals("P", i.getStatus());

        RegistrationResponseDTO res = new RegistrationResponseDTO();
        res.setId("1"); res.setNombreEquipo("E"); res.setStatus("P");
        assertEquals("1", res.getId()); assertEquals("E", res.getNombreEquipo()); assertEquals("P", res.getStatus());
        
        RegistrationResponseDTO res2 = new RegistrationResponseDTO(i);
        assertEquals("P", res2.getStatus());
    }

    @Test
    public void testPartido() {
        MatchRequestDTO req = new MatchRequestDTO();
        req.setHomeTeam("L"); req.setAwayTeam("V"); req.setTournamentName("T"); req.setMatchDate("F");
        assertEquals("L", req.getHomeTeam()); assertEquals("V", req.getAwayTeam());
        assertEquals("T", req.getTournamentName()); assertEquals("F", req.getMatchDate());

        Match p = new Match("L", "V", "F", "T");
        p.setId("1"); p.setStatus("E"); p.setHomeScore(1); p.setAwayScore(1);
        p.setAlineaciones(new HashMap<>()); p.setYellowCards(new HashMap<>());
        p.setRedCards(new HashMap<>()); p.setCorreoArbitro("C");
        p.setTournamentName("T"); p.setMatchDate("F"); p.setHomeTeam("L"); p.setAwayTeam("V");
        assertEquals("1", p.getId()); assertEquals("E", p.getStatus()); assertEquals("C", p.getCorreoArbitro());
        assertEquals(1, p.getHomeScore()); assertEquals(1, p.getAwayScore());
        assertNotNull(p.getAlineaciones()); assertNotNull(p.getYellowCards()); assertNotNull(p.getRedCards());
        assertEquals("T", p.getTournamentName()); assertEquals("F", p.getMatchDate());
        assertEquals("L", p.getHomeTeam()); assertEquals("V", p.getAwayTeam());

        MatchResponseDTO res = new MatchResponseDTO();
        res.setId("1"); res.setHomeTeam("L"); res.setAwayTeam("V");
        res.setTournamentName("T"); res.setMatchDate("F"); res.setStatus("E");
        res.setHomeScore(1); res.setAwayScore(1); res.setCorreoArbitro("C");
        res.setAlineaciones(new HashMap<>()); res.setYellowCards(new HashMap<>()); res.setRedCards(new HashMap<>());
        assertEquals("1", res.getId()); assertEquals("E", res.getStatus()); assertEquals("C", res.getCorreoArbitro());

        MatchResponseDTO res2 = new MatchResponseDTO(p);
        assertEquals("L", res2.getHomeTeam());
    }

    @Test
    public void testUsers() {
        Player j = new Player();
        j.setName("N"); j.setCorreo("C"); j.setContrasena("X"); j.setFoto("F"); j.setRole(Role.PLAYER);
        j.setPosicion("DEL"); j.setNumeroDorsal(9);
        assertEquals("N", j.getName()); assertEquals("C", j.getCorreo());
        assertEquals("X", j.getContrasena()); assertEquals("F", j.getFoto()); assertEquals(Role.PLAYER, j.getRole());
        assertEquals("DEL", j.getPosicion()); assertEquals(9, j.getNumeroDorsal());

        UserRequestDTO req = new UserRequestDTO();
        req.setName("N"); req.setCorreo("C"); req.setContrasena("X"); req.setFoto("F"); req.setRole(Role.PLAYER);
        req.setPosicion("DEL"); req.setNumeroDorsal(9);
        assertEquals("N", req.getName()); assertEquals("C", req.getCorreo()); assertEquals("X", req.getContrasena());
        assertEquals("F", req.getFoto()); assertEquals(Role.PLAYER, req.getRole());
        assertEquals("DEL", req.getPosicion()); assertEquals(9, req.getNumeroDorsal());

        UserResponseDTO res = new UserResponseDTO();
        res.setName("N"); res.setCorreo("C"); res.setRole(Role.PLAYER);
        assertEquals("N", res.getName()); assertEquals("C", res.getCorreo()); assertEquals(Role.PLAYER, res.getRole());

        Capitan c = new Capitan(); c.setPosicion("MED"); c.setNumeroDorsal(1);
        assertEquals("MED", c.getPosicion()); assertEquals(1, c.getNumeroDorsal());
        Arbitro a = new Arbitro(); a.setName("A"); assertEquals("A", a.getName());
        AdministradorSistema admin = new AdministradorSistema(); admin.setName("Admin"); assertEquals("Admin", admin.getName());
        TournamentOrganizer org = new TournamentOrganizer(); org.setName("Org"); assertEquals("Org", org.getName());
    }

    @Test
    public void testTournamentsAndTeams() {
        TournamentRequestDTO req = new TournamentRequestDTO();
        req.setTournamentName("Tor"); req.setNumeroEquipos(8); req.setCostoInscripcion(5.0);
        assertEquals("Tor", req.getTournamentName()); assertEquals(8, req.getNumeroEquipos()); assertEquals(5.0, req.getCostoInscripcion());

        Tournament t = new Tournament();
        t.setId("1"); t.setTournamentName("Tor"); t.setStatus("E"); t.setNumeroEquipos(8); t.setCostoInscripcion(5.0);
        assertEquals("1", t.getId()); assertEquals("Tor", t.getTournamentName()); assertEquals("E", t.getStatus());
        assertEquals(8, t.getNumeroEquipos()); assertEquals(5.0, t.getCostoInscripcion());

        TournamentResponseDTO tor = new TournamentResponseDTO();
        tor.setId("1"); tor.setTournamentName("Tor"); tor.setStatus("E");
        assertEquals("1", tor.getId()); assertEquals("Tor", tor.getTournamentName()); assertEquals("E", tor.getStatus());

        TournamentResponseDTO tor2 = new TournamentResponseDTO(new Tournament("Tor"));
        assertEquals("Tor", tor2.getTournamentName());

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
        InvitationRequestDTO invReq = new InvitationRequestDTO();
        invReq.setCorreoCapitan("C"); invReq.setCorreoJugador("J"); invReq.setNombreEquipo("E");
        assertEquals("C", invReq.getCorreoCapitan()); assertEquals("J", invReq.getCorreoJugador()); assertEquals("E", invReq.getNombreEquipo());

        Invitation inv = new Invitation();
        inv.setId("1"); inv.setCorreoCapitan("C"); inv.setCorreoJugador("J"); inv.setNombreEquipo("E"); inv.setStatus("E");
        assertEquals("1", inv.getId()); assertEquals("C", inv.getCorreoCapitan()); assertEquals("J", inv.getCorreoJugador());
        assertEquals("E", inv.getNombreEquipo()); assertEquals("E", inv.getStatus());

        InvitationResponseDTO invRes = new InvitationResponseDTO();
        invRes.setId("1"); invRes.setStatus("E");
        assertEquals("1", invRes.getId()); assertEquals("E", invRes.getStatus());

        InvitationResponseDTO invRes2 = new InvitationResponseDTO(inv);
        assertEquals("E", invRes2.getStatus());

        KnockoutBracket llave = new KnockoutBracket("F", "E1", "E2");
        llave.setPhase("F"); llave.setHomeTeam("E1"); llave.setAwayTeam("E2");
        llave.setGanador("E2");
        assertEquals("F", llave.getPhase()); assertEquals("E1", llave.getHomeTeam());
        assertEquals("E2", llave.getAwayTeam());
        assertEquals("E2", llave.getGanador());

        Standing tp = new Standing("E");
        tp.registrarPartido(2, 1);
        tp.registrarPartido(1, 1);
        tp.registrarPartido(0, 2);
        assertEquals("E", tp.getNombreEquipo());
        assertEquals(3, tp.getMatchesPlayed());
        assertEquals(1, tp.getMatchesWon());
        assertEquals(1, tp.getMatchesDrawn());
        assertEquals(1, tp.getMatchesLost());
        assertEquals(3, tp.getGolesFavor());
        assertEquals(4, tp.getGolesContra());
        assertEquals(-1, tp.getGoalDifference());
        assertEquals(4, tp.getPoints());

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
