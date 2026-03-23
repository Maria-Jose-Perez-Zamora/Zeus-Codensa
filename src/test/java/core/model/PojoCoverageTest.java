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
        req.setTeamName("E"); req.setTournamentName("T"); req.setComprobantePagoUrl("U");
        assertEquals("E", req.getTeamName()); assertEquals("T", req.getTournamentName()); assertEquals("U", req.getComprobantePagoUrl());

        Registration i = new Registration();
        i.setId("1"); i.setTeamName("E"); i.setTournamentName("T"); i.setComprobantePagoUrl("U"); i.setStatus("P");
        assertEquals("1", i.getId()); assertEquals("E", i.getTeamName()); assertEquals("T", i.getTournamentName());
        assertEquals("U", i.getComprobantePagoUrl()); assertEquals("P", i.getStatus());

        RegistrationResponseDTO res = new RegistrationResponseDTO();
        res.setId("1"); res.setTeamName("E"); res.setStatus("P");
        assertEquals("1", res.getId()); assertEquals("E", res.getTeamName()); assertEquals("P", res.getStatus());
        
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
        p.setRedCards(new HashMap<>()); p.setRefereeEmail("C");
        p.setTournamentName("T"); p.setMatchDate("F"); p.setHomeTeam("L"); p.setAwayTeam("V");
        assertEquals("1", p.getId()); assertEquals("E", p.getStatus()); assertEquals("C", p.getRefereeEmail());
        assertEquals(1, p.getHomeScore()); assertEquals(1, p.getAwayScore());
        assertNotNull(p.getAlineaciones()); assertNotNull(p.getYellowCards()); assertNotNull(p.getRedCards());
        assertEquals("T", p.getTournamentName()); assertEquals("F", p.getMatchDate());
        assertEquals("L", p.getHomeTeam()); assertEquals("V", p.getAwayTeam());

        MatchResponseDTO res = new MatchResponseDTO();
        res.setId("1"); res.setHomeTeam("L"); res.setAwayTeam("V");
        res.setTournamentName("T"); res.setMatchDate("F"); res.setStatus("E");
        res.setHomeScore(1); res.setAwayScore(1); res.setRefereeEmail("C");
        res.setAlineaciones(new HashMap<>()); res.setYellowCards(new HashMap<>()); res.setRedCards(new HashMap<>());
        assertEquals("1", res.getId()); assertEquals("E", res.getStatus()); assertEquals("C", res.getRefereeEmail());

        MatchResponseDTO res2 = new MatchResponseDTO(p);
        assertEquals("L", res2.getHomeTeam());
    }

    @Test
    public void testUsers() {
        Player j = new Player();
        j.setName("N"); j.setEmail("C"); j.setPassword("X"); j.setPhoto("F"); j.setRole(Role.PLAYER);
        j.setPosition("DEL"); j.setJerseyNumber(9);
        assertEquals("N", j.getName()); assertEquals("C", j.getEmail());
        assertEquals("X", j.getPassword()); assertEquals("F", j.getPhoto()); assertEquals(Role.PLAYER, j.getRole());
        assertEquals("DEL", j.getPosition()); assertEquals(9, j.getJerseyNumber());

        UserRequestDTO req = new UserRequestDTO();
        req.setName("N"); req.setEmail("C"); req.setPassword("X"); req.setPhoto("F"); req.setRole(Role.PLAYER);
        req.setPosition("DEL"); req.setJerseyNumber(9);
        assertEquals("N", req.getName()); assertEquals("C", req.getEmail()); assertEquals("X", req.getPassword());
        assertEquals("F", req.getPhoto()); assertEquals(Role.PLAYER, req.getRole());
        assertEquals("DEL", req.getPosition()); assertEquals(9, req.getJerseyNumber());

        UserResponseDTO res = new UserResponseDTO();
        res.setName("N"); res.setEmail("C"); res.setRole(Role.PLAYER);
        assertEquals("N", res.getName()); assertEquals("C", res.getEmail()); assertEquals(Role.PLAYER, res.getRole());

        Captain c = new Captain(); c.setPosition("MED"); c.setJerseyNumber(1);
        assertEquals("MED", c.getPosition()); assertEquals(1, c.getJerseyNumber());
        Referee a = new Referee(); a.setName("A"); assertEquals("A", a.getName());
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
        team.setTeamName("T"); team.setEscudo("ESC"); team.setColoresUniforme("COL"); team.setPlayerEmails(new ArrayList<>());
        assertEquals("T", team.getTeamName()); assertEquals("ESC", team.getEscudo()); assertEquals("COL", team.getColoresUniforme());
        assertNotNull(team.getPlayerEmails());
        
        TeamRequestDTO teamReq2 = new TeamRequestDTO("T", "ESC", "COL", new ArrayList<>());
        assertEquals("T", teamReq2.getTeamName());

        Team teamModel = new Team();
        teamModel.setTeamName("T"); teamModel.setEscudo("ESC"); teamModel.setColoresUniforme("COL"); teamModel.setPlayers(new ArrayList<>());
        assertEquals("T", teamModel.getTeamName()); assertEquals("ESC", teamModel.getEscudo());
        assertEquals("COL", teamModel.getColoresUniforme()); assertNotNull(teamModel.getPlayers());

        TeamResponseDTO teamRes = new TeamResponseDTO();
        teamRes.setTeamName("T"); teamRes.setEscudo("ESC"); teamRes.setColoresUniforme("COL");
        assertEquals("T", teamRes.getTeamName());

        TeamResponseDTO teamRes2 = new TeamResponseDTO(teamModel);
        assertEquals("T", teamRes2.getTeamName());
    }

    @Test
    public void testOthers() {
        InvitationRequestDTO invReq = new InvitationRequestDTO();
        invReq.setCaptainEmail("C"); invReq.setPlayerEmail("J"); invReq.setTeamName("E");
        assertEquals("C", invReq.getCaptainEmail()); assertEquals("J", invReq.getPlayerEmail()); assertEquals("E", invReq.getTeamName());

        Invitation inv = new Invitation();
        inv.setId("1"); inv.setCaptainEmail("C"); inv.setPlayerEmail("J"); inv.setTeamName("E"); inv.setStatus("E");
        assertEquals("1", inv.getId()); assertEquals("C", inv.getCaptainEmail()); assertEquals("J", inv.getPlayerEmail());
        assertEquals("E", inv.getTeamName()); assertEquals("E", inv.getStatus());

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
        assertEquals("E", tp.getTeamName());
        assertEquals(3, tp.getMatchesPlayed());
        assertEquals(1, tp.getMatchesWon());
        assertEquals(1, tp.getMatchesDrawn());
        assertEquals(1, tp.getMatchesLost());
        assertEquals(3, tp.getGolesFavor());
        assertEquals(4, tp.getGolesContra());
        assertEquals(-1, tp.getGoalDifference());
        assertEquals(4, tp.getPoints());

        LoginRequestDTO lr = new LoginRequestDTO();
        lr.setEmail("C"); lr.setPassword("C");
        assertEquals("C", lr.getEmail()); assertEquals("C", lr.getPassword());

        LoginResponseDTO ls = new LoginResponseDTO();
        ls.setToken("T"); ls.setUser(new UserResponseDTO());
        assertEquals("T", ls.getToken()); assertNotNull(ls.getUser());
        
        LoginResponseDTO ls2 = new LoginResponseDTO("T", new UserResponseDTO());
        assertEquals("T", ls2.getToken()); 
    }
}
