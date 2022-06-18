package com.biblioteca.livro.exception;

import com.biblioteca.exception.DadoDuplicadoException;

public class LivroComIsbnDuplicadoException extends DadoDuplicadoException {

	private static final long serialVersionUID = 4148712982696105L;

	public LivroComIsbnDuplicadoException() {
		super("O ISBN informado já foi cadastrado");
	}

}