package com.lucianoortizsilva.crud.seguranca.error;

import static java.util.Objects.nonNull;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.lucianoortizsilva.crud.exception.dto.MensagemErroPadrao;
import com.lucianoortizsilva.crud.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

//@formatter:off
@Slf4j
public abstract class GeraErro {

	private HttpServletResponse response;
	private HttpStatus httpStatus;
	private String mensagemPadrao;
	private String mensagemErroGerado;

	protected GeraErro(final HttpServletResponse response, final HttpStatus httpStatus, final String mensagemPadrao) {
		nonNull(response);
		nonNull(httpStatus);
		nonNull(mensagemPadrao);
		this.response = response;
		this.httpStatus = httpStatus;
		this.mensagemPadrao = mensagemPadrao;
	}

	private void gerarErroCom(final String mensagem) {
		try {
			MensagemErroPadrao mensagemPadraoErro = MensagemErroPadrao
					.builder()
					.status(httpStatus.value())
					.erro(httpStatus.name())
					.mensagem(mensagem)
					.build();
			mensagemErroGerado = mensagemPadraoErro.getMensagem();
			response.setStatus(httpStatus.value());
			response.setContentType("application/json;charset=UTF-8");
			final String json = JsonUtil.convertToJson(mensagemPadraoErro);
			response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	protected void comMensagem(final String mensagem) {
		gerarErroCom(mensagem);
	}
	
	protected void comMensagemPadrao() {
		gerarErroCom(mensagemPadrao);
	}

	protected String getMensagemErroGerado() {
		return mensagemErroGerado;
	}

}