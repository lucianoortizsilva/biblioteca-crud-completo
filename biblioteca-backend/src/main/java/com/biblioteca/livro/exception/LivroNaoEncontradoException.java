package com.biblioteca.livro.exception;

import com.biblioteca.exception.NaoEncontradoException;

public class LivroNaoEncontradoException extends NaoEncontradoException {

	private static final long serialVersionUID = -6858656831206266702L;

	public LivroNaoEncontradoException() {
		super("Cliente não encontrado");
	}

}