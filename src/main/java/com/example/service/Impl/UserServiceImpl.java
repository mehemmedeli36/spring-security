package com.example.service.Impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.dto.LoginDto;
import com.example.dto.RegisterDto;
import com.example.dto.TokenDto;
import com.example.dto.UserDto;
import com.example.jwt.JwtService;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	@Override
	
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsernameWithRoles(username);
	}
	@Override
	public UserDto register(RegisterDto dto) {
		User user=new User();
		user.setUsername(dto.getUsername());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		
		User dbUser=userRepository.save(user);
		UserDto userDto=new UserDto();
		userDto.setUsername(dbUser.getUsername());
		userDto.setPassword(dbUser.getPassword());
		return userDto;
	}
	@Override
	public TokenDto login(LoginDto dto) {
		Optional<User> optional=userRepository.findByUsernameWithRoles(dto.getUsername());
		if(optional.isPresent()) {
			User user= optional.get();
			return jwtService.generateTokens(user);
		}
		return null;
	}
	
	

}
