package dependencias.util;

import core.model.User;
import core.model.Team;
import core.model.Torneo;
import core.model.Inscripcion;
import core.model.Partido;
import core.model.Invitacion;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    public static List<User> users = new ArrayList<>();
    public static List<Team> teams = new ArrayList<>();
    public static List<Torneo> torneos = new ArrayList<>();
    public static List<Inscripcion> inscripciones = new ArrayList<>();
    public static List<Partido> partidos = new ArrayList<>();
    public static List<Invitacion> invitaciones = new ArrayList<>();

    public static void clearAll() {
        users.clear();
        teams.clear();
        torneos.clear();
        inscripciones.clear();
        partidos.clear();
        invitaciones.clear();
    }
}