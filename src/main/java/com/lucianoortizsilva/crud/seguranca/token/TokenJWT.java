package com.lucianoortizsilva.crud.seguranca.token;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lucianoortizsilva.crud.util.JsonUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenJWT {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private Long expiration;

	
	
	public String generateToken(final String username) {
		final Date dhExpiration = new Date(System.currentTimeMillis() + expiration);
		log.info("Date hora expiração: {}", dhExpiration);
		return Jwts.builder()
				.setSubject(username)
				.setExpiration(dhExpiration)
				.signWith(SignatureAlgorithm.HS512, secret.getBytes())
				.compact();
	}
	
	
	
	public Payload getPayload(final String token) {
		final Payload payload = getUsuarioFrom(token);
		validarDataHoraExpiracao(payload.getExpiration());
		return payload;
	}


	
	private Payload getUsuarioFrom(final String fullToken) {
		try {
			final Claims claims = Jwts.parser().setSigningKey(this.secret.getBytes()).parseClaimsJws(fullToken).getBody();
			final String json = JsonUtil.convertToJson(claims);
			return (Payload) JsonUtil.convertToObject(json, Payload.class);
		} catch (final ExpiredJwtException e) {
			throw new TokenException("Token Expirado");
		} catch (final Exception e) {
			throw new TokenException("Erro ao decodificar o payload");
		}
	}
	
	
	
	private static void validarDataHoraExpiracao(final Integer expiration) {
		final Date dhExpiration = new Date((long) expiration * 1000);
		if (dhExpiration.getTime() < new Date(System.currentTimeMillis()).getTime()) {
			throw new TokenException("Token Expirado em: " + dhExpiration);
		}
	}

}