package core.model;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String nombreEquipo;
    private String escudo;
    private String coloresUniforme;
    private List<User> jugadores;

    public Team() {
        this.jugadores = new ArrayList<>();
    }

    public Team(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
        this.jugadores = new ArrayList<>();
    }

    // Getters y Setters
    public String getNombreEquipo() { return nombreEquipo; }
    public void setNombreEquipo(String nombreEquipo) { this.nombreEquipo = nombreEquipo; }
    public String getEscudo() { return escudo; }
    public void setEscudo(String escudo) { this.escudo = escudo; }
    public String getColoresUniforme() { return coloresUniforme; }
    public void setColoresUniforme(String coloresUniforme) { this.coloresUniforme = coloresUniforme; }
    public List<User> getJugadores() { return jugadores; }
    public void setJugadores(List<User> jugadores) { this.jugadores = jugadores; }
}