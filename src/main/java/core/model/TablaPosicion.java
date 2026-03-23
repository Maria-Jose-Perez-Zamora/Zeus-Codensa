package core.model;

public class TablaPosicion {
    private String nombreEquipo;
    private int partidosJugados;
    private int partidosGanados;
    private int partidosEmpatados;
    private int partidosPerdidos;
    private int golesFavor;
    private int golesContra;
    private int diferenciaGoles;
    private int puntos;

    public TablaPosicion(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
        this.partidosJugados = 0;
        this.partidosGanados = 0;
        this.partidosEmpatados = 0;
        this.partidosPerdidos = 0;
        this.golesFavor = 0;
        this.golesContra = 0;
        this.diferenciaGoles = 0;
        this.puntos = 0;
    }

    public void registrarPartido(int golesFavor, int golesContra) {
        this.partidosJugados++;
        this.golesFavor += golesFavor;
        this.golesContra += golesContra;
        this.diferenciaGoles = this.golesFavor - this.golesContra;

        if (golesFavor > golesContra) {
            this.partidosGanados++;
            this.puntos += 3;
        } else if (golesFavor == golesContra) {
            this.partidosEmpatados++;
            this.puntos += 1;
        } else {
            this.partidosPerdidos++;
        }
    }

    public String getNombreEquipo() { return nombreEquipo; }
    public int getPartidosJugados() { return partidosJugados; }
    public int getPartidosGanados() { return partidosGanados; }
    public int getPartidosEmpatados() { return partidosEmpatados; }
    public int getPartidosPerdidos() { return partidosPerdidos; }
    public int getGolesFavor() { return golesFavor; }
    public int getGolesContra() { return golesContra; }
    public int getDiferenciaGoles() { return diferenciaGoles; }
    public int getPuntos() { return puntos; }
}
