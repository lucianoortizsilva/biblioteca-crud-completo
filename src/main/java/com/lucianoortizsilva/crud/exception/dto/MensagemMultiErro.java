package com.lucianoortizsilva.crud.exception.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class MensagemMultiErro extends MensagemErroPadrao {

	private static final long serialVersionUID = 7559055929703116325L;

	private List<MensagemCampo> erros = new ArrayList<>();

	public MensagemMultiErro(final Integer status, final String erro, final String mensagem, final String path) {
		super(status, erro, mensagem, path);
	}

	public void addErro(final String campo, final String mensagem) {
		this.erros.add(new MensagemCampo(campo, mensagem));
	}

}