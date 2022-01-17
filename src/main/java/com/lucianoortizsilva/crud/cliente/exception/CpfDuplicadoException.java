package com.lucianoortizsilva.crud.cliente.exception;

import com.lucianoortizsilva.crud.exception.DadoDuplicadoException;

public class CpfDuplicadoException extends DadoDuplicadoException {

	private static final long serialVersionUID = 4148712982696105L;

	public CpfDuplicadoException() {
		super("O CPF informado já foi cadastrado");
	}

}