package com.zeuscodensa.techcupfutbol.persistence.mapper;

import com.zeuscodensa.techcupfutbol.core.model.Captain;
import com.zeuscodensa.techcupfutbol.core.model.Invitation;
import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.model.Registration;
import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.model.UserType;
import com.zeuscodensa.techcupfutbol.persistence.entity.InvitationEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.MatchEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.RegistrationEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.TeamEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.TournamentEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PersistenceMapperTest {

    @Test
    void shouldMapUserModelToEntityAndBack() {
        Captain captain = new Captain();
        captain.setName("Laura");
        captain.setEmail("laura@mail.com");
        captain.setPassword("secret");
        captain.setRole(Role.CAPTAIN);
        captain.setType(UserType.EXTERNAL);
        captain.setPosition("Defensa");
        captain.setJerseyNumber(5);

        UserEntity entity = ModelToEntityMapper.toUserEntity(captain);
        User mappedBack = EntityToModelMapper.toUserModel(entity);

        assertEquals("Laura", entity.getName());
        assertEquals(Role.CAPTAIN, entity.getRole());
        assertEquals("Defensa", entity.getPosition());
        assertEquals(5, entity.getJerseyNumber());

        assertInstanceOf(Captain.class, mappedBack);
        assertEquals("laura@mail.com", mappedBack.getEmail());
        assertEquals(UserType.EXTERNAL, mappedBack.getType());
    }

    @Test
    void shouldMapTeamWithPlayers() {
        Player player = new Player();
        player.setName("Carlos");
        player.setEmail("carlos@mail.com");
        player.setRole(Role.PLAYER);
        player.setType(UserType.INTERNAL);

        Team team = new Team("Los Tigres");
        team.setEscudo("escudo.png");
        team.setColoresUniforme("Azul/Blanco");
        team.setPlayers(List.of(player));

        TeamEntity entity = ModelToEntityMapper.toTeamEntity(team);
        Team model = EntityToModelMapper.toTeamModel(entity);

        assertEquals("Los Tigres", entity.getTeamName());
        assertEquals(1, entity.getPlayers().size());

        assertEquals("Los Tigres", model.getTeamName());
        assertEquals(1, model.getPlayers().size());
        assertEquals("carlos@mail.com", model.getPlayers().get(0).getEmail());
    }

    @Test
    void shouldMapTournamentRoundTrip() {
        Tournament tournament = new Tournament("Apertura");
        tournament.setFechaInicio("2026-04-01");
        tournament.setFechaFin("2026-04-30");
        tournament.setNumeroEquipos(16);
        tournament.setCostoInscripcion(120000.0);
        tournament.setStatus("OPEN");
        tournament.setRules("FIFA");
        tournament.setFechaCierreInscripciones("2026-03-29");
        tournament.setFechaInicioFaseGrupos("2026-04-01");
        tournament.setHorariosPartidos(List.of("08:00", "10:00"));
        tournament.setCanchas(List.of("Cancha 1", "Cancha 2"));
        tournament.setSanctions("Tarjeta roja: 1 partido");

        TournamentEntity entity = ModelToEntityMapper.toTournamentEntity(tournament);
        Tournament mappedBack = EntityToModelMapper.toTournamentModel(entity);

        assertNotNull(entity.getId());
        assertEquals("Apertura", mappedBack.getTournamentName());
        assertEquals(16, mappedBack.getNumeroEquipos());
        assertEquals(2, mappedBack.getHorariosPartidos().size());
        assertEquals(2, mappedBack.getCanchas().size());
    }

    @Test
    void shouldMapRegistrationRoundTrip() {
        Registration registration = new Registration();
        registration.setId("reg-1");
        registration.setTeamName("Los Tigres");
        registration.setTournamentName("Apertura");
        registration.setStatus("PENDING");
        registration.setComprobantePagoUrl("http://pago.com/1");
        registration.setFechaInscripcion("2026-03-27T10:00:00");

        RegistrationEntity entity = ModelToEntityMapper.toRegistrationEntity(registration);
        Registration mappedBack = EntityToModelMapper.toRegistrationModel(entity);

        assertEquals("reg-1", entity.getId());
        assertEquals("Los Tigres", mappedBack.getTeamName());
        assertEquals("Apertura", mappedBack.getTournamentName());
    }

    @Test
    void shouldMapMatchRoundTripIncludingJsonFields() {
        Match match = new Match();
        match.setId("match-1");
        match.setHomeTeam("Los Tigres");
        match.setAwayTeam("Halcones");
        match.setMatchDate("2026-04-10T08:00:00");
        match.setHomeScore(2);
        match.setAwayScore(1);
        match.setStatus("FINISHED");
        match.setTournamentName("Apertura");
        match.setRefereeEmail("ref@mail.com");
        match.setAlineaciones(Map.of("Los Tigres", List.of("p1@mail.com", "p2@mail.com")));
        match.setGoles(Map.of("p1@mail.com", 2));
        match.setYellowCards(Map.of("p2@mail.com", List.of("52")));
        match.setRedCards(Map.of("p3@mail.com", List.of("80")));

        MatchEntity entity = ModelToEntityMapper.toMatchEntity(match);
        Match mappedBack = EntityToModelMapper.toMatchModel(entity);

        assertEquals("match-1", entity.getId());
        assertNotNull(entity.getAlineacionesJson());
        assertEquals(2, mappedBack.getHomeScore());
        assertEquals(1, mappedBack.getAwayScore());
        assertEquals(2, mappedBack.getGoles().get("p1@mail.com"));
        assertEquals(2, mappedBack.getAlineaciones().get("Los Tigres").size());
    }

    @Test
    void shouldMapInvitationRoundTrip() {
        Invitation invitation = new Invitation();
        invitation.setId("inv-1");
        invitation.setCaptainEmail("captain@mail.com");
        invitation.setPlayerEmail("player@mail.com");
        invitation.setTeamName("Los Tigres");
        invitation.setStatus("PENDING");

        InvitationEntity entity = ModelToEntityMapper.toInvitationEntity(invitation);
        Invitation mappedBack = EntityToModelMapper.toInvitationModel(entity);

        assertEquals("inv-1", entity.getId());
        assertEquals("captain@mail.com", mappedBack.getCaptainEmail());
        assertEquals("player@mail.com", mappedBack.getPlayerEmail());
    }

    @Test
    void shouldReturnNullWhenInputsAreNull() {
        // Entity to Model
        org.junit.jupiter.api.Assertions.assertNull(EntityToModelMapper.toUserModel(null));
        org.junit.jupiter.api.Assertions.assertNull(EntityToModelMapper.toTeamModel(null));
        org.junit.jupiter.api.Assertions.assertNull(EntityToModelMapper.toTournamentModel(null));
        org.junit.jupiter.api.Assertions.assertNull(EntityToModelMapper.toRegistrationModel(null));
        org.junit.jupiter.api.Assertions.assertNull(EntityToModelMapper.toMatchModel(null));
        org.junit.jupiter.api.Assertions.assertNull(EntityToModelMapper.toInvitationModel(null));

        // Model to Entity
        org.junit.jupiter.api.Assertions.assertNull(ModelToEntityMapper.toUserEntity(null));
        org.junit.jupiter.api.Assertions.assertNull(ModelToEntityMapper.toTeamEntity(null));
        org.junit.jupiter.api.Assertions.assertNull(ModelToEntityMapper.toTournamentEntity(null));
        org.junit.jupiter.api.Assertions.assertNull(ModelToEntityMapper.toRegistrationEntity(null));
        org.junit.jupiter.api.Assertions.assertNull(ModelToEntityMapper.toMatchEntity(null));
        org.junit.jupiter.api.Assertions.assertNull(ModelToEntityMapper.toInvitationEntity(null));
    }

    @Test
    void shouldMapDifferentRolesAndNullRole() {
        UserEntity admin = new UserEntity(); admin.setRole(Role.ADMINISTRADOR_SISTEMA);
        UserEntity org = new UserEntity(); org.setRole(Role.TOURNAMENT_ORGANIZER);
        UserEntity ref = new UserEntity(); ref.setRole(Role.REFEREE);
        UserEntity nullRole = new UserEntity(); nullRole.setRole(null);

        assertInstanceOf(com.zeuscodensa.techcupfutbol.core.model.SystemAdministrator.class, EntityToModelMapper.toUserModel(admin));
        assertInstanceOf(com.zeuscodensa.techcupfutbol.core.model.TournamentOrganizer.class, EntityToModelMapper.toUserModel(org));
        assertInstanceOf(com.zeuscodensa.techcupfutbol.core.model.Referee.class, EntityToModelMapper.toUserModel(ref));
        assertInstanceOf(com.zeuscodensa.techcupfutbol.core.model.Player.class, EntityToModelMapper.toUserModel(nullRole)); // default
    }

    @Test
    void shouldMapEntitiesWithNullLists() {
        // Team with null players
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setTeamName("Los Tigres");
        teamEntity.setPlayers(null);
        Team teamModel = EntityToModelMapper.toTeamModel(teamEntity);
        assertEquals("Los Tigres", teamModel.getTeamName());
        assertNotNull(teamModel.getPlayers());
        assertEquals(0, teamModel.getPlayers().size());

        // Tournament with null lists
        TournamentEntity tornEntity = new TournamentEntity();
        tornEntity.setHorariosPartidos(null);
        tornEntity.setCanchas(null);
        Tournament tornModel = EntityToModelMapper.toTournamentModel(tornEntity);
        assertNotNull(tornModel.getHorariosPartidos());
        assertNotNull(tornModel.getCanchas());

        // Match with null json
        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setAlineacionesJson(null);
        matchEntity.setGoalsJson("");
        matchEntity.setYellowCardsJson("   ");
        matchEntity.setRedCardsJson(null);
        Match matchModel = EntityToModelMapper.toMatchModel(matchEntity);
        assertNotNull(matchModel.getAlineaciones());
        assertEquals(0, matchModel.getAlineaciones().size());
        assertNotNull(matchModel.getGoles());
    }

    @Test
    void shouldMapModelsWithNullLists() {
        // Team with null players
        Team teamModel = new Team("Los Tigres");
        teamModel.setPlayers(null);
        TeamEntity teamEntity = ModelToEntityMapper.toTeamEntity(teamModel);
        assertEquals("Los Tigres", teamEntity.getTeamName());
        assertNotNull(teamEntity.getPlayers());
        assertEquals(0, teamEntity.getPlayers().size());

        // Tournament with null lists
        Tournament tornModel = new Tournament("Apertura");
        tornModel.setHorariosPartidos(null);
        tornModel.setCanchas(null);
        TournamentEntity tornEntity = ModelToEntityMapper.toTournamentEntity(tornModel);
        assertNotNull(tornEntity.getHorariosPartidos());
        assertEquals(0, tornEntity.getHorariosPartidos().size());
        assertNotNull(tornEntity.getCanchas());

        // Match with null maps
        Match match = new Match();
        match.setAlineaciones(null);
        match.setGoles(null);
        match.setYellowCards(null);
        match.setRedCards(null);
        MatchEntity matchEntity = ModelToEntityMapper.toMatchEntity(match);
        assertEquals("{}", matchEntity.getAlineacionesJson());
        assertEquals("{}", matchEntity.getGoalsJson());
    }
}
