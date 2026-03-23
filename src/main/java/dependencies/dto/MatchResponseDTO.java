package dependencies.dto;

import core.model.Match;
import java.util.List;
import java.util.Map;

public class MatchResponseDTO {
    private String id;
    private String homeTeam;
    private String awayTeam;
    private String matchDate;
    private Integer homeScore;
    private Integer awayScore;
    private String status;
    private String tournamentName;
    private String refereeEmail;
    private Map<String, List<String>> alineaciones;
    private Map<String, List<String>> yellowCards;
    private Map<String, List<String>> redCards;

    public MatchResponseDTO() {}

    public MatchResponseDTO(Match match) {
        this.id = match.getId();
        this.homeTeam = match.getHomeTeam();
        this.awayTeam = match.getAwayTeam();
        this.matchDate = match.getMatchDate();
        this.homeScore = match.getHomeScore();
        this.awayScore = match.getAwayScore();
        this.status = match.getStatus();
        this.tournamentName = match.getTournamentName();
        this.refereeEmail = match.getRefereeEmail();
        this.alineaciones = match.getAlineaciones();
        this.yellowCards = match.getYellowCards();
        this.redCards = match.getRedCards();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    public String getMatchDate() { return matchDate; }
    public void setMatchDate(String matchDate) { this.matchDate = matchDate; }
    public Integer getHomeScore() { return homeScore; }
    public void setHomeScore(Integer homeScore) { this.homeScore = homeScore; }
    public Integer getAwayScore() { return awayScore; }
    public void setAwayScore(Integer awayScore) { this.awayScore = awayScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    public String getRefereeEmail() { return refereeEmail; }
    public void setRefereeEmail(String refereeEmail) { this.refereeEmail = refereeEmail; }
    public Map<String, List<String>> getAlineaciones() { return alineaciones; }
    public void setAlineaciones(Map<String, List<String>> alineaciones) { this.alineaciones = alineaciones; }
    public Map<String, List<String>> getYellowCards() { return yellowCards; }
    public void setYellowCards(Map<String, List<String>> yellowCards) { this.yellowCards = yellowCards; }
    public Map<String, List<String>> getRedCards() { return redCards; }
    public void setRedCards(Map<String, List<String>> redCards) { this.redCards = redCards; }
}
