package com.lucianoortizsilva.crud.pessoa.util;

import java.time.LocalDate;
import java.util.Optional;

import com.lucianoortizsilva.crud.pessoa.model.Pessoa;

public class PessoaStub {

	public static Optional<Pessoa> getPessoa() {
		return Optional.of(new Pessoa("Luciano", "91458266547", LocalDate.of(2020, 01, 31), "lucianoortizsilva@gmail.com", "123456"));
	}
	
}