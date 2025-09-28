package com.example.jwt;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.dto.TokenDto;
import com.example.model.RefreshToken;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RefreshTokenRepository;
import com.example.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	@Autowired 
	private UserRepository userRepository;
	
	public static final String SECRET_KEY = "kIduUVUPApcD4UzqbfLOB/OwrED69kUXWPpGBBNZZFk=";
	
	public String generateRefreshToken(User user) {
		String token= UUID.randomUUID().toString();
		RefreshToken refreshToken=new RefreshToken();
		refreshToken.setCreateDate(OffsetDateTime.now(ZoneOffset.UTC));
		refreshToken.setExpired(OffsetDateTime.now(ZoneOffset.UTC).plusDays(20));
		refreshToken.setToken(token);
		refreshToken.setUser(user);
		refreshTokenRepository.save(refreshToken);
		return token;
	}
	
	public String refreshAccessToken(String refreshToken) {
		Optional<RefreshToken> optional= refreshTokenRepository.findByToken(refreshToken);
		if(!optional.isPresent()) return null;
		String accessToken=generateAccessToken(userRepository.findByUsernameWithRoles(optional.get().getUser().getUsername()).get());
		return accessToken;
	}
	
	public String generateAccessToken(User user) {
	    List<String> roles = Optional.ofNullable(user.getRoles()).orElse(Collections.emptyList())
                .stream().map(Role::getName).toList();

               Map<String, Object> claimsMap = new HashMap<>();
               claimsMap.put("roles", roles);

        return Jwts.builder()
                .subject(user.getUsername())
                .issuer("my-app")
                .claims(claimsMap)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 *10))
                .signWith(getKey())
                .compact();
	}
	
	public TokenDto generateTokens(User userDetails) {
          String accessToken= generateAccessToken(userDetails);
          String refreshToken=generateRefreshToken(userDetails);
          return new TokenDto(accessToken,refreshToken);
	}
	
	public SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
	}
	
	public Object getClaimsByKey(String token,String key) {
		Claims claims=getClaims(token);
		return claims.get(key);
	}
	
	public Claims getClaims(String token) {
         Claims payload = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
         return payload;
	}
	
	public Claims exportToken(String token) {
        Claims payload = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return payload;
	}
	
	public User getUserByToken(String token) {
		Claims claims= exportToken(token);
		List<String> roles = Optional.ofNullable(claims.get("roles", List.class))
				
		        .map(list -> ((List<?>) list).stream()
		                .map(Object::toString)
		                .toList()
		        )
		        .orElse(Collections.emptyList());
		
		List<Role> rollsList= roles.stream().map(x->
		{
			Role role=new Role(); 
			role.setName(x);
			return role;
		}).toList();

		User user=new User();
		user.setUsername(claims.getSubject());
		user.setRoles(rollsList);
		return user;
	}
	
	public boolean isTokenExpired(String token) {
		Date date=exportToken(token).getExpiration();
		return new Date().before(date);
	}
	
	
}
