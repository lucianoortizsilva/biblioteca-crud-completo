package com.lucianoortizsilva.crud.exception;

/**
 * 
 * HTTP 400
 *
 */
public class DadoDuplicadoException extends RuntimeException {

	private static final long serialVersionUID = 2348711512741441990L;

	public DadoDuplicadoException(final String mensagem) {
		super(mensagem);
	}

	public DadoDuplicadoException(final String mensagem, final Throwable e) {
		super(mensagem, e);
	}
}