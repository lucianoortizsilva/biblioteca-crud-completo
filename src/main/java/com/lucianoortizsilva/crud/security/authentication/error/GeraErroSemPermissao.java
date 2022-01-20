package com.lucianoortizsilva.crud.security.authentication.error;

import static org.springframework.http.HttpStatus.FORBIDDEN;

import javax.servlet.http.HttpServletResponse;

public class GeraErroSemPermissao extends GeraErro {

	private static final String MENSAGEM_PADRAO = "Não permitido";

	public GeraErroSemPermissao(final HttpServletResponse response) {
		super(response, FORBIDDEN, MENSAGEM_PADRAO);
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