package com.example.controller;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.LoginDto;
import com.example.dto.RegisterDto;
import com.example.dto.TokenDto;
import com.example.dto.UserDto;
import com.example.model.User;
import com.example.service.UserService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping(path = "/user/get")
	public Optional<User> get(@PathVariable String username){
		return userService.findByUsername(username);
	}
	
	@PostMapping(path = "/user/register")
	public UserDto register(@RequestBody RegisterDto registerDto) {
		return userService.register(registerDto);
	}
	
	@PostMapping(path = "/user/login")
	public boolean login(@RequestBody LoginDto registerDto,HttpServletResponse response) {
		TokenDto tokens= userService.login(registerDto);

		if(tokens==null) return false;
	    ResponseCookie accessCookie = ResponseCookie.from("access_token", tokens.getAccessToken())
	            .httpOnly(true)
	            .secure(true)
	            .path("/")
	            .sameSite("Strict")
	            .maxAge(Duration.ofMinutes(15))
	            .build();
	    
	    ResponseCookie refreshCookie = ResponseCookie.from("refresh_token", tokens.getRefreshToken())
	            .httpOnly(true)
	            .secure(true)
	            .path("/api/auth/refresh")
	            .sameSite("Strict")
	            .maxAge(Duration.ofDays(20))
	            .build();
	    response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
	    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
	    return true;
	}
}
