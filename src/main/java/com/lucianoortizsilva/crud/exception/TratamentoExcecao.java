package com.lucianoortizsilva.crud.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.lucianoortizsilva.crud.exception.dto.MensagemErroPadrao;
import com.lucianoortizsilva.crud.exception.dto.ValidaErro;

@ControllerAdvice
class TratamentoExcecao {

	@ExceptionHandler(NaoEncontradoException.class)
	public ResponseEntity<MensagemErroPadrao> objectNotFound(final NaoEncontradoException e, final HttpServletRequest request) {
		final HttpStatus status =  HttpStatus.NOT_FOUND;
		final MensagemErroPadrao mensagemErro = MensagemErroPadrao
				.builder()
				.status(status.value())
				.erro(status.getReasonPhrase())
				.mensagem(e.getMessage())
				.path(request.getRequestURI())
				.build();
		return ResponseEntity.status(status).body(mensagemErro);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<MensagemErroPadrao> validation(final MethodArgumentNotValidException e, final HttpServletRequest request) {
		final HttpStatus status =  HttpStatus.UNPROCESSABLE_ENTITY;
		final ValidaErro validaErro = new ValidaErro(status.value(), status.getReasonPhrase(), "Erro de validação", request.getRequestURI());
		for(final FieldError fe: e.getBindingResult().getFieldErrors()) {
			validaErro.addErro(fe.getField(), fe.getDefaultMessage());
		}
		return ResponseEntity.status(status).body(validaErro);
	}

	@ExceptionHandler(NaoAutorizadoException.class)
	public ResponseEntity<MensagemErroPadrao> authorization(final NaoAutorizadoException e, final HttpServletRequest request) {
		final HttpStatus status =  HttpStatus.FORBIDDEN;
		final MensagemErroPadrao mensagemErro = MensagemErroPadrao
				.builder()
				.status(status.value())
				.erro(status.getReasonPhrase())
				.mensagem(e.getMessage())
				.path(request.getRequestURI())
				.build();
		return ResponseEntity.status(status).body(mensagemErro);
	}	
	
	@ExceptionHandler(DadoDuplicadoException.class)
	public ResponseEntity<MensagemErroPadrao> dataIntegrity(final DadoDuplicadoException e, final HttpServletRequest request) {
		final HttpStatus status =  HttpStatus.BAD_REQUEST;
		final MensagemErroPadrao mensagemErro = MensagemErroPadrao
				.builder()
				.status(status.value())
				.erro(status.getReasonPhrase())
				.mensagem(e.getMessage())
				.path(request.getRequestURI())
				.build();
		return ResponseEntity.status(status).body(mensagemErro);
	}
	
}