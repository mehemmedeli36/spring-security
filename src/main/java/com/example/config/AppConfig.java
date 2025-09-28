package com.example.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.model.User;
import com.example.repository.UserRepository;

@Configuration
public class AppConfig {

	@Autowired 
	private UserRepository userRepository;
	@Bean
	UserDetailsService detailsService() {
		return new UserDetailsService() {
			
			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				Optional<User> optional= userRepository.findByUsernameWithRoles(username);
				if(optional.isPresent()) {
					return optional.get();
				}
				return null;
			}
		};
	}
	@Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {
		  AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
		    builder.userDetailsService(detailsService())
		           .passwordEncoder(bCryptPasswordEncoder());

		    return builder.build();
	}
	
	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
