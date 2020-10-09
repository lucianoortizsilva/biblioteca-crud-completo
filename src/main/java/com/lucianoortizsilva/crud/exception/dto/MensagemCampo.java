package com.lucianoortizsilva.crud.exception.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MensagemCampo implements Serializable {

	private static final long serialVersionUID = -5815227977851988101L;

	private String campo;
	private String mensagem;

}