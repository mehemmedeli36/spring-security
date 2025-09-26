package com.example.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;
private String username;
private String password;
@ManyToMany()
private List<Role> roles;
@OneToMany(mappedBy = "user",orphanRemoval = true,cascade = CascadeType.REMOVE)
private List<RefreshToken> refreshTokens;

@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
	return List.of();
}

}
