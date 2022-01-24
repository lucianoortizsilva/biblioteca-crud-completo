package com.lucianoortizsilva.crud.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lucianoortizsilva.crud.exception.dto.MensagemErroPadrao;
import com.lucianoortizsilva.crud.exception.dto.MensagemMultiErro;

@ControllerAdvice
class Status422ControllerAdvice {

	static final HttpStatus HTTP_STATUS = HttpStatus.UNPROCESSABLE_ENTITY;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MensagemErroPadrao> validation(final MethodArgumentNotValidException e, final HttpServletRequest request) {
		final MensagemMultiErro mensagemMultiErro = new MensagemMultiErro(HTTP_STATUS.value(), HTTP_STATUS.getReasonPhrase(), e.getMessage(), request.getRequestURI());
		for (final FieldError fe : e.getBindingResult().getFieldErrors()) {
			mensagemMultiErro.addErro(fe.getField(), fe.getDefaultMessage());
		}
		return ResponseEntity.status(HTTP_STATUS).body(mensagemMultiErro);
	}

}