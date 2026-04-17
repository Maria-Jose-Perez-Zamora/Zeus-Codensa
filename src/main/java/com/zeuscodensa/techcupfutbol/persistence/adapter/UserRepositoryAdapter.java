package com.zeuscodensa.techcupfutbol.persistence.adapter;

import com.zeuscodensa.techcupfutbol.core.model.User;
import com.zeuscodensa.techcupfutbol.core.repository.IUserRepository;
import com.zeuscodensa.techcupfutbol.persistence.entity.UserEntity;
import com.zeuscodensa.techcupfutbol.persistence.mapper.EntityToModelMapper;
import com.zeuscodensa.techcupfutbol.persistence.mapper.ModelToEntityMapper;
import com.zeuscodensa.techcupfutbol.persistence.repository.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserRepositoryAdapter implements IUserRepository {

    private final UserJpaRepository userJpaRepository;

    public UserRepositoryAdapter(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(EntityToModelMapper::toUserModel);
    }

    @Override
    public User save(User user) {
        UserEntity entity = ModelToEntityMapper.toUserEntity(user);
        UserEntity savedEntity = userJpaRepository.save(entity);
        return EntityToModelMapper.toUserModel(savedEntity);
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(EntityToModelMapper::toUserModel)
                .collect(Collectors.toList());
    }
}
