package com.lucianoortizsilva.crud.seguranca.error;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import javax.servlet.http.HttpServletResponse;

public class GeraErroRequisicaoInvalida extends GeraErro {

	private static final String MENSAGEM_PADRAO = "Requisição Inválida";

	public GeraErroRequisicaoInvalida(HttpServletResponse response) {
		super(response, BAD_REQUEST, MENSAGEM_PADRAO);
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