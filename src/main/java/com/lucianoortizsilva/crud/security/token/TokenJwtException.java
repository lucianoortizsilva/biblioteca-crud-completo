package com.lucianoortizsilva.crud.security.token;

public class TokenJwtException extends RuntimeException {

	static final long serialVersionUID = -518567546596988373L;

	public TokenJwtException(final String message) {
		super(message);
	}

}