package com.zeuscodensa.techcupfutbol.core.repository;

import com.zeuscodensa.techcupfutbol.core.model.User;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserRepository {
    Optional<User> findByEmail(String email);
    User save(User user);
    List<User> findAll();
    Page<User> findAll(Pageable pageable);
    void deleteByEmail(String email);
}
