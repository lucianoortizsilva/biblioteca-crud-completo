package com.lucianoortizsilva.crud.seguranca.autenticacao;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CredencialDTO implements Serializable {

	private static final long serialVersionUID = 4050945595677032300L;

	private String email;
	private String senha;

}