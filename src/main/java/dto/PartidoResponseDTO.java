package dto;

import model.Partido;
import java.util.List;
import java.util.Map;

public class PartidoResponseDTO {
    private String id;
    private String equipoLocal;
    private String equipoVisitante;
    private String fechaPartido;
    private Integer marcadorLocal;
    private Integer marcadorVisitante;
    private String estado;
    private String nombreTorneo;
    private String correoArbitro;
    private Map<String, List<String>> alineaciones;

    public PartidoResponseDTO() {}

    public PartidoResponseDTO(Partido partido) {
        this.id = partido.getId();
        this.equipoLocal = partido.getEquipoLocal();
        this.equipoVisitante = partido.getEquipoVisitante();
        this.fechaPartido = partido.getFechaPartido();
        this.marcadorLocal = partido.getMarcadorLocal();
        this.marcadorVisitante = partido.getMarcadorVisitante();
        this.estado = partido.getEstado();
        this.nombreTorneo = partido.getNombreTorneo();
        this.correoArbitro = partido.getCorreoArbitro();
        this.alineaciones = partido.getAlineaciones();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(String equipoLocal) { this.equipoLocal = equipoLocal; }
    public String getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(String equipoVisitante) { this.equipoVisitante = equipoVisitante; }
    public String getFechaPartido() { return fechaPartido; }
    public void setFechaPartido(String fechaPartido) { this.fechaPartido = fechaPartido; }
    public Integer getMarcadorLocal() { return marcadorLocal; }
    public void setMarcadorLocal(Integer marcadorLocal) { this.marcadorLocal = marcadorLocal; }
    public Integer getMarcadorVisitante() { return marcadorVisitante; }
    public void setMarcadorVisitante(Integer marcadorVisitante) { this.marcadorVisitante = marcadorVisitante; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getNombreTorneo() { return nombreTorneo; }
    public void setNombreTorneo(String nombreTorneo) { this.nombreTorneo = nombreTorneo; }
    public String getCorreoArbitro() { return correoArbitro; }
    public void setCorreoArbitro(String correoArbitro) { this.correoArbitro = correoArbitro; }
    public Map<String, List<String>> getAlineaciones() { return alineaciones; }
    public void setAlineaciones(Map<String, List<String>> alineaciones) { this.alineaciones = alineaciones; }
}
