package core.model;

public abstract class User {
    private String name;
    private String email;
    private String password;
    private String photo;
    private Role role;
    private UserType type;

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = UserType.EXTERNAL; // Default fallback for legacy tests
    }

    public User(String name, String email, String password, UserType type) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.type = type != null ? type : UserType.EXTERNAL;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public UserType getType() { return type; }
    public void setType(UserType type) { this.type = type; }
}
