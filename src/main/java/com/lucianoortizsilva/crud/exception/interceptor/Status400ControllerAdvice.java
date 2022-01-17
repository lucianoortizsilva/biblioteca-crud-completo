package com.lucianoortizsilva.crud.exception.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lucianoortizsilva.crud.exception.DadoDuplicadoException;
import com.lucianoortizsilva.crud.exception.model.MensagemErroPadrao;

@ControllerAdvice
class Status400ControllerAdvice {

	static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
	
	@ExceptionHandler(DadoDuplicadoException.class)
	ResponseEntity<MensagemErroPadrao> dataIntegrity(final DadoDuplicadoException e, final HttpServletRequest request) {
		return ResponseEntity.status(HTTP_STATUS)
				.body(MensagemErroPadrao.builder()
						.status(HTTP_STATUS.value())
						.erro(HTTP_STATUS.getReasonPhrase())
						.mensagem(e.getMessage())
						.path(request.getRequestURI())
						.build());
	}

	
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<MensagemErroPadrao> dataIntegrity(final HttpMessageNotReadableException e, final HttpServletRequest request) {
		return ResponseEntity.status(HTTP_STATUS)
				.body(MensagemErroPadrao.builder()
						.status(HTTP_STATUS.value())
						.erro(HTTP_STATUS.getReasonPhrase())
						.mensagem(e.getMessage())
						.path(request.getRequestURI())
						.build());
	}
	
}