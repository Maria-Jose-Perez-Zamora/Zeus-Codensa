package dependencies.dto;

import core.model.Tournament;
import java.util.List;

public class TournamentResponseDTO {
    private String id;
    private String tournamentName;
    private String fechaInicio;
    private String fechaFin;
    private Integer numeroEquipos;
    private Double costoInscripcion;
    private String status;

    // Advanced Configuration (RF-005)
    private String rules;
    private String fechaCierreInscripciones;
    private String fechaInicioFaseGrupos;
    private List<String> horariosPartidos;
    private List<String> canchas;
    private String sanctions;

    public TournamentResponseDTO() {}
    
    public TournamentResponseDTO(Tournament tournament) {
        this.id = tournament.getId();
        this.tournamentName = tournament.getTournamentName();
        this.fechaInicio = tournament.getFechaInicio();
        this.fechaFin = tournament.getFechaFin();
        this.numeroEquipos = tournament.getNumeroEquipos();
        this.costoInscripcion = tournament.getCostoInscripcion();
        this.status = tournament.getStatus();
        this.rules = tournament.getRules();
        this.fechaCierreInscripciones = tournament.getFechaCierreInscripciones();
        this.fechaInicioFaseGrupos = tournament.getFechaInicioFaseGrupos();
        this.horariosPartidos = tournament.getHorariosPartidos();
        this.canchas = tournament.getCanchas();
        this.sanctions = tournament.getSanctions();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public Integer getNumeroEquipos() { return numeroEquipos; }
    public void setNumeroEquipos(Integer numeroEquipos) { this.numeroEquipos = numeroEquipos; }
    public Double getCostoInscripcion() { return costoInscripcion; }
    public void setCostoInscripcion(Double costoInscripcion) { this.costoInscripcion = costoInscripcion; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getRules() { return rules; }
    public void setRules(String rules) { this.rules = rules; }
    public String getFechaCierreInscripciones() { return fechaCierreInscripciones; }
    public void setFechaCierreInscripciones(String fechaCierreInscripciones) { this.fechaCierreInscripciones = fechaCierreInscripciones; }
    public String getFechaInicioFaseGrupos() { return fechaInicioFaseGrupos; }
    public void setFechaInicioFaseGrupos(String fechaInicioFaseGrupos) { this.fechaInicioFaseGrupos = fechaInicioFaseGrupos; }
    public List<String> getHorariosPartidos() { return horariosPartidos; }
    public void setHorariosPartidos(List<String> horariosPartidos) { this.horariosPartidos = horariosPartidos; }
    public List<String> getCanchas() { return canchas; }
    public void setCanchas(List<String> canchas) { this.canchas = canchas; }
    public String getSanctions() { return sanctions; }
    public void setSanctions(String sanctions) { this.sanctions = sanctions; }
}
