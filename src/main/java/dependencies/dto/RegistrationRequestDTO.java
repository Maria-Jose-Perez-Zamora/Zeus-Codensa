package dependencies.dto;

public class RegistrationRequestDTO {
    private String nombreEquipo;
    private String tournamentName;
    private String comprobantePagoUrl;

    public RegistrationRequestDTO() {}

    public RegistrationRequestDTO(String nombreEquipo, String tournamentName, String comprobantePagoUrl) {
        this.nombreEquipo = nombreEquipo;
        this.tournamentName = tournamentName;
        this.comprobantePagoUrl = comprobantePagoUrl;
    }

    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getTournamentName() { return tournamentName; }
    public void setTournamentName(String tournamentName) { this.tournamentName = tournamentName; }
    public String getComprobantePagoUrl() { return comprobantePagoUrl; }
    public void setComprobantePagoUrl(String comprobantePagoUrl) { this.comprobantePagoUrl = comprobantePagoUrl; }
}
