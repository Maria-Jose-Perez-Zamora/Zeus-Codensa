package dto;

import java.util.List;

public class TeamRequestDTO {
    private String nombreEquipo;
    private String escudo;
    private String coloresUniforme;
    private List<String> jugadorCorreos;

    public TeamRequestDTO() {}

    public TeamRequestDTO(String nombreEquipo, String escudo, String coloresUniforme, List<String> jugadorCorreos) {
        this.nombreEquipo = nombreEquipo;
        this.escudo = escudo;
        this.coloresUniforme = coloresUniforme;
        this.jugadorCorreos = jugadorCorreos;
    }

    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }
    public String getColoresUniforme() { return coloresUniforme; }
    public void setColoresUniforme(String coloresUniforme) { this.coloresUniforme = coloresUniforme; }
    public List<String> getJugadorCorreos() { return jugadorCorreos; }
    public void setJugadorCorreos(List<String> jugadorCorreos) { this.jugadorCorreos = jugadorCorreos; }
}
