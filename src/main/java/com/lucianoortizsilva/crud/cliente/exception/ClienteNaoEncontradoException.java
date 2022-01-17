package com.lucianoortizsilva.crud.cliente.exception;

import com.lucianoortizsilva.crud.exception.NaoEncontradoException;

public class ClienteNaoEncontradoException extends NaoEncontradoException {

	private static final long serialVersionUID = -6858656831206266702L;

	public ClienteNaoEncontradoException() {
		super("Cliente não econtrado");
	}

}