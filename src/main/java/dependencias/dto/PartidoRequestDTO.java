package dependencias.dto;

public class PartidoRequestDTO {
    private String equipoLocal;
    private String equipoVisitante;
    private String fechaPartido;
    private String nombreTorneo;

    public PartidoRequestDTO() {}

    public PartidoRequestDTO(String equipoLocal, String equipoVisitante, String fechaPartido, String nombreTorneo) {
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fechaPartido = fechaPartido;
        this.nombreTorneo = nombreTorneo;
    }

    public String getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(String equipoLocal) { this.equipoLocal = equipoLocal; }
    public String getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(String equipoVisitante) { this.equipoVisitante = equipoVisitante; }
    public String getFechaPartido() { return fechaPartido; }
    public void setFechaPartido(String fechaPartido) { this.fechaPartido = fechaPartido; }
    public String getNombreTorneo() { return nombreTorneo; }
    public void setNombreTorneo(String nombreTorneo) { this.nombreTorneo = nombreTorneo; }
}
