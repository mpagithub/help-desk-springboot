package com.marceloSpringboot.helpdesk.api.Jwt.security;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable{

	private static final long serialVersionUID = 1L;

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_CREATED = "created";
	static final String CLAIM_KEY_EXPIRED = "exp";
	
	
	
	// Variáveis  que definimos no application.properties
	
	@Value("${jwt.secret}")
	private String secret;  
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	
	
	// Este método vai obter o email que está dentro do token(já que é o email nosso username de acesso)
	public String getUserNameFromToken(String token) {
		
		String username;
		try {
			final Claims claims = getClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
		
	}
	
	// Vai retornar a data de expiração de um token JWT
	public Date getExpirationDateFromToken(String token) {
		
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration(); 
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
		
	}
	
	
	// Realiza o parsing do token JWT, para extrair as informações contidas nele
	private Claims getClaimsFromToken(String token) {
		
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		} 	
		return claims;
		
	}
	
	
	// Vai verificar se o token está expirado
	private Boolean isTokenExpired(String token) {
		
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
		
	}
	
	
	// Responsável por gerar o token
	public String generateToken(UserDetails userDetails) {
		
		Map<String, Object> claims = new HashMap<>();
		
		claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		
		final Date createdDate = new Date();
		claims.put(CLAIM_KEY_CREATED, createdDate);
		
		return doGenerateToken(claims);
		
	}
	private String doGenerateToken(Map<String, Object> claims) {
		
		final Date createdDate = (Date) claims.get(CLAIM_KEY_CREATED);
		final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
		return Jwts.builder()
				   .setClaims(claims)
				   .setExpiration(expirationDate)
				   .signWith(SignatureAlgorithm.HS512, secret)
				   .compact();
		
	}
	
	
	// Verifica se o token pode ser atualizado
	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token));
	}
	
	
	// Atualiza o token
	public String refreshToken(String token) {
		
		String refreshedToken;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put(CLAIM_KEY_CREATED, new Date());
			refreshedToken = doGenerateToken(claims);
		} catch (Exception e) {
			refreshedToken = null;
		}
		
		return refreshedToken;
		
	}
	
	
	// Verifica se o token está válido
	 public Boolean validateToken(String token, UserDetails userDetails) {
		
		JwtUser user = (JwtUser) userDetails;
		final String username = getUserNameFromToken(token);
		return (username.equals(user.getUsername()) && !isTokenExpired(token));
		
	} 
	
}
