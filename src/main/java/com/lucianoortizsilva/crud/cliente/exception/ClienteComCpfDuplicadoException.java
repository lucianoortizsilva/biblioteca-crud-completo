package com.lucianoortizsilva.crud.cliente.exception;

import com.lucianoortizsilva.crud.exception.DadoDuplicadoException;

public class ClienteComCpfDuplicadoException extends DadoDuplicadoException {

	private static final long serialVersionUID = 4148712982696105L;

	public ClienteComCpfDuplicadoException() {
		super("O CPF informado já foi cadastrado");
	}

}