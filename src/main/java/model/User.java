package model;

public class User {
    private String nombre;
    private String correo;
    private String contrasena;
    private String posicion;
    private Integer numeroDorsal;
    private String foto;

    public User() {}

    public User(String nombre, String correo, String contrasena) {
        this.nombre = nombre;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
    public Integer getNumeroDorsal() { return numeroDorsal; }
    public void setNumeroDorsal(Integer numeroDorsal) { this.numeroDorsal = numeroDorsal; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
}
