package com.lucianoortizsilva.crud.exception.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class ValidaErro extends MensagemErroPadrao {

	private static final long serialVersionUID = 7559055929703116325L;

	private List<MensagemCampo> erros = new ArrayList<>();

	public ValidaErro(final Integer status, final String erro, final String mensagem, final String path) {
		super(status, erro, mensagem, path);
	}

	public void addErro(final String campo, final String mensagem) {
		this.erros.add(new MensagemCampo(campo, mensagem));
	}

}