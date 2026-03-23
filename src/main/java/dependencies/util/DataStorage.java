package dependencies.util;

import core.model.User;
import core.model.Team;
import core.model.Tournament;
import core.model.Registration;
import core.model.Match;
import core.model.Invitation;
import java.util.ArrayList;
import java.util.List;

public class DataStorage {
    public static List<User> users = new ArrayList<>();
    public static List<Team> teams = new ArrayList<>();
    public static List<Tournament> tournaments = new ArrayList<>();
    public static List<Registration> registrations = new ArrayList<>();
    public static List<Match> matches = new ArrayList<>();
    public static List<Invitation> invitations = new ArrayList<>();

    public static void clearAll() {
        users.clear();
        teams.clear();
        tournaments.clear();
        registrations.clear();
        matches.clear();
        invitations.clear();
    }
}