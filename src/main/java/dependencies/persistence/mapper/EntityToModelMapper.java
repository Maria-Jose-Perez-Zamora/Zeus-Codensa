package dependencies.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.model.AdministradorSistema;
import core.model.Captain;
import core.model.Invitation;
import core.model.Match;
import core.model.Player;
import core.model.Referee;
import core.model.Registration;
import core.model.Role;
import core.model.Team;
import core.model.Tournament;
import core.model.TournamentOrganizer;
import core.model.User;
import dependencies.persistence.entity.InvitationEntity;
import dependencies.persistence.entity.MatchEntity;
import dependencies.persistence.entity.RegistrationEntity;
import dependencies.persistence.entity.TeamEntity;
import dependencies.persistence.entity.TournamentEntity;
import dependencies.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class EntityToModelMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private EntityToModelMapper() {
    }

    public static User toUserModel(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = createUserByRole(entity.getRole());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setPhoto(entity.getPhoto());
        user.setRole(entity.getRole());
        user.setType(entity.getType());

        if (user instanceof Player player) {
            player.setPosition(entity.getPosition());
            player.setJerseyNumber(entity.getJerseyNumber());
        }

        return user;
    }

    public static Team toTeamModel(TeamEntity entity) {
        if (entity == null) {
            return null;
        }

        Team model = new Team();
        model.setTeamName(entity.getTeamName());
        model.setEscudo(entity.getEscudo());
        model.setColoresUniforme(entity.getColoresUniforme());

        List<User> players = new ArrayList<>();
        if (entity.getPlayers() != null) {
            for (UserEntity playerEntity : entity.getPlayers()) {
                players.add(toUserModel(playerEntity));
            }
        }
        model.setPlayers(players);

        return model;
    }

    public static Tournament toTournamentModel(TournamentEntity entity) {
        if (entity == null) {
            return null;
        }

        Tournament model = new Tournament();
        model.setId(entity.getId());
        model.setTournamentName(entity.getTournamentName());
        model.setFechaInicio(entity.getFechaInicio());
        model.setFechaFin(entity.getFechaFin());
        model.setNumeroEquipos(entity.getNumeroEquipos());
        model.setCostoInscripcion(entity.getCostoInscripcion());
        model.setStatus(entity.getStatus());
        model.setRules(entity.getRules());
        model.setFechaCierreInscripciones(entity.getFechaCierreInscripciones());
        model.setFechaInicioFaseGrupos(entity.getFechaInicioFaseGrupos());
        model.setHorariosPartidos(entity.getHorariosPartidos() != null ? new ArrayList<>(entity.getHorariosPartidos()) : new ArrayList<>());
        model.setCanchas(entity.getCanchas() != null ? new ArrayList<>(entity.getCanchas()) : new ArrayList<>());
        model.setSanctions(entity.getSanctions());

        return model;
    }

    public static Registration toRegistrationModel(RegistrationEntity entity) {
        if (entity == null) {
            return null;
        }

        Registration model = new Registration();
        model.setId(entity.getId());
        model.setTeamName(entity.getTeamName());
        model.setTournamentName(entity.getTournamentName());
        model.setStatus(entity.getStatus());
        model.setComprobantePagoUrl(entity.getComprobantePagoUrl());
        model.setFechaInscripcion(entity.getFechaInscripcion());

        return model;
    }

    public static Match toMatchModel(MatchEntity entity) {
        if (entity == null) {
            return null;
        }

        Match model = new Match();
        model.setId(entity.getId());
        model.setHomeTeam(entity.getHomeTeam());
        model.setAwayTeam(entity.getAwayTeam());
        model.setMatchDate(entity.getMatchDate());
        model.setHomeScore(entity.getHomeScore());
        model.setAwayScore(entity.getAwayScore());
        model.setStatus(entity.getStatus());
        model.setTournamentName(entity.getTournamentName());
        model.setRefereeEmail(entity.getRefereeEmail());
        model.setAlineaciones(readMapStringList(entity.getAlineacionesJson()));
        model.setGoles(readMapStringInteger(entity.getGoalsJson()));
        model.setYellowCards(readMapStringList(entity.getYellowCardsJson()));
        model.setRedCards(readMapStringList(entity.getRedCardsJson()));

        return model;
    }

    public static Invitation toInvitationModel(InvitationEntity entity) {
        if (entity == null) {
            return null;
        }

        Invitation model = new Invitation();
        model.setId(entity.getId());
        model.setCaptainEmail(entity.getCaptainEmail());
        model.setPlayerEmail(entity.getPlayerEmail());
        model.setTeamName(entity.getTeamName());
        model.setStatus(entity.getStatus());

        return model;
    }

    private static User createUserByRole(Role role) {
        if (role == null) {
            return new Player();
        }

        return switch (role) {
            case ADMINISTRADOR_SISTEMA -> new AdministradorSistema();
            case TOURNAMENT_ORGANIZER -> new TournamentOrganizer();
            case REFEREE -> new Referee();
            case CAPTAIN -> new Captain();
            case PLAYER -> new Player();
        };
    }

    private static Map<String, List<String>> readMapStringList(String value) {
        if (value == null || value.isBlank()) {
            return new HashMap<>();
        }

        try {
            return OBJECT_MAPPER.readValue(value, new TypeReference<Map<String, List<String>>>() { });
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("No fue posible deserializar mapa<String, List<String>>", ex);
        }
    }

    private static Map<String, Integer> readMapStringInteger(String value) {
        if (value == null || value.isBlank()) {
            return new HashMap<>();
        }

        try {
            return OBJECT_MAPPER.readValue(value, new TypeReference<Map<String, Integer>>() { });
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("No fue posible deserializar mapa<String, Integer>", ex);
        }
    }
}
