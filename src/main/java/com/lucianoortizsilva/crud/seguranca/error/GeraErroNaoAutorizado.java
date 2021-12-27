package com.lucianoortizsilva.crud.seguranca.error;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.lucianoortizsilva.crud.exception.dto.MensagemErroPadrao;
import com.lucianoortizsilva.crud.util.JsonUtil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GeraErroNaoAutorizado {

	private HttpServletResponse response;

	@Getter
	private String mensagemErroGerado;

	public GeraErroNaoAutorizado(final HttpServletResponse response) {
		this.response = response;
	}

	public void comMensagem(final String message) {
		try {
			final int status = HttpStatus.UNAUTHORIZED.value();
			final String error = "NAO AUTORIZADO";
			final MensagemErroPadrao mensagemErroPadrao = MensagemErroPadrao
					.builder()
					.mensagem(message)
					.status(status)
					.erro(error)
					.build();
			this.mensagemErroGerado = message;
			response.setStatus(status);
			response.setContentType("application/json;charset=UTF-8");
			response.getOutputStream().println(JsonUtil.convertToJson(mensagemErroPadrao));
		} catch (final Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}