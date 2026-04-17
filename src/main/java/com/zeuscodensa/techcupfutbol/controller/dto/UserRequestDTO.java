package com.zeuscodensa.techcupfutbol.controller.dto;

import com.zeuscodensa.techcupfutbol.core.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UserRequestDTO {
    @NotBlank(message = "El nombre no puede estar vacío")
    private String name;
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "Debe ser un email válido")
    private String email;
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
    
    private String position;
    private Integer jerseyNumber;
    private String photo;
    
    @NotNull(message = "El rol no puede ser nulo")
    private Role role;
    
    private String userType;

    public UserRequestDTO() {}

    public UserRequestDTO(String name, String email, String password, String position, Integer jerseyNumber, String photo, Role role, String userType) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.position = position;
        this.jerseyNumber = jerseyNumber;
        this.photo = photo;
        this.role = role;
        this.userType = userType;
    }

    // Legacy 7-arg constructor for backward compatibility
    public UserRequestDTO(String name, String email, String password, String position, Integer jerseyNumber, String photo, Role role) {
        this(name, email, password, position, jerseyNumber, photo, role, "EXTERNAL");
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(Integer jerseyNumber) { this.jerseyNumber = jerseyNumber; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public String getUserType() { return userType; }
    public void setUserType(String userType) { this.userType = userType; }
}
