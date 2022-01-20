package com.lucianoortizsilva.crud.security.authentication.error;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import javax.servlet.http.HttpServletResponse;

public class GeraErroInesperado extends GeraErro {

	private static final String MENSAGEM_PADRAO = "Erro inesperado";

	public GeraErroInesperado(final HttpServletResponse response) {
		super(response, INTERNAL_SERVER_ERROR, MENSAGEM_PADRAO);
	}

	@Override
	public void comMensagem(final String mensagem) {
		super.comMensagem(mensagem);
	}

	@Override
	public void comMensagemPadrao() {
		super.comMensagemPadrao();
	}

}