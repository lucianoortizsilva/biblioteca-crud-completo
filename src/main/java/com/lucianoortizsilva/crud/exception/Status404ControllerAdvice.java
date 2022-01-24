package com.lucianoortizsilva.crud.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lucianoortizsilva.crud.exception.dto.MensagemErroPadrao;

@ControllerAdvice
class Status404ControllerAdvice {

	static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
	
	@ExceptionHandler(NaoEncontradoException.class)
	ResponseEntity<MensagemErroPadrao> objectNotFound(final NaoEncontradoException e, final HttpServletRequest request) {
		return ResponseEntity.status(HTTP_STATUS)
				.body(MensagemErroPadrao.builder()
						.status(HTTP_STATUS.value())
						.erro(HTTP_STATUS.getReasonPhrase())
						.mensagem(e.getMessage())
						.path(request.getRequestURI())
						.build());
	}
	
}