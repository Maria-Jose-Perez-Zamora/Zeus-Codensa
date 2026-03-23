package dependencies.dto;

import core.model.Role;

public class UserRequestDTO {
    private String name;
    private String correo;
    private String contrasena;
    private String posicion;
    private Integer numeroDorsal;
    private String foto;
    private Role role;

    public UserRequestDTO() {}

    public UserRequestDTO(String name, String correo, String contrasena, String posicion, Integer numeroDorsal, String foto, Role role) {
        this.name = name;
        this.correo = correo;
        this.contrasena = contrasena;
        this.posicion = posicion;
        this.numeroDorsal = numeroDorsal;
        this.foto = foto;
        this.role = role;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
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
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
