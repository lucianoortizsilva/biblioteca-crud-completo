package com.biblioteca.exception;

/**
 * 
 * HTTP 404
 *
 */
public class NaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = -8979247949102244261L;

	public NaoEncontradoException(final String mensagem) {
		super(mensagem);
	}

	public NaoEncontradoException(final String mensagem, Throwable e) {
		super(mensagem);
	}

}