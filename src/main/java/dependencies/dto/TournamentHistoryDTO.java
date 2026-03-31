package dependencies.dto;

import core.model.Tournament;

public class TournamentHistoryDTO {
    private String id;
    private String tournamentName;
    private String fechaInicio;
    private String fechaFin;
    private String status;
    private String campeon;

    public TournamentHistoryDTO() {}

    public TournamentHistoryDTO(Tournament tournament) {
        this.id = tournament.getId();
        this.tournamentName = tournament.getTournamentName();
        this.fechaInicio = tournament.getFechaInicio();
        this.fechaFin = tournament.getFechaFin();
        this.status = tournament.getStatus();
        this.campeon = tournament.getCampeon();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCampeon() { return campeon; }
    public void setCampeon(String campeon) { this.campeon = campeon; }
}
