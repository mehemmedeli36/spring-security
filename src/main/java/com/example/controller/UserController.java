package com.example.controller;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.example.utility.CookieHelper;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping(path = "/user/get")
	public Optional<User> get(@PathVariable String username) {
		return userService.findByUsername(username);
	}

	@PostMapping(path = "/user/register")
	public UserDto register(@RequestBody RegisterDto registerDto) {
		return userService.register(registerDto);
	}

	@PostMapping(path = "/user/login")
	public Boolean login(@RequestBody LoginDto registerDto, HttpServletResponse response) {
		TokenDto tokens = userService.login(registerDto);
		if (tokens == null)
			return false;
		CookieHelper.setCookie("access_token", tokens.getAccessToken(), Duration.ofMinutes(10), response);
		CookieHelper.setCookie("resfresh_token", tokens.getRefreshToken(), Duration.ofDays(20), response);
		return true;
	}
}
