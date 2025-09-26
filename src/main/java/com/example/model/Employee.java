package com.example.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "employee")
@Data
public class Employee {
@Id
@GeneratedValue(strategy = GenerationType.UUID)
private UUID id;
@Column(name = "first_name")
private String firstName;
@Column(name = "last_name")
private String lastName;
}
