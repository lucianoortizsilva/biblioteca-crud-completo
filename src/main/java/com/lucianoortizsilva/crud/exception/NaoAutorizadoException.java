package com.lucianoortizsilva.crud.exception;

public class NaoAutorizadoException extends RuntimeException {

	private static final long serialVersionUID = -3623720129927238779L;

	public NaoAutorizadoException(final String mensagem) {
		super(mensagem);
	}

	public NaoAutorizadoException(final String mensagem, final Throwable e) {
		super(mensagem);
	}

}