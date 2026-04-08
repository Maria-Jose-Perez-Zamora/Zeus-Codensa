package com.zeuscodensa.techcupfutbol.core.repository;

import com.zeuscodensa.techcupfutbol.core.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
    List<User> findAll();
}
