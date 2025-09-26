package com.example.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "refresh_token")
@Data
public class RefreshToken {
@Id
@GeneratedValue
private UUID id;
private String token;
private OffsetDateTime expired;
private OffsetDateTime createDate;
@ManyToOne
private User user;
}
