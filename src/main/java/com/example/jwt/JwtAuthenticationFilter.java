package com.example.jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsService userDetailsService;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

	    String header = request.getHeader("Authorization");

	    if (header == null || !header.startsWith("Bearer ")) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    String token = header.substring(7);

	    try {
	        String username = jwtService.getUsernameByToken(token);

	        if (username != null) {
	            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	            if (userDetails != null) {
	                UsernamePasswordAuthenticationToken authentication =
	                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authentication);
	            }
	        }
	    } catch (Exception e) {
	       System.out.println(e.getMessage());
	    }
	    filterChain.doFilter(request, response);
	}
}
