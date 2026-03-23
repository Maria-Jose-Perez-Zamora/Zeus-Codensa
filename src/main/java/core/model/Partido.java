package core.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Partido {
    private String id;
    private String equipoLocal;
    private String equipoVisitante;
    private String fechaPartido;
    private Integer marcadorLocal;
    private Integer marcadorVisitante;
    private String estado;
    private String nombreTorneo;
    private String correoArbitro;
    // Alineaciones: mapa equipoNombre -> lista de correos de jugadores
    private Map<String, List<String>> alineaciones;
    // Goleadores: mapa correoJugador -> cantidad de goles anotados
    private Map<String, Integer> goles;
    // Tarjetas (RF-006): mapa nombreJugador(minuto) -> motivo/cantidad
    private Map<String, List<String>> tarjetasAmarillas;
    private Map<String, List<String>> tarjetasRojas;

    public Partido() {
        this.alineaciones = new HashMap<>();
        this.goles = new HashMap<>();
        this.tarjetasAmarillas = new HashMap<>();
        this.tarjetasRojas = new HashMap<>();
    }

    public Partido(String equipoLocal, String equipoVisitante, String fechaPartido, String nombreTorneo) {
        this.id = UUID.randomUUID().toString();
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fechaPartido = fechaPartido;
        this.nombreTorneo = nombreTorneo;
        this.estado = "PROGRAMADO";
        this.marcadorLocal = 0;
        this.marcadorVisitante = 0;
        this.alineaciones = new HashMap<>();
        this.goles = new HashMap<>();
        this.tarjetasAmarillas = new HashMap<>();
        this.tarjetasRojas = new HashMap<>();
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
    public Map<String, Integer> getGoles() { return goles; }
    public void setGoles(Map<String, Integer> goles) { this.goles = goles; }
    public Map<String, List<String>> getTarjetasAmarillas() { return tarjetasAmarillas; }
    public void setTarjetasAmarillas(Map<String, List<String>> tarjetasAmarillas) { this.tarjetasAmarillas = tarjetasAmarillas; }
    public Map<String, List<String>> getTarjetasRojas() { return tarjetasRojas; }
    public void setTarjetasRojas(Map<String, List<String>> tarjetasRojas) { this.tarjetasRojas = tarjetasRojas; }
}
