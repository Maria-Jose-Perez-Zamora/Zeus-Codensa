package dependencies.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "matches")
public class MatchEntity {

    @Id
    private String id;

    @Column(nullable = false)
    private String homeTeam;

    @Column(nullable = false)
    private String awayTeam;

    @Column(nullable = false)
    private String matchDate;

    private Integer homeScore;

    private Integer awayScore;

    private String status;

    private String tournamentName;

    private String refereeEmail;

    @Lob
    private String alineacionesJson;

    @Lob
    private String goalsJson;

    @Lob
    private String yellowCardsJson;

    @Lob
    private String redCardsJson;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public Integer getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(Integer homeScore) {
        this.homeScore = homeScore;
    }

    public Integer getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(Integer awayScore) {
        this.awayScore = awayScore;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getRefereeEmail() {
        return refereeEmail;
    }

    public void setRefereeEmail(String refereeEmail) {
        this.refereeEmail = refereeEmail;
    }

    public String getAlineacionesJson() {
        return alineacionesJson;
    }

    public void setAlineacionesJson(String alineacionesJson) {
        this.alineacionesJson = alineacionesJson;
    }

    public String getGoalsJson() {
        return goalsJson;
    }

    public void setGoalsJson(String goalsJson) {
        this.goalsJson = goalsJson;
    }

    public String getYellowCardsJson() {
        return yellowCardsJson;
    }

    public void setYellowCardsJson(String yellowCardsJson) {
        this.yellowCardsJson = yellowCardsJson;
    }

    public String getRedCardsJson() {
        return redCardsJson;
    }

    public void setRedCardsJson(String redCardsJson) {
        this.redCardsJson = redCardsJson;
    }
}
