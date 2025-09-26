package com.example.jwt;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.dto.TokenDto;
import com.example.model.RefreshToken;
import com.example.model.User;
import com.example.repository.RefreshTokenRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtService {
	@Autowired
	private RefreshTokenRepository refreshTokenRepository;
	
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
	
	public String generateAccessToken(User user) {
		Map<String, List<String>> claimsMap=new HashMap<>();
	    List<String> roles=user.getRoles().stream().map(x->x.getName()).toList();
		claimsMap.put("roles", roles);
        return Jwts.builder()
                .subject(user.getUsername())
                .issuer("my-app")
                .claims(claimsMap)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2))
                .signWith(getKey())
                .compact();
	}
	
	public TokenDto generateToken(User userDetails) {
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
	
	public <T> T exportToken(String token,Function<Claims, T> claimFunction) {
        Claims payload = Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimFunction.apply(payload);
	}
	
	public String getUsernameByToken(String token) {
		return exportToken(token, Claims->Claims.getSubject());
	}
	
	public boolean isTokenExpired(String token) {
		Date date=exportToken(token, Claims::getExpiration);
		return new Date().before(date);
	}
	
	
}
