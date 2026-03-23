package dependencias.dto;

public class InscripcionRequestDTO {
    private String nombreEquipo;
    private String nombreTorneo;
    private String comprobantePagoUrl;

    public InscripcionRequestDTO() {}

    public InscripcionRequestDTO(String nombreEquipo, String nombreTorneo, String comprobantePagoUrl) {
        this.nombreEquipo = nombreEquipo;
        this.nombreTorneo = nombreTorneo;
        this.comprobantePagoUrl = comprobantePagoUrl;
    }

    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getNombreTorneo() { return nombreTorneo; }
    public void setNombreTorneo(String nombreTorneo) { this.nombreTorneo = nombreTorneo; }
    public String getComprobantePagoUrl() { return comprobantePagoUrl; }
    public void setComprobantePagoUrl(String comprobantePagoUrl) { this.comprobantePagoUrl = comprobantePagoUrl; }
}
