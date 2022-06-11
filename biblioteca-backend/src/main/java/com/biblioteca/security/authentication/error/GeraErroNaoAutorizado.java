package com.biblioteca.security.authentication.error;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import javax.servlet.http.HttpServletResponse;

public class GeraErroNaoAutorizado extends GeraErro {

	private static final String MENSAGEM_PADRAO = "Não Autorizado";

	public GeraErroNaoAutorizado(final HttpServletResponse response) {
		super(response, UNAUTHORIZED, MENSAGEM_PADRAO);
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