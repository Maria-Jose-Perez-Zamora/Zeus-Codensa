package core.model;

public abstract class User {
    private String name;
    private String correo;
    private String contrasena;
    private String foto;
    private Role role;
    private UserType type;

    public User() {}

    public User(String name, String correo, String contrasena) {
        this.name = name;
        this.correo = correo;
        this.contrasena = contrasena;
        this.type = UserType.EXTERNAL; // Default fallback for old tests
    }

    public User(String name, String correo, String contrasena, UserType type) {
        this.name = name;
        this.correo = correo;
        this.contrasena = contrasena;
        this.type = type != null ? type : UserType.EXTERNAL;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public UserType getType() { return type; }
    public void setType(UserType type) { this.type = type; }
}
