package com.biblioteca.security.authentication.error;

import static java.util.Objects.nonNull;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.biblioteca.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

//@formatter:off
@Slf4j
public abstract class GeraErro {

	private HttpServletResponse response;
	private HttpStatus httpStatus;
	private String mensagemPadrao;

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
			Map<String, Object> erro = new LinkedHashMap<>();
			erro.put("timestamp", LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
			erro.put("datetime", LocalDateTime.now().toString());
			erro.put("status", httpStatus.value());
			erro.put("erro", httpStatus.name());
			erro.put("mensagem", mensagem);
			response.setStatus(httpStatus.value());
			response.setContentType("application/json;charset=UTF-8");
			final String json = JsonUtil.convertToJson(erro);
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

}