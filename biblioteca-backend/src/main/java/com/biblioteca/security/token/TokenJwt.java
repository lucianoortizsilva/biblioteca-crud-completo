package com.biblioteca.security.token;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.biblioteca.util.JsonUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

//@formatter:off
@Slf4j
@Component
public class TokenJwt {

	@Value("${app.jwt.secret}")
	private String secret;

	@Value("${app.jwt.expiration}")
	private Long expiration;
	
	
	
	public String generateToken(final String username) {
		final Date dhExpiration = new Date(System.currentTimeMillis() + expiration);
		final byte[] secretKey = secret.getBytes();
		log.info("Date hora expiração: {}", dhExpiration);
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(dhExpiration)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	
	
	public Payload getPayload(final String authorization) {
		final Payload payload = getUsuarioFrom(authorization);
		validarDataHoraExpiracao(payload.getExpiration());
		return payload;
	}

	
	
	public String updateExpirationDateToken(final String authorization) {
		Claims claims = getClaimsFromToken(authorization);
		final Date dhExpiration = new Date(System.currentTimeMillis() + expiration);
		final byte[] secretKey = secret.getBytes();
		log.info("Date hora expiração renovada até: {}", dhExpiration);
		return Jwts.builder()
				.setClaims(claims)
				.setExpiration(dhExpiration)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
	}

	
	
	private Claims getClaimsFromToken(String fullToken) {
		final byte[] secretKey = secret.getBytes();
		final String token = fullToken.substring("Bearer".length()).trim();
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody();
	}

	
	
	private Payload getUsuarioFrom(final String authorization) {
		try {
			final byte[] secretKey = secret.getBytes();
			final String token = authorization.substring("Bearer".length()).trim();
			final Claims claims = Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody();
			final String json = JsonUtil.convertToJson(claims);
			return (Payload) JsonUtil.convertToObject(json, Payload.class);
		} catch (final ExpiredJwtException e) {
			throw new TokenJwtException("Token Expirado");
		} catch (final Exception e) {
			throw new TokenJwtException("Token Inválido");
		}
	}

	
	
	private static void validarDataHoraExpiracao(final Integer expiration) {
		final Date dhExpiration = new Date((long) expiration * 1000);
		if (dhExpiration.getTime() < new Date(System.currentTimeMillis()).getTime()) {
			throw new TokenJwtException("Token Expirado em: " + dhExpiration);
		}
	}

}