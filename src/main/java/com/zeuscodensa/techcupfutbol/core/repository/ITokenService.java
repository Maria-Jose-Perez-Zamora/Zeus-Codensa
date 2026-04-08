package com.zeuscodensa.techcupfutbol.core.repository;

public interface ITokenService {
    String generateToken(String email, String role);
}
