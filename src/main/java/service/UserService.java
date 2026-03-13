package service;

import model.User;
import util.DataStorage;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserService {

    public String registerUser(User user) {
        for (User u : DataStorage.users) {
            if (u.getCorreo().equals(user.getCorreo())) {
                return "Error: El correo ya existe.";
            }
        }
        DataStorage.users.add(user);
        return "Usuario " + user.getNombre() + " registrado exitosamente.";
    }

    public List<User> getAllUsers() {
        return DataStorage.users;
    }
}