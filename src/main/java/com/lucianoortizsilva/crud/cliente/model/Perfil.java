package com.lucianoortizsilva.crud.cliente.model;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Perfil {

	ADMIN(1, "ADMINISTRADOR"), 
	CLIENTE(2, "CLIENTE");

	private int codigo;
	private String descricao;

	public static Perfil toEnum(final Integer codigo) {
		if (Objects.isNull(codigo)) {
			return null;
		}
		for (final Perfil tc : Perfil.values()) {
			if (codigo.equals(tc.getCodigo())) {
				return tc;
			}
		}
		throw new IllegalArgumentException("Codigo do perfil invalido: " + codigo);
	}

}