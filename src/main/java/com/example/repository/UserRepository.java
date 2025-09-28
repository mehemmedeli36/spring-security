package com.example.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
	@Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.username = :username")
	Optional<User> findByUsernameWithRoles(@Param("username") String username);
}