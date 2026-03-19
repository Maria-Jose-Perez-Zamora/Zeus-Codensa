package model;

public class LlaveEliminatoria {
    private String fase;
    private String equipoLocal;
    private String equipoVisitante;
    private String ganador;

    public LlaveEliminatoria(String fase, String equipoLocal, String equipoVisitante) {
        this.fase = fase;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
    }

    public String getFase() { return fase; }
    public void setFase(String fase) { this.fase = fase; }
    public String getEquipoLocal() { return equipoLocal; }
    public void setEquipoLocal(String equipoLocal) { this.equipoLocal = equipoLocal; }
    public String getEquipoVisitante() { return equipoVisitante; }
    public void setEquipoVisitante(String equipoVisitante) { this.equipoVisitante = equipoVisitante; }
    public String getGanador() { return ganador; }
    public void setGanador(String ganador) { this.ganador = ganador; }
}
