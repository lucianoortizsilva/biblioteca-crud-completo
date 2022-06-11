package com.biblioteca.security.authentication.error;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import javax.servlet.http.HttpServletResponse;

public class GeraErroNaoEncontrado extends GeraErro {

	private static final String MENSAGEM_PADRAO = "Não Encontrado";

	public GeraErroNaoEncontrado(final HttpServletResponse response) {
		super(response, NOT_FOUND, MENSAGEM_PADRAO);
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