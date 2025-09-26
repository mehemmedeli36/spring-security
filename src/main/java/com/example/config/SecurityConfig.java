package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.exception.CustomAccessDeniedHandler;
import com.example.exception.JwtAuthenticationEntryPoint;
import com.example.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Lazy
	@Autowired
	private AuthenticationManager authManager;
	@Lazy
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
	@Autowired
	private CustomAccessDeniedHandler accessDeniedHandler;
	
	@Autowired 
	private JwtAuthenticationEntryPoint entryPoint;
	
//	public  SecurityConfig(AuthenticationManager authManager,JwtAuthenticationFilter authenticationFilter) {
//		this.authManager=authManager;
//		this.authenticationFilter=authenticationFilter;
//	}
	
	@Bean
     SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	   http.exceptionHandling(exceptions-> exceptions.accessDeniedHandler(accessDeniedHandler).authenticationEntryPoint(entryPoint)).csrf(csrf -> csrf.disable()).authorizeHttpRequests(request->request
			   .requestMatchers("/employee/**").hasRole("ADMIN")
			   .requestMatchers("/department/**").authenticated()
			   .requestMatchers("/test/**").hasAnyRole("USER","ADMIN")
	            .anyRequest().permitAll()
	            )
	   .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	   .authenticationManager(authManager)
	   .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
	return http.build();
	}
}
