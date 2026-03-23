package core.model;

public class KnockoutBracket {
    private String phase;
    private String homeTeam;
    private String awayTeam;
    private String ganador;

    public KnockoutBracket(String phase, String homeTeam, String awayTeam) {
        this.phase = phase;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public String getPhase() { return phase; }
    public void setPhase(String phase) { this.phase = phase; }
    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }
    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }
    public String getGanador() { return ganador; }
    public void setGanador(String ganador) { this.ganador = ganador; }
}
