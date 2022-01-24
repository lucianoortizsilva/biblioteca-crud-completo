package com.lucianoortizsilva.crud.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lucianoortizsilva.crud.exception.dto.MensagemErroPadrao;

@ControllerAdvice
class Status403ControllerAdvice {
	
	static final HttpStatus HTTP_STATUS = HttpStatus.FORBIDDEN;

	@ExceptionHandler(NaoAutorizadoException.class)
	ResponseEntity<MensagemErroPadrao> authorization(final NaoAutorizadoException e, final HttpServletRequest request) {
		return ResponseEntity.status(HTTP_STATUS)
				.body(MensagemErroPadrao.builder()
						.status(HTTP_STATUS.value())
						.erro(HTTP_STATUS.getReasonPhrase())
						.mensagem(e.getMessage())
						.path(request.getRequestURI())
						.build());
	}

}