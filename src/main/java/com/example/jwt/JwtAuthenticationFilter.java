package com.example.jwt;

import java.io.IOException;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.model.User;
import com.example.utility.CookieHelper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String token = CookieHelper.getValue(request, "access_token");
		String refreshToken = CookieHelper.getValue(request, "refresh_token");
		if (token == null && refreshToken == null) {
			filterChain.doFilter(request, response);
			return;
		}
		if (token == null && refreshToken != null) {
			token = jwtService.refreshAccessToken(refreshToken);
			if (token == null) {
				filterChain.doFilter(request, response);
				return;
			}
			CookieHelper.setCookie("access_token", token, Duration.ofMinutes(10), response);
		}

		User user = jwtService.getUserByToken(token);
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
				user.getAuthorities());

		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}
}
