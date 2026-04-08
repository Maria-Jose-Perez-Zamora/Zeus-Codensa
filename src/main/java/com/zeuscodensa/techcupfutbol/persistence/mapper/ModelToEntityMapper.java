package com.zeuscodensa.techcupfutbol.persistence.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zeuscodensa.techcupfutbol.core.model.Match;
import com.zeuscodensa.techcupfutbol.core.model.Registration;
import com.zeuscodensa.techcupfutbol.core.model.Team;
import com.zeuscodensa.techcupfutbol.core.model.Tournament;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.model.Invitation;
import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.persistence.entity.InvitationEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.MatchEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.RegistrationEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.TeamEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.TournamentEntity;
import com.zeuscodensa.techcupfutbol.persistence.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class ModelToEntityMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ModelToEntityMapper() {
    }

    public static UserEntity toUserEntity(User model) {
        if (model == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setName(model.getName());
        entity.setEmail(model.getEmail());
        entity.setPassword(model.getPassword());
        entity.setPhoto(model.getPhoto());
        entity.setRole(model.getRole());
        entity.setType(model.getType());

        if (model instanceof Player player) {
            entity.setPosition(player.getPosition());
            entity.setJerseyNumber(player.getJerseyNumber());
        }

        return entity;
    }

    public static TeamEntity toTeamEntity(Team model) {
        if (model == null) {
            return null;
        }

        TeamEntity entity = new TeamEntity();
        entity.setTeamName(model.getTeamName());
        entity.setEscudo(model.getEscudo());
        entity.setColoresUniforme(model.getColoresUniforme());

        List<UserEntity> players = new ArrayList<>();
        if (model.getPlayers() != null) {
            for (User player : model.getPlayers()) {
                players.add(toUserEntity(player));
            }
        }
        entity.setPlayers(players);

        return entity;
    }

    public static TournamentEntity toTournamentEntity(Tournament model) {
        if (model == null) {
            return null;
        }

        TournamentEntity entity = new TournamentEntity();
        entity.setId(model.getId() != null ? model.getId() : UUID.randomUUID().toString());
        entity.setTournamentName(model.getTournamentName());
        entity.setFechaInicio(model.getFechaInicio());
        entity.setFechaFin(model.getFechaFin());
        entity.setNumeroEquipos(model.getNumeroEquipos());
        entity.setCostoInscripcion(model.getCostoInscripcion());
        entity.setStatus(model.getStatus());
        entity.setRules(model.getRules());
        entity.setFechaCierreInscripciones(model.getFechaCierreInscripciones());
        entity.setFechaInicioFaseGrupos(model.getFechaInicioFaseGrupos());
        entity.setHorariosPartidos(model.getHorariosPartidos() != null ? new ArrayList<>(model.getHorariosPartidos()) : new ArrayList<>());
        entity.setCanchas(model.getCanchas() != null ? new ArrayList<>(model.getCanchas()) : new ArrayList<>());
        entity.setSanctions(model.getSanctions());
        entity.setCampeon(model.getCampeon());
        return entity;
    }

    public static RegistrationEntity toRegistrationEntity(Registration model) {
        if (model == null) {
            return null;
        }

        RegistrationEntity entity = new RegistrationEntity();
        entity.setId(model.getId() != null ? model.getId() : UUID.randomUUID().toString());
        entity.setTeamName(model.getTeamName());
        entity.setTournamentName(model.getTournamentName());
        entity.setStatus(model.getStatus());
        entity.setComprobantePagoUrl(model.getComprobantePagoUrl());
        entity.setFechaInscripcion(model.getFechaInscripcion());
        return entity;
    }

    public static MatchEntity toMatchEntity(Match model) {
        if (model == null) {
            return null;
        }

        MatchEntity entity = new MatchEntity();
        entity.setId(model.getId() != null ? model.getId() : UUID.randomUUID().toString());
        entity.setHomeTeam(model.getHomeTeam());
        entity.setAwayTeam(model.getAwayTeam());
        entity.setMatchDate(model.getMatchDate());
        entity.setHomeScore(model.getHomeScore());
        entity.setAwayScore(model.getAwayScore());
        entity.setStatus(model.getStatus());
        entity.setTournamentName(model.getTournamentName());
        entity.setRefereeEmail(model.getRefereeEmail());
        entity.setAlineacionesJson(writeJson(model.getAlineaciones()));
        entity.setGoalsJson(writeJson(model.getGoles()));
        entity.setYellowCardsJson(writeJson(model.getYellowCards()));
        entity.setRedCardsJson(writeJson(model.getRedCards()));
        return entity;
    }

    public static InvitationEntity toInvitationEntity(Invitation model) {
        if (model == null) {
            return null;
        }

        InvitationEntity entity = new InvitationEntity();
        entity.setId(model.getId() != null ? model.getId() : UUID.randomUUID().toString());
        entity.setCaptainEmail(model.getCaptainEmail());
        entity.setPlayerEmail(model.getPlayerEmail());
        entity.setTeamName(model.getTeamName());
        entity.setStatus(model.getStatus());
        return entity;
    }

    private static String writeJson(Object value) {
        if (value == null) {
            return "{}";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("No fue posible serializar el campo del partido", ex);
        }
    }
}
