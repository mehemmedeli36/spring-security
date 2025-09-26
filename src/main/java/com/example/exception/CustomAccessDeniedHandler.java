package com.example.exception;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.example.dto.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
	@Autowired
	private ObjectMapper mapper;
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.access.AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		Result<Void> result=Result.error(HttpStatus.NOT_ACCEPTABLE, accessDeniedException.getMessage());
		response.setContentType("application/json");
        response.setStatus(403);
        response.getWriter().write(mapper.writeValueAsString(result));
		
	}
}
