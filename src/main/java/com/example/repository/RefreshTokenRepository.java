package com.example.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.model.RefreshToken;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID>{

	Optional<RefreshToken> findByToken(String token);
}
