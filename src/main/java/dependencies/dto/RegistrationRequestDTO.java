package dependencies.dto;

public class RegistrationRequestDTO {
    private String nombreEquipo;
    private String nombreTorneo;
    private String comprobantePagoUrl;

    public RegistrationRequestDTO() {}

    public RegistrationRequestDTO(String nombreEquipo, String nombreTorneo, String comprobantePagoUrl) {
        this.nombreEquipo = nombreEquipo;
        this.nombreTorneo = nombreTorneo;
        this.comprobantePagoUrl = comprobantePagoUrl;
    }

    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getNombreTorneo() { return nombreTorneo; }
    public void setNombreTorneo(String nombreTorneo) { this.nombreTorneo = nombreTorneo; }
    
    // English adapters for partially translated classes
    public String getTeamName() { return this.nombreEquipo; }
    public void setTeamName(String teamName) { this.nombreEquipo = teamName; }
    public String getTournamentName() { return this.nombreTorneo; }
    public void setTournamentName(String tournamentName) { this.nombreTorneo = tournamentName; }
    
    public String getComprobantePagoUrl() { return comprobantePagoUrl; }
    public void setComprobantePagoUrl(String comprobantePagoUrl) { this.comprobantePagoUrl = comprobantePagoUrl; }
}
