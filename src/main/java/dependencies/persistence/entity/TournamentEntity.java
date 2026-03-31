package dependencies.persistence.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tournaments")
public class TournamentEntity {

    @Id
    private String id;

    @Column(nullable = false, unique = true)
    private String tournamentName;

    private String fechaInicio;

    private String fechaFin;

    private Integer numeroEquipos;

    private Double costoInscripcion;

    private String status;

    private String rules;

    private String fechaCierreInscripciones;

    private String fechaInicioFaseGrupos;

    @ElementCollection
    @CollectionTable(name = "tournament_horarios", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "horario")
    private List<String> horariosPartidos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "tournament_canchas", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "cancha")
    private List<String> canchas = new ArrayList<>();

    private String sanctions;

    private String campeon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public void setTournamentName(String tournamentName) {
        this.tournamentName = tournamentName;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Integer getNumeroEquipos() {
        return numeroEquipos;
    }

    public void setNumeroEquipos(Integer numeroEquipos) {
        this.numeroEquipos = numeroEquipos;
    }

    public Double getCostoInscripcion() {
        return costoInscripcion;
    }

    public void setCostoInscripcion(Double costoInscripcion) {
        this.costoInscripcion = costoInscripcion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getFechaCierreInscripciones() {
        return fechaCierreInscripciones;
    }

    public void setFechaCierreInscripciones(String fechaCierreInscripciones) {
        this.fechaCierreInscripciones = fechaCierreInscripciones;
    }

    public String getFechaInicioFaseGrupos() {
        return fechaInicioFaseGrupos;
    }

    public void setFechaInicioFaseGrupos(String fechaInicioFaseGrupos) {
        this.fechaInicioFaseGrupos = fechaInicioFaseGrupos;
    }

    public List<String> getHorariosPartidos() {
        return horariosPartidos;
    }

    public void setHorariosPartidos(List<String> horariosPartidos) {
        this.horariosPartidos = horariosPartidos;
    }

    public List<String> getCanchas() {
        return canchas;
    }

    public void setCanchas(List<String> canchas) {
        this.canchas = canchas;
    }

    public String getSanctions() {
        return sanctions;
    }

    public void setSanctions(String sanctions) {
        this.sanctions = sanctions;
    }

    public String getCampeon() {
        return campeon;
    }

    public void setCampeon(String campeon) {
        this.campeon = campeon;
    }
}
