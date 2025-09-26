package com.example.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.example.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Autowired
	private ObjectMapper mapper;
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws java.io.IOException, ServletException {
//		response.setContentType("application/json");
//        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
		Result<Void> result=Result.error(HttpStatus.UNAUTHORIZED, authException.getMessage());
		response.setContentType("application/json");
        response.setStatus(401);
        response.getWriter().write(mapper.writeValueAsString(result));
	}
}
