package dependencies.persistence.mapper;

import core.model.Captain;
import core.model.Invitation;
import core.model.Match;
import core.model.Player;
import core.model.Registration;
import core.model.Role;
import core.model.Team;
import core.model.Tournament;
import core.model.User;
import core.model.UserType;
import dependencies.persistence.entity.InvitationEntity;
import dependencies.persistence.entity.MatchEntity;
import dependencies.persistence.entity.RegistrationEntity;
import dependencies.persistence.entity.TeamEntity;
import dependencies.persistence.entity.TournamentEntity;
import dependencies.persistence.entity.UserEntity;
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
}
