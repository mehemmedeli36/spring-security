package com.example.service;

import java.util.Optional;

import com.example.dto.LoginDto;
import com.example.dto.RegisterDto;
import com.example.dto.TokenDto;
import com.example.dto.UserDto;
import com.example.model.User;

public interface UserService {
Optional<User> findByUsername(String username);
public UserDto register(RegisterDto dto);
public TokenDto login(LoginDto dto);
}
