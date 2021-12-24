package com.lucianoortizsilva.crud.seguranca.erro;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.lucianoortizsilva.crud.exception.dto.MensagemErroPadrao;
import com.lucianoortizsilva.crud.util.JsonUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeraErroNaoEncontrado {

	private HttpServletResponse response;

	@Getter
	private String mensagemErroGerado;

	public GeraErroNaoEncontrado(final HttpServletResponse response) {
		this.response = response;
	}

	public void comMensagem(final String mensagem) {
		try {
			final int status = HttpStatus.NOT_FOUND.value();
			final String error = "NOT FOUND";
			final MensagemErroPadrao mensagemErroPadrao = MensagemErroPadrao
					.builder()
					.mensagem(mensagem)
					.status(status)
					.erro(error)
					.build();
			this.mensagemErroGerado = mensagem;
			response.setStatus(status);
			response.setContentType("application/json;charset=UTF-8");
			response.getOutputStream().println(JsonUtil.convertToJson(mensagemErroPadrao));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}