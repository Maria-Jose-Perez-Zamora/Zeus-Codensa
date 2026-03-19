package util;

import model.User;
import model.Team;
import model.Torneo;
import model.Inscripcion;
import model.Partido;
import model.Invitacion;
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