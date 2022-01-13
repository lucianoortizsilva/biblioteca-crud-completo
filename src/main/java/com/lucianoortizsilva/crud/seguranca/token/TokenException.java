package com.lucianoortizsilva.crud.seguranca.token;

public class TokenException extends RuntimeException {

	static final long serialVersionUID = -518567546596988373L;

	public TokenException(final String message) {
		super(message);
	}

}