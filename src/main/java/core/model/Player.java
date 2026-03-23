package core.model;

public class Player extends User {
    private String posicion;
    private Integer numeroDorsal;

    public Player() {}

    public Player(String posicion, Integer numeroDorsal) {
        this.posicion = posicion;
        this.numeroDorsal = numeroDorsal;
    }

    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
    public Integer getNumeroDorsal() { return numeroDorsal; }
    public void setNumeroDorsal(Integer numeroDorsal) { this.numeroDorsal = numeroDorsal; }
}
