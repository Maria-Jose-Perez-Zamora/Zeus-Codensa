package com.zeuscodensa.techcupfutbol.controller.dto;

import com.zeuscodensa.techcupfutbol.core.model.Role;
import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.model.Player;
import com.zeuscodensa.techcupfutbol.core.model.UserType;

public class UserResponseDTO {
    private String name;
    private String email;
    private String position;
    private Integer jerseyNumber;
    private String photo;
    private Role role;
    private UserType userType;

    public UserResponseDTO() {}

    public UserResponseDTO(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.photo = user.getPhoto();
        this.role = user.getRole();
        this.userType = user.getType();
        
        if (user instanceof Player) {
            Player j = (Player) user;
            this.position = j.getPosition();
            this.jerseyNumber = j.getJerseyNumber();
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public Integer getJerseyNumber() { return jerseyNumber; }
    public void setJerseyNumber(Integer jerseyNumber) { this.jerseyNumber = jerseyNumber; }
    public String getPhoto() { return photo; }
    public void setPhoto(String photo) { this.photo = photo; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }
}
