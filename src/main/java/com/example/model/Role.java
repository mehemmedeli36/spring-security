package com.example.model;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;


@Entity
@Table(name = "role")
@Data
public class Role {
@Id
@GeneratedValue
private UUID id;
private String name;
@ManyToMany(mappedBy = "roles")
private List<User> users;
}
