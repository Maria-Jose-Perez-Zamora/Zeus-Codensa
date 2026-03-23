package dependencias.dto;

import core.model.Role;
import core.model.User;
import core.model.Jugador;

public class UserResponseDTO {
    private String nombre;
    private String correo;
    private String posicion;
    private Integer numeroDorsal;
    private String foto;
    private Role role;

    public UserResponseDTO() {}

    public UserResponseDTO(User user) {
        this.nombre = user.getNombre();
        this.correo = user.getCorreo();
        this.foto = user.getFoto();
        this.role = user.getRole();
        
        if (user instanceof Jugador) {
            Jugador j = (Jugador) user;
            this.posicion = j.getPosicion();
            this.numeroDorsal = j.getNumeroDorsal();
        }
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPosicion() { return posicion; }
    public void setPosicion(String posicion) { this.posicion = posicion; }
    public Integer getNumeroDorsal() { return numeroDorsal; }
    public void setNumeroDorsal(Integer numeroDorsal) { this.numeroDorsal = numeroDorsal; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
