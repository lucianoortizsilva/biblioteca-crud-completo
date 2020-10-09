package com.lucianoortizsilva.crud.exception;

public class AutorizacaoException extends RuntimeException {

	private static final long serialVersionUID = -3623720129927238779L;

	public AutorizacaoException(final String mensagem) {
		super(mensagem);
	}

	public AutorizacaoException(final String mensagem, final Throwable e) {
		super(mensagem);
	}

}